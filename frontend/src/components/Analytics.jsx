import React from "react";
import "./styledComponents/analytics.css";
import tasksDone from "./assets/tasks.png";
import timeDone from "./assets/time.png";
import {ListTodo, ClipboardCheck, ClockFading, ClockCheck} from "lucide-react";

import {LineChart, Line, BarChart, Bar, AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer} from "recharts";

const sprintData = {
  sprintNumber: "Sprint #24",
  projectManager: "Sarah Johnson",
  tasksAssignedPerDev: 8.5,
  hoursWorkedPerDev: 42.3,
  tasksCompleted: 34,
  totalHoursWorked: 253
};

const taskCompletionData = [
  { day: "Mon", completed: 5, inProgress: 3 },
  { day: "Tue", completed: 7, inProgress: 4 },
  { day: "Wed", completed: 6, inProgress: 5 },
  { day: "Thu", completed: 8, inProgress: 2 },
  { day: "Fri", completed: 8, inProgress: 3 }
];

const hoursWorkedData = [
  { dev: "Alex", hours: 45 },
  { dev: "Blake", hours: 38 },
  { dev: "Casey", hours: 42 },
  { dev: "Dana", hours: 40 },
  { dev: "Eli", hours: 44 },
  { dev: "Fran", hours: 44 }
];

const velocityData = [
  { sprint: "S20", velocity: 28 },
  { sprint: "S21", velocity: 32 },
  { sprint: "S22", velocity: 30 },
  { sprint: "S23", velocity: 35 },
  { sprint: "S24", velocity: 34 }
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
  return (
    <div className="main-content">
      <div className="page-header">
        <h1 className="sprint-title">{sprintData.sprintNumber}</h1>
        <span className="project-manager">
          {sprintData.projectManager}
        </span>
      </div>

      <div className="stats-section">
        <div className="stats-grid">
          <StatsCard
            colorClass="orange"
            icon={<ClipboardCheck />}
            value={sprintData.tasksCompleted}
            label="Tasks completed this sprint"
          />

          <StatsCard
            colorClass="yellow"
            icon={<ListTodo />}
            value={sprintData.tasksAssignedPerDev}
            label="Tasks assigned avg /dev"
          />

          <StatsCard
            colorClass="blue"
            icon={<ClockFading/>}
            value={sprintData.hoursWorkedPerDev}
            label="Hours avg /dev"
          />
          <StatsCard
            colorClass="light-blue"
            icon={<ClockCheck />}
            value={sprintData.totalHoursWorked}
            label="Total hours"
          />
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
                  { day: "Day 5", actual: 34, ideal: 34 }
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