import React, { useState, useEffect } from "react";
import NewItem from "../NewItem";
import API_LIST from "../API";
import { ListFilter, ChevronRight, ChevronDown, Plus } from "lucide-react";
import CreateTask from "./CreateTask";
import Filter from "./Filter";
import "./styledComponents/homepage.css";

const initialTasks = [
  {
    id: 1,
    name: "Design Homepage Mockup",
    description: "Create wireframes and high-fidelity mockups for the new homepage redesign. Include mobile and desktop versions.",
    dateAdded: "2026-04-20",
    dateDue: "2026-04-30",
    tags: ["design", "ui"],
    importance: "high",
    starred: false,
  },
  {
    id: 2,
    name: "Implement User Authentication",
    description: "Set up OAuth 2.0 authentication flow with Google and GitHub providers.",
    dateAdded: "2026-04-22",
    dateDue: "2026-05-05",
    tags: ["backend", "security"],
    importance: "high",
    starred: true,
  },
  {
    id: 3,
    name: "Write API Documentation",
    description: "Document all REST API endpoints.",
    dateAdded: "2026-04-18",
    dateDue: "2026-04-28",
    tags: ["documentation"],
    importance: "medium",
    starred: false,
  }
];

function Homepage() {
  const [tasks, setTasks] = useState(initialTasks);
  const [expandedTaskId, setExpandedTaskId] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showFilterDropdown, setShowFilterDropdown] = useState(false);

  const [filters, setFilters] = useState({
    dateSort: "none",
    tags: [],
    importance: "all",
  });

  const handleCreateTask = (task) => {
    const newTask = {
      ...task,
      id: Math.max(...tasks.map((t) => t.id), 0) + 1,
    };
    setTasks([newTask, ...tasks]);
    setShowCreateModal(false);
  };

  const toggleTaskExpansion = (taskId) => {
    setExpandedTaskId(expandedTaskId === taskId ? null : taskId);
  };

  const toggleStarred = (taskId) => {
    setTasks(
      tasks.map((task) =>
        task.id === taskId ? { ...task, starred: !task.starred } : task
      )
    );
  };

  const getFilteredTasks = () => {
    let filtered = [...tasks];

    if (filters.tags.length > 0) {
      filtered = filtered.filter((task) =>
        task.tags.some((tag) => filters.tags.includes(tag))
      );
    }

    if (filters.importance !== "all") {
      filtered = filtered.filter(
        (task) => task.importance === filters.importance
      );
    }

    if (filters.dateSort === "added") {
      filtered.sort(
        (a, b) => new Date(b.dateAdded) - new Date(a.dateAdded)
      );
    } else if (filters.dateSort === "due") {
      filtered.sort(
        (a, b) => new Date(a.dateDue) - new Date(b.dateDue)
      );
    }

    return filtered;
  };

  const filteredTasks = getFilteredTasks();

  return (
    <div className="home-container">
      <div className="home-content">

        <div className="header-buttons">
          <div className="filter-button-wrapper">
            <button
              onClick={() => setShowFilterDropdown(!showFilterDropdown)}
              className="filter-button"
            >
              <div className="filter-button-content">
                <ListFilter />
                <span className="filter-button-text">FILTER</span>
              </div>
              <ChevronRight />
            </button>

            {showFilterDropdown && (
              <Filter
                filters={filters}
                setFilters={setFilters}
                onClose={() => setShowFilterDropdown(false)}
                allTasks={tasks}
              />
            )}
          </div>

          <button
            onClick={() => setShowCreateModal(true)}
            className="create-button"
          >
            <span className="create-button-text">CREATE TASK</span>
            <Plus />
          </button>
        </div>

        <div className="tasks-list">
          {filteredTasks.map((task) => (
            <div key={task.id} className="task-item">
              <div className="task-header">
                <div
                  className="task-header-content"
                  onClick={() => toggleTaskExpansion(task.id)}
                >
                  <div>
                    <span className="task-name">{task.name}</span>

                    <div className="task-tags">
                      {task.tags.map((tag) => (
                        <span key={tag}>{tag}</span>
                      ))}
                    </div>
                  </div>

                  <div>
                    <span>
                      {task.dateAdded} - {task.dateDue}
                    </span>
                    <ChevronDown />
                  </div>
                </div>
              </div>

              {expandedTaskId === task.id && (
                <div className="task-expanded">
                  <p>{task.description}</p>
                  <span>{task.importance.toUpperCase()}</span>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {showCreateModal && (
        <CreateTask
          onClose={() => setShowCreateModal(false)}
          onCreate={handleCreateTask}
        />
      )}
    </div>
  );
}

export default Homepage;