import './styledComponents/analytics.css'
import tasksDone from './assets/tasks.png'
import timeDone from './assets/time.png'

function Analytics() {
    return(
        <div className='analytics-bg'>
            <h1>Analytics</h1>
            <h3>#1 sprint</h3>
            <div>
                <ul className='kpi-container'>
                    <li className='kpi'><p> 5 TASKS ASSIGNED IN AVERAGE / DEV </p></li>
                    <li className='kpi'><p> 6 HOURS WORKED IN AVERAGE / DEV </p></li>
                    <li className='kpi'><p>10 COMPLETED TASKS</p></li>
                    <li className='kpi'><p>24.25 HOURS WORKED</p></li>
                </ul>
            </div>
            <div className='graph-container'>
                <img src={tasksDone} className='graph' alt="taskGraph" />
                <img src={timeDone} className='graph' alt="timeGraph" />
            </div>
        </div>
    );
}

export default Analytics;