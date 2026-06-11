import React, { useState, useEffect } from "react";
import NewItem from "../NewItem";
import API_LIST from "../API";
import { ListFilter, ChevronRight, ChevronDown, Plus } from "lucide-react";
import { Button, TableBody, CircularProgress } from "@mui/material";
import Filter from "./Filter";
import "./styledComponents/homepage.css";


function Homepage() {
  // const [tasks, setTasks] = useState(initialTasks);
  const [expandedTaskId, setExpandedTaskId] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showFilterDropdown, setShowFilterDropdown] = useState(false);
  const [sprintId, setSprintId] = useState(1); //default sprint

  // isLoading is true while waiting for the backend to return the list
  // of items. We use this state to display a spinning circle:
  const [isLoading, setLoading] = useState(false);
  // Similar to isLoading, isInserting is true while waiting for the backend
  // to insert a new item:
  const [isInserting, setInserting] = useState(false);
  // The list of todo items is stored in this state. It includes the "done"
  // "not-done" items:
  const [items, setItems] = useState([]);
  // In case of an error during the API call:
  const [error, setError] = useState();

  const [filters, setFilters] = useState({
    dateSort: "none",
    tags: [],
    importance: "all",
  });

  const toggleTaskExpansion = (itemsId) => {
    setExpandedTaskId(expandedTaskId === itemsId ? null : itemsId);
  };

  const getFilteredTasks = () => {
    let filtered = [...items];

    if (filters.tags.length > 0) {
      filtered = filtered.filter((items) =>
        items.tags.some((tag) => filters.tags.includes(tag)),
      );
    }

    if (filters.importance !== "all") {
      filtered = filtered.filter(
        (items) => items.importance === filters.importance,
      );
    }

    if (filters.dateSort === "added") {
      filtered.sort((a, b) => new Date(b.dateAdded) - new Date(a.dateAdded));
    } else if (filters.dateSort === "due") {
      filtered.sort((a, b) => new Date(a.dateDue) - new Date(b.dateDue));
    }

    return filtered;
  };

  useEffect(() => {
    const fetchTasks = async () => {
      setLoading(true);
      try {
        const response = await fetch(`${API_LIST}/sprint?sprintId=${sprintId}`);
        if (!response.ok) {
          throw new Error("Failed task fecth");
        }

        const data = await response.json();

        setItems(data);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, [sprintId]);

  const filteredTasks = getFilteredTasks();

  return (
    <div className="home-container">
      <div className="container-temp">
        <div className="header-buttons">
          <input
            type="number"
            value={sprintId}
            onChange={(e) => setSprintId(Number(e.target.value))}
            placeholder="Sprint ID"
            min="1"
            className="inputStyleH"
          />

          <div className="filter-button-wrapper">
            <button
              onClick={() => setShowFilterDropdown(!showFilterDropdown)}
              className="filter-button"
            >
              <div className="filter-button-content">
                <ListFilter />
                <span className="filter-button-text">FILTER</span>
              </div>
            </button>

            {showFilterDropdown && (
              <Filter
                filters={filters}
                setFilters={setFilters}
                onClose={() => setShowFilterDropdown(false)}
                allTasks={items}
              />
            )}
          </div>

        </div>

        <div className="tasks-list">
          {filteredTasks.map((items) => (
            <div key={items.taskId} className="task-item">
              <div className="task-header">
                <div
                  className="task-header-content"
                  onClick={() => toggleTaskExpansion(items.taskId)}
                >
                  <div>
                    <span className="task-name">{items.taskName}</span>

                    <div className="task-tags">
                      {items.tags?.map((tag) => (
                        <span key={tag}>{tag}</span>
                      ))}
                    </div>
                  </div>

                  <div>
                    <span>
                      {items.sprint?.startDate} - {items.sprint?.endDate}
                    </span>
                    <ChevronDown />
                  </div>
                </div>
              </div>

              {expandedTaskId === items.taskId && (
                <div className="task-expanded">
                  <p>{items.description}</p>
                  <span>{items.priority.toUpperCase()}</span>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Homepage;
