import React from "react";
import "./styledComponents/analytics.css";
import API_LIST from "../API";
import {ListTodo,ClipboardCheck,ClockFading,ClockCheck,} from "lucide-react";
import { Button, TableBody, CircularProgress } from "@mui/material";
import { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

/*
const sprintData = {
  sprintNumber: "Sprint #24",
  projectManager: "Sarah Johnson",
  tasksAssignedPerDev: 8.5,
  hoursWorkedPerDev: 42.3,
  tasksCompleted: 34,
  totalHoursWorked: 253,
};

*/

const taskCompletionData = [
  { day: "Mon", completed: 5, inProgress: 3 },
  { day: "Tue", completed: 7, inProgress: 4 },
  { day: "Wed", completed: 6, inProgress: 5 },
  { day: "Thu", completed: 8, inProgress: 2 },
  { day: "Fri", completed: 8, inProgress: 3 },
];

const hoursWorkedData = [
  { dev: "Alex", hours: 45 },
  { dev: "Blake", hours: 38 },
  { dev: "Casey", hours: 42 },
  { dev: "Dana", hours: 40 },
  { dev: "Eli", hours: 44 },
  { dev: "Fran", hours: 44 },
];

const velocityData = [
  { sprint: "S20", velocity: 28 },
  { sprint: "S21", velocity: 32 },
  { sprint: "S22", velocity: 30 },
  { sprint: "S23", velocity: 35 },
  { sprint: "S24", velocity: 34 },
];

function StatsCard({ colorClass, icon, value, label }) {
  return (
    <div className={`stats-card ${colorClass}`}>
      <div className="stats-card-border" />
      <div className="stats-card-icon">{icon}</div>
      <div className="stats-card-content">
        <p className="stats-card-value">{value}</p>
        <p className="stats-card-label">{label}</p>
      </div>
    </div>
  );
}

function ChartContainer({ title, children }) {
  return (
    <div className="chart-container">
      <h3 className="chart-title">{title}</h3>
      {children}
    </div>
  );
}

function Analytics() {
  const [sprintId, setSprintId] = useState(1);
  const [teamId, setTeamId] = useState(1);

  const [teamKpis, setTeamKpis] = useState(null);
  const [userKpis, setUserKpis] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchKpis = async () => {
    setLoading(true);
    setError("");

    try {
      // TEAM KPIs
      const teamResponse = await fetch(
        `${API_LIST}/kpis/team?sprintId=${sprintId}&teamId=${teamId}`,
      );

      if (!teamResponse.ok) throw new Error("Failed to fetch team KPIs");

      const teamData = await teamResponse.json();
      setTeamKpis(teamData);

      // USER KPIs
      const userResponse = await fetch(
        `${API_LIST}/kpis/users?sprintId=${sprintId}&teamId=${teamId}`,
      );

      if (!userResponse.ok) throw new Error("Failed to fetch user KPIs");

      const userData = await userResponse.json();
      setUserKpis(userData);
    } catch (err) {
      console.error(err);
      setError("Error fetching KPIs");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="analytics">
      <div className="page-header">
        <h1 className="sprint-title">
          {" "}
          <input
            type="number"
            value={sprintId}
            onChange={(e) => setSprintId(e.target.value)}
            style={{ marginLeft: "10px", marginRight: "20px" }}
          />
        </h1>
        <span className="project-manager">
          {" "}
          <input
            type="number"
            value={teamId}
            onChange={(e) => setTeamId(e.target.value)}
            style={{ marginLeft: "10px", marginRight: "20px" }}
          />
        </span>
        <button onClick={fetchKpis}>Load KPIs</button>
      </div>

      {/* LOADING / ERROR */}
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div className="stats-section">
        <div className="stats-grid">
          {teamKpis && (
          <StatsCard
            colorClass="orange"
            icon={<ClipboardCheck />}
            value={teamKpis.totalTasksCompleted}
            label="Tasks completed this sprint"
          />)}

          {teamKpis && (
          <StatsCard
            colorClass="yellow"
            icon={<ListTodo />}
            value={teamKpis.avgTasksPerUser}
            label="Tasks assigned avg /dev"
          />)}

          {teamKpis && (
          <StatsCard
            colorClass="blue"
            icon={<ClockFading />}
            value={teamKpis.avgHoursPerUser}
            label="Hours avg /dev"
          />)}

          {teamKpis && (
            <StatsCard
            colorClass="light-blue"
            icon={<ClockCheck />}
            value={teamKpis.totalHoursWorked}
            label="Total hours worked"
          />)}
        </div>
      </div>

      <div className="charts-section">
        <div className="charts-grid">
          <ChartContainer title="Daily Task Completion">
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={taskCompletionData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="completed" fill="#3479c7" />
                <Bar dataKey="inProgress" fill="#ffc353" />
              </BarChart>
            </ResponsiveContainer>
          </ChartContainer>

          <ChartContainer title="Hours Worked by Developer">
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={hoursWorkedData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" />
                <YAxis dataKey="dev" type="category" />
                <Tooltip />
                <Bar dataKey="hours" fill="#08d" />
              </BarChart>
            </ResponsiveContainer>
          </ChartContainer>

          <ChartContainer title="Sprint Velocity Trend">
            <ResponsiveContainer width="100%" height={350}>
              <AreaChart data={velocityData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="sprint" />
                <YAxis />
                <Tooltip />
                <Area dataKey="velocity" stroke="#f07431" fill="#ffc353" />
              </AreaChart>
            </ResponsiveContainer>
          </ChartContainer>

          <ChartContainer title="Cumulative Progress">
            <ResponsiveContainer width="100%" height={350}>
              <LineChart
                data={[
                  { day: "Day 1", actual: 4, ideal: 6.8 },
                  { day: "Day 2", actual: 11, ideal: 13.6 },
                  { day: "Day 3", actual: 17, ideal: 20.4 },
                  { day: "Day 4", actual: 25, ideal: 27.2 },
                  { day: "Day 5", actual: 34, ideal: 34 },
                ]}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line dataKey="actual" stroke="#3479c7" />
                <Line dataKey="ideal" stroke="#48b9ff" strokeDasharray="5 5" />
              </LineChart>
            </ResponsiveContainer>
          </ChartContainer>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
