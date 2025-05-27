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
  prayerPeriod: PrayerPeriod;
  prayerTimeList: PrayerTime[];
}

// Buscar período com horários
export async function fetchPeriod(periodId: string): Promise<PeriodWithTime> {
  const res = await fetch(`${BASE_URL}`, {
    method: "POST",
    headers: { "spring.cloud.function.definition": "fetchPeriod" },
    body: JSON.stringify(periodId),
  });
  if (!res.ok) throw new Error("Erro ao buscar período");
  return res.json();
}

// Criar horário de oração
export async function createPrayerTime(params: {
  periodId: string;
  name: string;
  timeString: string;
}): Promise<PrayerTime> {
  const res = await fetch(`${BASE_URL}/prayer-time`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(params),
  });

  if (!res.ok) throw new Error("Erro ao criar horário de oração");
  return res.json();
}
