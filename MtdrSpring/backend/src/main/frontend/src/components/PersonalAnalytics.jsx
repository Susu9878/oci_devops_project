import React from "react";
import "./styledComponents/analytics.css";
import API_LIST from "../API";
import {
  ListTodo,
  ClipboardCheck,
  ClockFading,
  ClockCheck,
} from "lucide-react";
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

function PersonalAnalytics() {
  const [teamId, setTeamId] = useState(1);
  const [userId, setUserId] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState();
  const [kpi, setKpi] = useState({});
  const [taskGraphKpis, setTaskGraphKpis] = useState([]);
  const [hourGraphKpis, setHourGraphKpis] = useState([]);

  useEffect(() => {
    const fetchKpis = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `${API_LIST}/kpis/user?userId=${userId}&teamId=${teamId}`,
        );
        if (!response.ok) {
          throw new Error("Failed kpi fetch");
        }

        const data = await response.json();

        setKpi(data);
        setError(null);

        // GRAPH 1
        const taskGraphResponse = await fetch(
          `${API_LIST}/kpis/tasks-per-sprint?teamId=${teamId}`,
        );

        if (!taskGraphResponse.ok)
          throw new Error("Failed to fetch graph KPIs");
        const taskData = await taskGraphResponse.json();
        setTaskGraphKpis(taskData);

        // GRAPH 2
        const hourGraphResponse = await fetch(
          `${API_LIST}/kpis/hours-per-sprint?teamId=${teamId}`,
        );

        if (!hourGraphResponse.ok)
          throw new Error("Failed to fetch graph KPIs");
        const hourGraphData = await hourGraphResponse.json();
        setHourGraphKpis(hourGraphData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchKpis();
  }, [teamId, userId]);

  //FIRST GRAPH
  const taskChartData = taskGraphKpis
    .filter((item) => item.userId === userId)
    .map((item) => ({
      sprint: item.sprintName,
      tasks: item.completedTasks,
    }));
  const selectedUserName = kpi.username || "User";
  //SECOND GRAPH
  const hourChartData = hourGraphKpis
    .filter((item) => item.userId === userId)
    .map((item) => ({
      sprint: item.sprintName,
      hours: item.totalHours,
    }));

  return (
    <div className="analytics">
      <div className="page-header">
        <h1>Analytics</h1>
        <span className="project-manager">{" Team Id "}</span>
        <input
          type="number"
          value={teamId}
          onChange={(e) => setTeamId(Number(e.target.value))}
          className="inputStyle"
          placeholder="Team ID"
          min="1"
        />

        <span className="project-manager">{" User Id "}</span>
        <input
          type="number"
          value={userId}
          onChange={(e) => setUserId(Number(e.target.value))}
          className="inputStyle"
          placeholder="User ID"
          min="1"
        />
      </div>

      <div className="stats-section">
        <div className="stats-grid">
          <StatsCard
            colorClass="orange"
            icon={<ClipboardCheck className="stats-card-icon" />}
            value={kpi.totalTasksCompleted ?? 0}
            label="Tasks Completed"
          />

          <StatsCard
            colorClass="mustard"
            icon={<ListTodo className="stats-card-icon" />}
            value={kpi.totalTasksAssigned ?? 0}
            label="Tasks Assigned"
          />

          <StatsCard
            colorClass="yellow"
            icon={<ListTodo className="stats-card-icon" />}
            value={(kpi.avgTasksPerSprint ?? 0).toFixed(2)}
            label="Avg Tasks / Sprint"
          />

          <StatsCard
            colorClass="blue"
            icon={<ClockFading className="stats-card-icon" />}
            value={(kpi.avgHoursPerSprint ?? 0).toFixed(2)}
            label="Avg Hours / Sprint"
          />

          <StatsCard
            colorClass="light-blue"
            icon={<ClockCheck className="stats-card-icon" />}
            value={(kpi.totalHoursWorked ?? 0).toFixed(2)}
            label="Total Hours Worked"
          />
        </div>
      </div>

      <div className="charts-section">
        <div className="charts-grid">
          <div className="sprintGrid">
            <ChartContainer title="Daily Task Completion">
              <ResponsiveContainer width="100%" height={350}>
                <BarChart data={taskChartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="sprint" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="tasks" fill="#8884d8" name={selectedUserName} />
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
          </div>

          <div className="hourGrid">
            <ChartContainer title="Hours Worked by Developer">
              <ResponsiveContainer width="100%" height={350}>
                <BarChart data={hourChartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="sprint" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="hours" fill="#82ca9d" name={selectedUserName} />
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PersonalAnalytics;
