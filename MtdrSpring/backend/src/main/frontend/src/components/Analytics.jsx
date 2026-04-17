import './styledComponents/analytics.css'


function Analytics() {
    return(
        <div className='analytics-bg'>
            <h1>Analytics</h1>
            <h3># sprint</h3>
            <div>
                <ul className='kpi-container'>
                    <li className='kpi'><p>Promedio Tasks / dev</p></li>
                    <li className='kpi'><p>Promedio de horas / dev</p></li>
                    <li className='kpi'><p>Task Completed</p></li>
                    <li className='kpi'><p>Horas Trabajadas totales</p></li>
                </ul>
            </div>
        </div>
    );
}

export default Analytics;