import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { fetchPeriod, createPrayerTime, type PeriodWithTime } from '../services/oracao24hapi';

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
    const [scheduledName, setScheduledName] = useState<string | undefined>(undefined);

    useEffect(() => {
        setScheduledName(undefined); // Reset if slot is reused
    }, [start, end, name]);

    const handleSchedule = async () => {
        if (!inputName.trim() || !onSchedule) return;
        setLoading(true);
        await onSchedule(inputName.trim());
        setLoading(false);
        setScheduledName(inputName.trim());
        setInputName('');
    };

    const displayName = name || scheduledName;

    return (
        <div className="flex items-center gap-2 p-3 rounded-lg shadow-2xl bg-red-50 border border-gray-200 mb-2">
            <span className="font-semibold text-[#7f1d1d] flex-shrink-0">
                {start} - {end}
            </span>
            {displayName ? (
                <span className="ml-2 text-red-50 font-bold bg-[#7f1d1d] rounded-xl p-1 px-3 sm:px-5">{displayName}</span>
            ) : (
                <div className="flex gap-2 items-center flex-1 min-w-0">
                    <input
                        type="text"
                        placeholder="Seu nome"
                        value={inputName}
                        onChange={e => setInputName(e.target.value)}
                        disabled={disabled || loading}
                        className="flex-1 min-w-0 px-3 py-1 rounded border text-[#7f1d1d] border-gray-100 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-[#7f1d1d] transition"
                    />
                    <button
                        onClick={handleSchedule}
                        disabled={disabled || loading || !inputName.trim()}
                        className={`px-4 py-1 rounded font-semibold transition 
                            ${disabled || loading || !inputName.trim()
                                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                : 'bg-[#7f1d1d] hover:bg-[#7f1d1d]/80 text-red-50 hover:cursor-pointer'}
                        `}
                    >
                        {loading ? 'Agendando...' : 'Agendar'}
                    </button>
                </div>
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
    const [periodWithTimes, setPeriodWithTimes] = useState<PeriodWithTime | null>(null);
    const [blocking, setBlocking] = useState<{ [key: string]: boolean }>({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (periodId) {
            setLoading(true);
            fetchPeriod(periodId).then(res => {
                setPeriodWithTimes(res);
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

    const timeNameMap: Record<string, string> = {};
    const timeList = periodWithTimes?.times || [];
    for (const t of timeList) {
        timeNameMap[t.timeString] = t.name;
    }

    const slots = generateTimeSlots();

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#7f1d1d]"></div>
                <span className="ml-4 text-lg text-[#7f1d1d]">Carregando horários...</span>
            </div>
        );
    }

    const handleSchedule = async (time: string, name: string) => {
        if (!periodId) return;
        setBlocking(b => ({ ...b, [time]: true }));
        try {
            await createPrayerTime({ periodId, time, name });
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
        <div className="max-w-2xl mx-auto p-6">
            <img src='/logo.png' alt='Logo da Igreja Cristã Maranata' className='w-50 m-auto' />
            <h2 className="text-3xl font-bold mb-6 text-center text-[#7f1d1d]">
                Período de Oração
            </h2>
            <div className="flex-col mb-8 text-center text-[#7f1d1d] text-lg">
                <p>
                    <span className="font-bold">Igreja:</span>{' '}
                    <span className="font-semibold">{periodWithTimes?.period.church}</span>
                </p>
                <p>
                    <span className="font-bold">Motivo:</span>{' '}
                    <span className="font-semibold">{periodWithTimes?.period.reason}</span>
                </p>
                <p>
                    <span className="font-bold">Início:</span>{' '}
                    <span className="font-semibold">
                        {periodWithTimes?.period.startDate
                            ? new Date(periodWithTimes.period.startDate).toLocaleDateString('pt-BR')
                            : ''}
                    </span>
                    {' - '}
                    <span className="font-bold">Fim:</span>{' '}
                    <span className="font-semibold">
                        {periodWithTimes?.period.endDate
                            ? new Date(periodWithTimes.period.endDate).toLocaleDateString('pt-BR')
                            : ''}
                    </span>
                </p>
                <br />
                <p className="font-semibold">Escolha um horário disponível e agende sua participação!</p>
            </div>
            <div className="space-y-2">
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
        </div>
    );
}