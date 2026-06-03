import { useState } from "react";
import "./styledComponents/hourRegistry.css"

function Hour_Registry(){
    const [hours, setHours] = useState('');
    const [workDay, setworkDay] = useState('');
    const [taskId, setTaskId] = useState('');
    const [isPending, setIsPending] = useState(false);

    
    const handleSubmit = (e) => {
        e.preventDefault();
        const registry = {hours,workDay,taskId}
        setIsPending(true);
        
         fetch("http://localhost:8080/todolist/worklogs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            
            body: JSON.stringify(registry)
            }).then(() => {
                console.log('Hours registered');
                setIsPending(false);
            })
        }

    return(
        <div className="formContainer">
            <h1>Work hours Registry</h1>
            <form onSubmit={handleSubmit}>
                <p className="labelP"> Hours worked: </p>
                <input
                    type= "number"
                    value={hours}
                    onChange={(e) => setHours(e.target.value)}
                />
                <p className="labelP"> Work day: </p>
                <input
                    type= "number"
                    value={workDay}
                    onChange={(e) => setworkDay(e.target.value)}
                />
                <p className="labelP"> Task ID: </p>
                <input
                    type= "number"
                    value={taskId}
                    onChange={(e) => setTaskId(e.target.value)}
                />
                {!isPending && <button className="hourBtn">Register hours</button>}
                {isPending && <button disabled>Registering hours...</button>}
                
            </form>

        </div>
    );
}

export default Hour_Registry;