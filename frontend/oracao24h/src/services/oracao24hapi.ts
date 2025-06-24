const BASE_URL = import.meta.env.VITE_BASE_URL as string;

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


// Buscar período com horários
export async function fetchPeriod(periodId: string): Promise<PeriodWithTime> {
  const res = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: {
      "spring.cloud.function.definition": "fetchPeriod",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      periodId: formatPeriodId(periodId),
    }),
  });
  if (!res.ok) throw new Error("Erro ao buscar período");
  return res.json();
}

// Criar período de oração
export async function createPrayerPeriod(form: {
  church: string;
  startDate: Date;
  endDate: Date;
  reason: string;
}): Promise<PrayerPeriod> {
  const res = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: {
      "spring.cloud.function.definition": "createPeriod",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(form),
  });
  if (!res.ok) throw new Error("Erro ao criar período de oração");
  return res.json();
}





export async function createPrayerTime(params: {
  periodId: string;
  time: string;
  name: string;
}): Promise<CreatePrayerTimeResponse> {
  const res = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: {
      "spring.cloud.function.definition": "createTime",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      ...params,
      periodId: formatPeriodId(params.periodId), // Garantir que periodId esteja no formato correto
    }),
  });
  if (!res.ok) throw new Error("Erro ao agendar horário de oração");
  return res.json();
}
