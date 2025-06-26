import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPrayerPeriod } from '../services/oracao24hapi';

export default function Home() {
    const [form, setForm] = useState({
        churchName: '',
        prayerReason: '',
        startDate: '',
        endDate: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setLoading(true);
        setError(null);
        try {
            const data = await createPrayerPeriod({
                churchName: form.churchName,
                prayerReasons: form.prayerReason,
                startDate: new Date(form.startDate),
                endDate: new Date(form.endDate),
            });
            setLoading(false);
            if (data.periodId) {
                const periodId = data.periodId.replace(/^PERIOD#/, '');
                navigate(`/period/${periodId}`);
            }
        } catch (error) {
            setLoading(false);
            setError('Erro ao criar período. Tente novamente.');
        }
    }

    return (
        <div className="max-w-xl mx-auto p-6">
            <img src='/logo.png' alt='Logo da Igreja Cristã Maranata' className='w-48 m-auto mb-6' />
            <h2 className="text-3xl font-bold mb-6 text-center text-[#7f1d1d]">
                Criar Período de Oração
            </h2>
            <form
                onSubmit={handleSubmit}
                className="bg-red-50 border border-gray-200 rounded-xl shadow-2xl p-6 flex flex-col gap-4"
            >
                <input
                    name="churchName"
                    placeholder="Nome da Igreja"
                    value={form.churchName}
                    onChange={handleChange}
                    required
                    className="px-3 py-2 rounded border text-[#7f1d1d] border-gray-100 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-[#7f1d1d] font-semibold"
                />
                <input
                    name="prayerReason"
                    placeholder="Motivo do Período"
                    value={form.prayerReason}
                    onChange={handleChange}
                    required
                    className="px-3 py-2 rounded border text-[#7f1d1d] border-gray-100 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-[#7f1d1d] font-semibold"
                />
                <div className="flex gap-2 flex-col sm:flex-row">
                    <div className="flex-1 flex flex-col min-w-0">
                        <label className="text-[#7f1d1d] font-bold mb-1" htmlFor="startDate">Início</label>
                        <input
                            name="startDate"
                            id="startDate"
                            type="date"
                            value={form.startDate}
                            onChange={handleChange}
                            required
                            className="w-full px-3 py-2 rounded border text-[#7f1d1d] border-gray-100 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-[#7f1d1d] font-semibold min-w-0"
                        />
                    </div>
                    <div className="flex-1 flex flex-col min-w-0">
                        <label className="text-[#7f1d1d] font-bold mb-1" htmlFor="endDate">Fim</label>
                        <input
                            name="endDate"
                            id="endDate"
                            type="date"
                            value={form.endDate}
                            onChange={handleChange}
                            required
                            className="w-full px-3 py-2 rounded border text-[#7f1d1d] border-gray-100 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-[#7f1d1d] font-semibold min-w-0"
                        />
                    </div>
                </div>
                {error && (
                    <div className="text-red-700 bg-red-100 border border-red-200 rounded p-2 text-center">
                        {error}
                    </div>
                )}
                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full px-4 py-2 rounded font-bold transition
                        ${loading
                            ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                            : 'bg-[#7f1d1d] hover:bg-[#7f1d1d]/80 text-red-50 hover:cursor-pointer'}
                    `}
                >
                    {loading ? 'Criando...' : 'Criar Período'}
                </button>
            </form>
            <div className='mt-10 text-center text-[#7f1d1d] text-sm font-semibold w-100 m-auto'>
                <p>Após criar o período, você será direcionado para a página de agendamento de horários.</p>
                <p>Copie o link da página para compartilhar com os membros da sua igreja.</p>
            </div>
        </div>
    );
}