import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import PrayerPeriod from './pages/PrayerPeriod';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/period/:periodId" element={<PrayerPeriod />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;