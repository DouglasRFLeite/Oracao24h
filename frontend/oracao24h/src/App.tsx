import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { fetchPeriod } from './services/oracao24hapi';

function App() {

  const [periodWithTimes, setPeriodWithTimes] = useState();

  useEffect(() => {
    fetchPeriod("PERIOD#1747775100195").then(res => console.log(res));
  }, []);

  return (
    <>

    </>
  )
}

export default App
