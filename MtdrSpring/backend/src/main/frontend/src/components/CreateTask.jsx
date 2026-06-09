import { useState } from "react";
import "./styledComponents/createTask.css"


function CreateTask() {
  const [name, setName] = useState("");
  const [desc, setDesc] = useState("");
  const [storyP, setstoryP] = useState("");
  const [expHrs, setExpHrs] = useState("");
  const [levPr, setPriority] = useState("");
  const [status, setStatus] = useState("");
  const [start, setStartDate] = useState("");
  const [user, setUser] = useState("");
  const [sprint,setSprint] = useState("");
  const [isPending, setIsPending] = useState(false);
  
  const handleSubmit = async (e) => {
        e.preventDefault();
    
    const taskLog = {
      taskName: String(name),
      description: String(desc),
      storyPoints: Number(storyP),
      expectedHours: Number(expHrs),
      priority: String(levPr),
      status: String(status),
      startDate: `${start}T00:00:00Z`,
      username: String(user),
      sprint: Number(sprint)
    };

        console.log("Sending:", taskLog);
    
        try {
            setIsPending(true);
            const response = await fetch(
                "http://localhost:8080/todolist/todolist",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(taskLog)
                }
            );
    
            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }
      
            console.log("Task created successfully");
      
            // Clear form after successful registration
            setName("");
            setDesc("");
            setstoryP("");
            setExpHrs("");
            setPriority("");
            setStatus("");
            setStartDate("");
            setUser("");
            setSprint("");

        } catch (error) {
            console.error("Error creating task:", error);
        } finally {
            setIsPending(false);
        }
    };

    const resetForm = () => {
      setName("");
      setDesc("");
      setstoryP("");
      setExpHrs("");
      setPriority("");
      setStatus("");
      setStartDate("");
      setUser("");
      setSprint("");
    };


  return (
      <div className="formContainer">
        <h2>Create Task</h2>
            <form onSubmit={handleSubmit}>
                <p className="labelP">Task name:</p>
                <input
                    type="text"
                    value={name}
                    placeholder="Task name"
                    onChange={(e) => setName(e.target.value)}
                    required
                />

                <p className="labelP">Description:</p>
                <input
                    type="text"
                    value={desc}
                    placeholder="Description of task"
                    onChange={(e) => setDesc(e.target.value)}
                    required
                />

                <p className="labelP">Story points:</p>
                <input
                    type="number"
                    placeholder="Story points"
                    value={storyP}
                    onChange={(e) => setstoryP(e.target.value)}
                    required
                />

                <p className="labelP">Expected hours:</p>
                <input
                    type="number"
                    placeholder="Expected hours"
                    value={expHrs}
                    onChange={(e) => setExpHrs(e.target.value)}
                    required
                />

                <p className="labelP">Priority:</p>
                <select value={levPr} className="optStyle" onChange={(e) => setPriority(e.target.value)} required >
                  <option value="LOWEST">LOWEST</option>
                  <option value="LOW">LOW</option>
                  <option value="MEDIUM">MEDIUM</option>
                  <option value="HIGH">HIGH</option>
                  <option value="CRITICAL">CRITICAL</option>
                </select>

                <p className="labelP">Status:</p>
                <select value={status} required onChange={(e) => setStatus(e.target.value)} className="optStyle">
                  <option value="NOT_STARTED">NOT_STARTED</option>
                  <option value="IN_PROGRESS">IN_PROGRESS</option>
                  <option value="DONE">DONE</option>
                  <option value="NOT_DONE">NOT_DONE</option>
                </select>

                <p className="labelP">Start date:</p>
                <input
                    type="date"
                    value={start}
                    placeholder="Beginning date"
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                />

                <p className="labelP">User assigned:</p>
                <input
                    type="text"
                    value={user}
                    placeholder="Username"
                    onChange={(e) => setUser(e.target.value)}
                    required
                />

                <p className="labelP">Sprint:</p>
                <input
                    type="number"
                    value={sprint}
                    placeholder="Sprint number"
                    onChange={(e) => setSprint(e.target.value)}
                    required
                />
                <div className="buttonContainer">
                  {!isPending && (
                      <button className="taskBtn">
                          Create task
                      </button>
                  )}

                  {isPending && (
                      <button disabled>
                          Creating task...
                      </button>
                  )}

                  <button type="button" className="RtaskBtn" onClick={resetForm}>
                    Reset task
                  </button>
                </div>

              </form>
      </div>
  );
}

export default CreateTask;