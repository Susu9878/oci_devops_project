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
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-content"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="modal-title">Modify Task</h2>

        <form onSubmit={handleSubmit} className="modal-form">

          <div className="form-group">
            <label className="form-label">Task Name</label>
            <input
              type="text"
              value={formData.taskName}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  taskName: e.target.value,
                })
              }
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              value={formData.description}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  description: e.target.value,
                })
              }
            />
          </div>

          <div className="form-group">
            <label>Priority</label>
            <select
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
          </div>

          <div className="form-group">
            <label>Status</label>
            <select
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

          <div className="form-buttons">
            <button
              type="submit"
              className="form-submit-button"
            >
              Save Changes
            </button>

            <button
              type="button"
              onClick={onClose}
              className="form-cancel-button"
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