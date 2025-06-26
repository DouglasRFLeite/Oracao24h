const BASE_URL = import.meta.env.VITE_BASE_URL as string;
const API_MODE = import.meta.env.VITE_API_MODE as string;

// Tipos para os dados que serão retornados ou enviados
export interface PrayerTime {
  periodId: string;
  timeId: string;
  timeString: string;
  name: string;
}

export interface PrayerPeriod {
  periodId: string;
  timeId: string;
  church: string;
  startDate: Date;
  endDate: Date;
  reason: string;
}

export interface PeriodWithTime {
  period: PrayerPeriod;
  times: PrayerTime[];
}


export interface CreatePrayerTimeResponse {
  timeId: string;
  message: string;
}

// Helper to format periodId with "PERIOD#"
function formatPeriodId(periodId: string): string {
  return periodId.startsWith("PERIOD#") ? periodId : `PERIOD#${periodId}`;
}

function buildApiRequest(endpoint: string, body: any): { url: string; headers: Record<string, string>; body: string } {
  if (API_MODE === 'local') {
    // Local: endpoint is /createTime, /createPeriod, etc.
    return {
      url: `${BASE_URL}/${endpoint}`,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    };
  } else {
    // Prod: always POST to / with header
    return {
      url: BASE_URL,
      headers: {
        "spring.cloud.function.definition": endpoint,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    };
  }
}

// Buscar período com horários
export async function fetchPeriod(periodId: string): Promise<PeriodWithTime> {
  const { url, headers, body } = buildApiRequest(
    'fetchPeriod',
    { periodId: formatPeriodId(periodId) }
  );
  const res = await fetch(url, {
    method: "POST",
    headers,
    body,
  });
  if (!res.ok) throw new Error("Erro ao buscar período");
  return res.json();
}

// Criar período de oração
export async function createPrayerPeriod(form: {
  churchName: string;
  startDate: Date;
  endDate: Date;
  prayerReasons: string;
}): Promise<PrayerPeriod> {
  const { url, headers, body } = buildApiRequest(
    'createPeriod',
    form
  );
  const res = await fetch(url, {
    method: "POST",
    headers,
    body,
  });
  if (!res.ok) throw new Error("Erro ao criar período de oração");
  return res.json();
}

// Agendar horário de oração
export async function createPrayerTime(params: {
  periodId: string;
  time: string;
  name: string;
}): Promise<CreatePrayerTimeResponse> {
  const { url, headers, body } = buildApiRequest(
    'createTime',
    { ...params, periodId: formatPeriodId(params.periodId) }
  );
  const res = await fetch(url, {
    method: "POST",
    headers,
    body,
  });
  if (!res.ok) throw new Error("Erro ao agendar horário de oração");
  return res.json();
}
