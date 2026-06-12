import { useState } from "react";
import "./styledComponents/createTask.css";
import API_LIST from "../API";

function CreateTask() {
  const initialData = {
    task: "",
    desc: "",
    storyP: "",
    expHrs: "",
    levPr: "",
    status: "",
    start: "",
    user: "",
    sprint: "",
  };

  const [dataLog, setDataLog] = useState(initialData);
  const [errors, setErrors] = useState({});
  const [isPending, setIsPending] = useState(false);

  const validateData = () => {
    let dataErrors = {};

    if (!dataLog.task.trim() || dataLog.task.length < 3) {
      dataErrors.task = "Task name is required";
    }
    if (!dataLog.desc.trim()) {
      dataErrors.desc = "Description is required";
    }
    if (!dataLog.storyP || dataLog.storyP <= 0) {
      dataErrors.storyP = "Story points are required";
    }
    if (!dataLog.expHrs || Number(dataLog.expHrs) <= 0) {
      dataErrors.expHrs = "Expected hours are required";
    }
    if (!dataLog.levPr) {
      dataErrors.levPr = "Priority is required";
    }
    if (!dataLog.status) {
      dataErrors.status = "Status is required";
    }
    if (!dataLog.user || dataLog.user <= 0) {
      dataErrors.user = "UserId is required";
    }
    if (!dataLog.sprint || dataLog.sprint <= 0) {
      dataErrors.sprint = "Sprint is required";
    }

    setErrors(dataErrors);
    return Object.keys(dataErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setDataLog({
      ...dataLog,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const isValid = validateData();

    if (!isValid) {
      console.log("Data Validation Failed");
      return;
    }

    console.log("Data Submitted");

    const taskLog = {
      taskName: String(dataLog.task),
      description: String(dataLog.desc),
      storyPoints: Number(dataLog.storyP),
      expectedHours: Number(dataLog.expHrs),
      priority: String(dataLog.levPr),
      status: String(dataLog.status),
      userId: Number(dataLog.user),
      sprintId: Number(dataLog.sprint),
    };

    console.log("Sending:", taskLog);

    try {
      setIsPending(true);
      const response = await fetch(`${API_LIST}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(taskLog),
      });

      if (!response.ok) {
        throw new Error(`HTTP Error: ${response.status}`);
      }

      console.log("Task created successfully");
      setDataLog(initialData);
      setErrors({});

    } catch (error) {
      console.error("Error creating task:", error);
    } finally {
      setIsPending(false);
    }
  };

  const resetForm = () => {
    setDataLog(initialData);
    setErrors({});
  };

  return (
    <div className="formContainer">
      <h2>Create Task</h2>
      <form onSubmit={handleSubmit}>
        <p className="labelP">Task name:</p>
        <input
          type="text"
          name="task"
          value={dataLog.task}
          placeholder="Task name"
          onChange={handleChange}
          
        />
        {errors.task && <div className="errorMsg">{errors.task}</div>}

        <p className="labelP">Description:</p>
        <input
          type="text"
          name="desc"
          value={dataLog.desc}
          placeholder="Description of task"
          onChange={handleChange}
          
        />
        {errors.desc && <div className="errorMsg">{errors.desc}</div>}

        <p className="labelP">Story points:</p>
        <input
          type="number"
          name="storyP"
          placeholder="Story points"
          value={dataLog.storyP}
          onChange={handleChange}
          
        />
        {errors.storyP && <div className="errorMsg">{errors.storyP}</div>}

        <p className="labelP">Expected hours:</p>
        <input
          type="number"
          name="expHrs"
          placeholder="Expected hours"
          value={dataLog.expHrs}
          onChange={handleChange}
          
        />
        {errors.expHrs && <div className="errorMsg">{errors.expHrs}</div>}

        <p className="labelP">Priority:</p>
        <select
          value={dataLog.levPr}
          name="levPr"
          onChange={handleChange}
          className="optStyle"
        >
          <option value="">Priority</option>
          <option value="LOWEST">LOWEST</option>
          <option value="LOW">LOW</option>
          <option value="MEDIUM">MEDIUM</option>
          <option value="HIGH">HIGH</option>
          <option value="CRITICAL">CRITICAL</option>
        </select>
        {errors.levPr && <div className="errorMsg">{errors.levPr}</div>}

        <p className="labelP">Status:</p>
        <select
          value={dataLog.status}
          name="status"
          onChange={handleChange}
          className="optStyle"
        >
          <option value="">Status</option>
          <option value="NOT_STARTED">NOT_STARTED</option>
          <option value="IN_PROGRESS">IN_PROGRESS</option>
          <option value="DONE">DONE</option>
          <option value="NOT_DONE">NOT_DONE</option>
        </select>
        {errors.status && <div className="errorMsg">{errors.status}</div>}

        <p className="labelP">UserId assigned:</p>
        <input
          type="number"
          name="user"
          value={dataLog.user}
          placeholder="UserId"
          onChange={handleChange}
        />
        {errors.user && <div className="errorMsg">{errors.user}</div>}

        <p className="labelP">Sprint:</p>
        <input
          type="number"
          name="sprint"
          value={dataLog.sprint}
          placeholder="Sprint number"
          onChange={handleChange}
        />
        {errors.sprint && <div className="errorMsg">{errors.sprint}</div>}

        <div className="buttonContainer">
          {!isPending && <button className="taskBtn">Create task</button>}

          {isPending && <button disabled>Creating task...</button>}

          <button type="button" className="RtaskBtn" onClick={resetForm}>
            Reset task
          </button>
        </div>
      </form>
    </div>
  );
}

export default CreateTask;
