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

function Analytics() {
  const [sprintId, setSprintId] = useState(1);
  const [teamId, setTeamId] = useState(1);

  const [userKpis, setUserKpis] = useState([]);
  const [taskGraphKpis, setTaskGraphKpis] = useState([]);
  const [hourGraphKpis, setHourGraphKpis] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchKpis = async () => {
    setLoading(true);
    setError("");

    try {
      // USER KPIs
      const userResponse = await fetch(
        `${API_LIST}/kpis/team?sprintId=${sprintId}&teamId=${teamId}`,
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

  //FIRST GRAPH

  const firstData = taskGraphKpis.map((item) => ({
    sprint: item.sprintId,
    name: item.sprintName,
    dev: item.username,
    tasks: item.completedTasks,
  }));

  const groupedFirst = Object.values(
    firstData.reduce((acc, { sprint, dev, tasks }) => {
      if (!acc[sprint]) {
        acc[sprint] = { sprint };
      }

      acc[sprint][dev] = (acc[sprint][dev] || 0) + tasks;

      return acc;
    }, {}),
  );

  const devs = [...new Set(taskGraphKpis.map((item) => item.username))];
  console.log(devs);
  console.log(groupedFirst);

  //SECOND GRAPH
  const secondData = hourGraphKpis.map((item) => ({
    sprint: item.sprintId,
    dev: item.username,
    hours: item.totalHours,
  }));
  const groupedSecond = Object.values(
    secondData.reduce((acc, { sprint, dev, hours }) => {
      if (!acc[sprint]) {
        acc[sprint] = { sprint };
      }

      acc[sprint][dev] = (acc[sprint][dev] || 0) + hours;

      return acc;
    }, {}),
  );

  return (
    <div className="analytics">
      <div className="page-header">
        <h1>Analytics</h1>
        <span className="project-manager">
          {" Team Id "}
          <input
            type="number"
            value={teamId}
            onChange={(e) => setTeamId(e.target.value)}
            className="inputStyle"
          />
        </span>
        <button onClick={fetchKpis} className="kpiButton">
          Load KPIs
        </button>
      </div>

      {/* LOADING / ERROR */}
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div className="stats-section">
        <div className="stats-grid">
          {userKpis && (
            <StatsCard
              colorClass="orange"
              icon={<ClipboardCheck className="stats-card-icon" />}
              value={userKpis.totalTasksCompleted}
              label="Tasks completed"
            />
          )}

          {userKpis && (
            <StatsCard
              colorClass="mustard"
              icon={<ListTodo className="stats-card-icon" />}
              value={userKpis.totalTasksAssigned}
              label="Total tasks assigned"
            />
          )}

          {userKpis && (
            <StatsCard
              colorClass="yellow"
              icon={<ListTodo className="stats-card-icon" />}
              value={(userKpis.avgTasksPerUser ?? 0).toFixed(2)}
              label="Tasks assigned avg /dev"
            />
          )}

          {userKpis && (
            <StatsCard
              colorClass="blue"
              icon={<ClockFading className="stats-card-icon" />}
              value={(userKpis.avgHoursPerUser ?? 0).toFixed(2)}
              label="Hours avg /dev"
            />
          )}

          {userKpis && (
            <StatsCard
              colorClass="light-blue"
              icon={<ClockCheck className="stats-card-icon" />}
              value={(userKpis.totalHoursWorked ?? 0).toFixed(2)}
              label="Total hours worked"
            />
          )}
        </div>
      </div>

      <div className="charts-section">
        <div className="charts-grid">
          <div className="sprintGrid">
            <ChartContainer title="Daily Task Completion">
              <ResponsiveContainer width="100%" height={350}>
                <BarChart data={groupedFirst}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  {devs.map((dev, index) => (
                    <Bar
                      key={dev}
                      dataKey={dev}
                      className={`bar-color-${index}`}
                    />
                  ))}
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
          </div>

          <div className="hourGrid">
            <ChartContainer title="Hours Worked by Developer">
              <ResponsiveContainer width="100%" height={350}>
                <BarChart data={groupedSecond}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="sprint" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  {devs.map((dev, index) => (
                    <Bar
                      key={dev}
                      dataKey={dev}
                      className={`bar-color-${index}`}
                    />
                  ))}
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
