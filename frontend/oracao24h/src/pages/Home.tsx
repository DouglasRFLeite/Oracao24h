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
    const navigate = useNavigate();

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setLoading(true);
        try {
            const data = await createPrayerPeriod({
                church: form.churchName,
                reason: form.prayerReason,
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
            // handle error as needed
        }
    }


    return (
        <form onSubmit={handleSubmit}>
            <input name="churchName" placeholder="Church Name" value={form.churchName} onChange={handleChange} required />
            <input name="prayerReason" placeholder="Prayer Reason" value={form.prayerReason} onChange={handleChange} required />
            <input name="startDate" type="date" value={form.startDate} onChange={handleChange} required />
            <input name="endDate" type="date" value={form.endDate} onChange={handleChange} required />
            <button type="submit" disabled={loading}>{loading ? 'Creating...' : 'Create Prayer Period'}</button>
        </form>
    );
}