import { useState } from "react";
import "./styledComponents/hourRegistry.css";

function Hour_Registry() {
    const [hours, setHours] = useState("");
    const [workDay, setWorkDay] = useState("");
    const [taskId, setTaskId] = useState("");
    const [isPending, setIsPending] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const registry = {
            workedHours: Number(hours),
            workedDay: `${workDay}T00:00:00Z`,
            taskId: Number(taskId)
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
            setHours("");
            setWorkDay("");
            setTaskId("");
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
                    value={hours}
                    onChange={(e) => setHours(e.target.value)}
                    required
                />

                <p className="labelP">Work day:</p>
                <input
                    type="date"
                    value={workDay}
                    onChange={(e) => setWorkDay(e.target.value)}
                    required
                />

                <p className="labelP">Task ID:</p>
                <input
                    type="number"
                    value={taskId}
                    onChange={(e) => setTaskId(e.target.value)}
                    required
                />

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