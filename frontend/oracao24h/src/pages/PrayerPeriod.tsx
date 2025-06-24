import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { fetchPeriod, createPrayerTime } from '../services/oracao24hapi';

function PrayerSlot({
    start,
    end,
    name,
    onSchedule,
    disabled,
}: {
    start: string;
    end: string;
    name?: string;
    onSchedule?: (name: string) => void;
    disabled?: boolean;
}) {
    const [inputName, setInputName] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSchedule = async () => {
        if (!inputName.trim() || !onSchedule) return;
        setLoading(true);
        await onSchedule(inputName.trim());
        setLoading(false);
        setInputName('');
    };

    return (
        <div style={{ marginBottom: 8 }}>
            <strong>{start}-{end}</strong>
            {name ? (
                <> - <b>{name}</b></>
            ) : (
                <>
                    <input
                        type="text"
                        placeholder="Seu nome"
                        value={inputName}
                        onChange={e => setInputName(e.target.value)}
                        disabled={disabled || loading}
                        style={{ marginLeft: 8 }}
                    />
                    <button
                        onClick={handleSchedule}
                        disabled={disabled || loading || !inputName.trim()}
                        style={{ marginLeft: 4 }}
                    >
                        {loading ? 'Agendando...' : 'Agendar'}
                    </button>
                </>
            )}
        </div>
    );
}

function generateTimeSlots() {
    const slots: { start: string; end: string }[] = [];
    for (let h = 0; h < 24; h++) {
        for (let m = 0; m < 60; m += 15) {
            const startHour = h.toString().padStart(2, '0');
            const startMin = m.toString().padStart(2, '0');
            let endH = h;
            let endM = m + 15;
            if (endM === 60) {
                endM = 0;
                endH = h + 1;
            }
            const endHour = endH.toString().padStart(2, '0');
            const endMin = endM.toString().padStart(2, '0');
            slots.push({
                start: `${startHour}:${startMin}`,
                end: `${endHour}:${endMin}`,
            });
        }
    }
    return slots;
}

export default function PrayerPeriod() {
    const { periodId } = useParams();
    const [periodWithTimes, setPeriodWithTimes] = useState<any>(null);
    const [blocking, setBlocking] = useState<{ [key: string]: boolean }>({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (periodId) {
            setLoading(true);
            fetchPeriod(periodId).then(res => {
                setPeriodWithTimes(res);
                // Block already filled slots using only the start time
                const initialBlocking: { [key: string]: boolean } = {};
                const timeList = res.times;
                for (const t of timeList) {
                    initialBlocking[t.timeString] = true;
                }
                setBlocking(initialBlocking);
                setLoading(false);
            });
        }
    }, [periodId]);

    // Build a lookup for quick access to names by start time
    const timeNameMap: Record<string, string> = {};
    const timeList = periodWithTimes?.times || periodWithTimes?.prayerTimeList || [];
    for (const t of timeList) {
        timeNameMap[t.timeString] = t.name;
    }

    const slots = generateTimeSlots();

    if (loading) {
        return <div>Carregando horários...</div>;
    }

    const handleSchedule = async (time: string, name: string) => {
        if (!periodId) return;
        setBlocking(b => ({ ...b, [time]: true }));
        try {
            await createPrayerTime({ periodId, time, name });
            // Update UI immediately
            setPeriodWithTimes((prev: any) => ({
                ...prev,
                prayerTimeList: [
                    ...(prev?.prayerTimeList || []),
                    { periodId, timeString: time, name },
                ],
            }));
        } catch (e) {
            alert('Erro ao agendar horário');
            setBlocking(b => ({ ...b, [time]: false }));
        }
    };

    return (
        <div>
            <h2>Prayer Period: {periodId}</h2>
            {slots.map(({ start, end }) => (
                <PrayerSlot
                    key={start}
                    start={start}
                    end={end}
                    name={timeNameMap[start]}
                    disabled={!!blocking[start]}
                    onSchedule={
                        timeNameMap[start]
                            ? undefined
                            : (name: string) => handleSchedule(start, name)
                    }
                />
            ))}
        </div>
    );
}