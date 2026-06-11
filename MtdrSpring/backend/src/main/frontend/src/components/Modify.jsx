import { useState } from "react";
import "./styledComponents/modify.css";

function Modify({ task, onClose, onUpdate }) {
  const [formData, setFormData] = useState({
    taskId: task.taskId,
    taskName: task.taskName || "",
    description: task.description || "",
    priority: task.priority || "MEDIUM",
    status: task.status || "NOT_STARTED",
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submitting:", formData);

    onUpdate(formData);
  };

  return (
    <div className="modify-overlay" onClick={onClose}>
      <div
        className="modify-content"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="modify-title">Modify Task</h2>

        <form onSubmit={handleSubmit} className="modal-form">

          <div className="modifyForm">
            <label className="modifyLabel">Task Name</label>
            <input
              className="modifyInput"
              type="text"
              value={formData.taskName}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  taskName: e.target.value,
                })
              }
            />

            <label className="modifyLabel">Description</label>
            <textarea
              className="txtArea"
              value={formData.description}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  description: e.target.value,
                })
              }
            />

            <label className="modifyLabel">Priority</label>
            <select
              className="optSel"
              value={formData.priority}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  priority: e.target.value,
                })
              }
            >
              <option value="LOWEST">LOWEST</option>
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
              <option value="CRITICAL">CRITICAL</option>
            </select>

            <label className="modifyLabel">Status</label>
            <select
              className="optSel"
              value={formData.status}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  status: e.target.value,
                })
              }
            >
              <option value="NOT_STARTED">NOT_STARTED</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="DONE">DONE</option>
              <option value="NOT_DONE">NOT_DONE</option>
            </select>
          </div>

          <div className="mBtn">
            <button
              type="submit"
              className="sBtn"
            >
              Save Changes
            </button>

            <button
              type="button"
              onClick={onClose}
              className="cBtn"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Modify;