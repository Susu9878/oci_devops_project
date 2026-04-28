import { useState } from "react";
import "./styledComponents/createTask.css"


export default function CreateTask({ onClose, onCreate }) {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    dateAdded: new Date().toISOString().split("T")[0],
    dateDue: "",
    tags: "",
    importance: "medium",
    starred: false,
  });

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.name || !formData.dateDue) return;

    onCreate({
      name: formData.name,
      description: formData.description,
      dateAdded: formData.dateAdded,
      dateDue: formData.dateDue,
      tags: formData.tags
        .split(",")
        .map((t) => t.trim())
        .filter(Boolean),
      importance: formData.importance,
      starred: formData.starred,
    });
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-content"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="modal-title">Create New Task</h2>

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-group">
            <label className="form-label">Task Name *</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea
              value={formData.description}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  description: e.target.value,
                })
              }
              className="form-textarea"
            />
          </div>

          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">Date Added</label>
              <input
                type="date"
                value={formData.dateAdded}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    dateAdded: e.target.value,
                  })
                }
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">Due Date *</label>
              <input
                type="date"
                value={formData.dateDue}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    dateDue: e.target.value,
                  })
                }
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">
              Tags (comma-separated)
            </label>
            <input
              type="text"
              value={formData.tags}
              onChange={(e) =>
                setFormData({ ...formData, tags: e.target.value })
              }
              placeholder="e.g., design, frontend, urgent"
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Importance</label>
            <select
              value={formData.importance}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  importance: e.target.value,
                })
              }
              className="form-select"
            >
              <option value="low">Low</option>
              <option value="medium">Medium</option>
              <option value="high">High</option>
            </select>
          </div>

          <div className="form-checkbox-group">
            <input
              type="checkbox"
              id="starred"
              checked={formData.starred}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  starred: e.target.checked,
                })
              }
              className="form-checkbox"
            />
            <label
              htmlFor="starred"
              className="form-checkbox-label"
            >
              Add to starred tasks
            </label>
          </div>

          <div className="form-buttons">
            <button type="submit" className="form-submit-button">
              Create Task
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