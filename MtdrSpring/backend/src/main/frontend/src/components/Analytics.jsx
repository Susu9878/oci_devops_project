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
  const [taskGraphKpis, setTaskGraphKpis] = useState([]);
  const [hourGraphKpis, setHourGraphKpis] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchKpis = async () => {
    setLoading(true);
    setError("");

    try {
      // TEAM KPIs
      const teamResponse = await fetch(
        `${API_LIST}/kpis/team/sprint?sprintId=${sprintId}&teamId=${teamId}`,
      );

      if (!teamResponse.ok) throw new Error("Failed to fetch team KPIs");

      const teamData = await teamResponse.json();
      setTeamKpis(teamData);

      // USER KPIs
      const userResponse = await fetch(
        `${API_LIST}/kpis/users/sprint?sprintId=${sprintId}&teamId=${teamId}`,
      );

      if (!userResponse.ok) throw new Error("Failed to fetch user KPIs");

      const userData = await userResponse.json();
      setUserKpis(userData);

      // GRAPH 1
      const taskGraphResponse = await fetch(
        `${API_LIST}/kpis/tasks-per-sprint?teamId=${teamId}`,
      );

      if (!taskGraphResponse.ok) throw new Error("Failed to fetch graph KPIs");
      const taskData = await taskGraphResponse.json();
      setTaskGraphKpis(taskData);

      // GRAPH 2
      const hourGraphResponse = await fetch(
        `${API_LIST}/kpis/hours-per-sprint?teamId=${teamId}`,
      );

      if (!hourGraphResponse.ok) throw new Error("Failed to fetch graph KPIs");
      const hourGraphData = await hourGraphResponse.json();
      setHourGraphKpis(hourGraphData);

    } catch (err) {
      console.error(err);
      setError("Error fetching KPIs");
    } finally {
      setLoading(false);
    }
  };

  const firstData = taskGraphKpis.map(item => ({
    sprint: item.sprintId,
    dev: item.username,
    tasks: item.completedTasks
  }))

  const secondData = hourGraphKpis.map(item => ({
    sprint: item.sprintId,
    dev: item.username,
    hours: item.totalHours
  }))

  return (
    <div className="analytics">
      <div className="page-header">
        <h1 className="sprint-title">Analytics</h1>
        <input
          type="number"
          value={sprintId}
          onChange={(e) => setSprintId(e.target.value)}
          style={{ marginLeft: "10px", marginRight: "20px" }}
        />
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
            value={teamKpis.totalTasksAssigned}
            label="Total tasks assigned"
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

      {/* USER KPIs */}
      {userKpis.length > 0 && (
        <div>
          <h2>User KPIs</h2>
          <table border="1" cellPadding="10">
            <thead>
              <tr>
                <th>User</th>
                <th>Completed</th>
                <th>In Progress</th>
                <th>Not Started</th>
                <th>Hours Worked</th>
              </tr>
            </thead>
            <tbody>
              {userKpis.map((user) => (
                <tr key={user.userId}>
                  <td>{user.username}</td>
                  <td>{user.tasksCompleted}</td>
                  <td>{user.tasksInProgress}</td>
                  <td>{user.tasksNotStarted}</td>
                  <td>{user.hoursWorked}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <div className="charts-section">
        <div className="charts-grid">
          <ChartContainer title="Daily Task Completion">
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={firstData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="sprint" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="tasks" fill="#3479c7" />
              </BarChart>
            </ResponsiveContainer>
          </ChartContainer>

          <ChartContainer title="Hours Worked by Developer">
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={secondData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="sprint" />
                <YAxis dataKey="dev" type="category" />
                <Tooltip />
                <Bar dataKey="hours" fill="#08d" />
              </BarChart>
            </ResponsiveContainer>
          </ChartContainer>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
