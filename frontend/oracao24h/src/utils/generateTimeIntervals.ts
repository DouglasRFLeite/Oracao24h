export function generateTimeIntervals(): string[] {
  const intervals: string[] = [];

  for (let hour = 0; hour < 24; hour++) {
    for (let minute = 0; minute < 60; minute += 15) {
      const start = `${String(hour).padStart(2, "0")}:${String(minute).padStart(
        2,
        "0"
      )}`;
      const endMinute = (minute + 15) % 60;
      const endHour = hour + Math.floor((minute + 15) / 60);
      const end = `${String(endHour).padStart(2, "0")}:${String(
        endMinute
      ).padStart(2, "0")}`;
      intervals.push(`${start} - ${end}`);
    }
  }

  return intervals;
}
