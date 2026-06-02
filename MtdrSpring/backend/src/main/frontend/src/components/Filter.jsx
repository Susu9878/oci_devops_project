import "./styledComponents/Filter.css";

export default function Filter({ filters, setFilters, onClose, allTasks }) {
  const allTags = Array.from(
    new Set(allTasks.flatMap((task) => task.tags))
  );

  const toggleTag = (tag) => {
    const newTags = filters.tags.includes(tag)
      ? filters.tags.filter((t) => t !== tag)
      : [...filters.tags, tag];

    setFilters({ ...filters, tags: newTags });
  };

  const clearFilters = () => {
    setFilters({
      dateSort: "none",
      tags: [],
      importance: "all",
    });
  };

  return (
    <>
      <div
        className="filter-dropdown-overlay"
        onClick={onClose}
      />

      <div className="filter-dropdown">
        <div className="filter-dropdown-header">
          <h3 className="filter-dropdown-title">Filters</h3>

          <button
            onClick={clearFilters}
            className="filter-clear-button"
          >
            Clear All
          </button>
        </div>

        <div className="filter-section">
          <label className="filter-label">Sort by Date</label>

          <select
            value={filters.dateSort}
            onChange={(e) =>
              setFilters({
                ...filters,
                dateSort: e.target.value,
              })
            }
            className="filter-select"
          >
            <option value="none">None</option>
            <option value="added">Date Added</option>
            <option value="due">Due Date</option>
          </select>
        </div>

        <div className="filter-section">
          <label className="filter-label">Importance</label>

          <select
            value={filters.importance}
            onChange={(e) =>
              setFilters({
                ...filters,
                importance: e.target.value,
              })
            }
            className="filter-select"
          >
            <option value="all">All</option>
            <option value="high">High</option>
            <option value="medium">Medium</option>
            <option value="low">Low</option>
          </select>
        </div>

        <div className="filter-section">
          <label className="filter-label">Tags</label>

          <div className="filter-tags-container">
            {allTags.map((tag) => (
              <button
                key={tag}
                onClick={() => toggleTag(tag)}
                className={`filter-tag-button ${
                  filters.tags.includes(tag)
                    ? "active"
                    : "inactive"
                }`}
              >
                {tag}
              </button>
            ))}
          </div>
        </div>
      </div>
    </>
  );
}