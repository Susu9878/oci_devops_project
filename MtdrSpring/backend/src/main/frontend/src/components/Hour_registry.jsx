import { useState } from "react";
import "./styledComponents/hourRegistry.css";

function Hour_Registry() {
    const initialHours = {
        hours:"",
        workDay:"",
        taskId:""
    };

    const [hourLog, setHourLog] = useState(initialHours);
    const [errors, setErrors] = useState({});
    const [isPending, setIsPending] = useState(false);

    const validateHours = () => {
        let hourErrors = {};

        if(!hourLog.hours || hourLog.hours <= 0){
            hourErrors.hours = "Hours are required";
        }
        if(!hourLog.workDay){
            hourErrors.workDay = "Work date is required";
        }
        if(!hourLog.taskId || hourLog.taskId <= 0){
            hourErrors.taskId = "TaskId is required";
        }

        setErrors(hourErrors);
        return Object.keys(hourErrors).length === 0;
    }

  const handleChange = (e) => {
    const { name, value } = e.target;

    setHourLog({
      ...hourLog,
      [name]: value,
    });
  };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const isValid = validateHours();

        if (!isValid) {
        console.log("Data Validation Failed");
        return;
        }

        const registry = {
            workedHours: Number(hourLog.hours),
            workedDay: `${hourLog.workDay}T00:00:00Z`,
            taskId: Number(hourLog.taskId)
        };

        console.log("Sending:", registry);

        try {
            setIsPending(true);

            const response = await fetch(
                "http://localhost:8080/todolist/worklogs",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(registry)
                }
            );

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            console.log("Hours registered successfully");

            // Clear form after successful registration
            setHourLog(initialHours);
            setErrors({});

        } catch (error) {
            console.error("Error registering hours:", error);
        } finally {
            setIsPending(false);
        }
    };

    return (
        <div className="formContainer">
            <h2>Work Hours Registry</h2>

            <form onSubmit={handleSubmit}>
                <p className="labelP">Hours worked:</p>
                <input
                    type="number"
                    name="hours"
                    value={hourLog.hours}
                    onChange={handleChange}
                />
                {errors.hours && <div className="errorMsg">{errors.hours}</div>}

                <p className="labelP">Work day:</p>
                <input
                    type="date"
                    name="hours"
                    value={hourLog.workDay}
                    onChange={handleChange}
                />
                {errors.workDay && <div className="errorMsg">{errors.workDay}</div>}

                <p className="labelP">Task ID:</p>
                <input
                    type="number"
                    name="hours"
                    value={hourLog.taskId}
                    onChange={handleChange}
                />
                {errors.taskId && <div className="errorMsg">{errors.taskId}</div>}

                {!isPending && (
                    <button className="hourBtn">
                        Register hours
                    </button>
                )}

                {isPending && (
                    <button disabled>
                        Registering hours...
                    </button>
                )}
            </form>
        </div>
    );
}

export default Hour_Registry;