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
  const [sprintId, setSprintId] = useState(1);
  const [teamId, setTeamId] = useState(1);
  const [userId, setUserId] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState();
  const [kpi, setKpi] = useState([]);
  const [userKpis, setUserKpis] = useState([]);
  const [taskGraphKpis, setTaskGraphKpis] = useState([]);
  const [hourGraphKpis, setHourGraphKpis] = useState([]);

  useEffect(() => {
    const fetchKpis = async () => {
      setLoading(true);
      try {
        const response = await fetch( `${API_LIST}/kpis/team?sprintId=${sprintId}&teamId=${teamId}`);
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
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchKpis();
  }, [teamId]);

  //FIRST GRAPH
  const firstData = taskGraphKpis.map((item) => ({
    sprint: item.sprintId,
    name: item.sprintName,
    dev: item.username,
    tasks: item.completedTasks,
  }));

  const groupedFirst = Object.values(
    firstData.reduce((acc, { sprint, name, dev, tasks }) => {
      if (!acc[sprint]) {
        acc[sprint] = { sprint, name };
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
    name: item.sprintName,
    dev: item.username,
    hours: item.totalHours,
  }));
  const groupedSecond = Object.values(
    secondData.reduce((acc, { sprint, name, dev, hours }) => {
      if (!acc[sprint]) {
        acc[sprint] = { sprint, name };
      }

      acc[sprint][dev] = (acc[sprint][dev] || 0) + hours;

      return acc;
    }, {}),
  );

  //userId filter
  useEffect(() => {
    const fetchKpis = async () => {
      setLoading(true);
      try {
        const response = await fetch( `${API_LIST}/kpis/team?sprintId=${sprintId}&teamId=${teamId}`);
        if (!response.ok) {
          throw new Error("Failed userId fetch");
        }

        const data = await response.json();

        setKpi(data);
        setError(null);

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
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchKpis();
  }, [userId]);

  return (
    <div className="analytics">
      <div className="page-header">
        <h1>Analytics</h1>
        <span className="project-manager">
          {" Team Id "}
        </span>
          <input
            type="number"
            value={teamId}
            onChange={(e) => setTeamId(Number(e.target.value))}
            className="inputStyle"
            placeholder="Team ID"
            min="1"
          />

        <span className="project-manager">
          {" User Id "}
        </span>
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
          {kpi && (
            <StatsCard
              colorClass="orange"
              icon={<ClipboardCheck className="stats-card-icon" />}
              value={kpi.totalTasksCompleted}
              label="Tasks completed"
            />
          )}

          {kpi.totalTasksCompleted && (
            <StatsCard
              colorClass="mustard"
              icon={<ListTodo className="stats-card-icon" />}
              value={kpi.totalTasksCompleted.totalTasksAssigned}
              label="Total tasks assigned"
            />
          )}

          {kpi && (
            <StatsCard
              colorClass="yellow"
              icon={<ListTodo className="stats-card-icon" />}
              value={(kpi.avgTasksPerUser ?? 0).toFixed(2)}
              label="Tasks assigned avg /dev"
            />
          )}

          {kpi && (
            <StatsCard
              colorClass="blue"
              icon={<ClockFading className="stats-card-icon" />}
              value={(kpi.avgHoursPerUser ?? 0).toFixed(2)}
              label="Hours avg /dev"
            />
          )}

          {kpi && (
            <StatsCard
              colorClass="light-blue"
              icon={<ClockCheck className="stats-card-icon" />}
              value={(kpi.totalHoursWorked ?? 0).toFixed(2)}
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
          </div>
        </div>
    </div>
  );
}

export default PersonalAnalytics;
