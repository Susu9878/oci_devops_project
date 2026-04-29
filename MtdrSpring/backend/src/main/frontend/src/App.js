/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * This is the application main React component. We're using "function"
 * components in this application. No "class" components should be used for
 * consistency.
 * @author  jean.de.lavarene@oracle.com
 */
import React, { useState, useEffect } from "react";
import NewItem from "./NewItem";
import API_LIST from "./API";
import DeleteIcon from "@mui/icons-material/Delete";
import { Button, TableBody, CircularProgress } from "@mui/material";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Moment from "react-moment";
import NavBar from "./components/NavBar";
import SideBar from "./components/SideBar";
import { useState } from "react";
import API_LIST from "./API"; // '/todolist'

function App() {
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
        `${API_LIST}/kpis/team?sprintId=${sprintId}&teamId=${teamId}`
      );

      if (!teamResponse.ok) throw new Error("Failed to fetch team KPIs");

      const teamData = await teamResponse.json();
      setTeamKpis(teamData);

      // USER KPIs
      const userResponse = await fetch(
        `${API_LIST}/kpis/users?sprintId=${sprintId}&teamId=${teamId}`
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

    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      <h1>KPI Dashboard</h1>

      {/* INPUTS */}
      <div style={{ marginBottom: "20px" }}>
        <label>
          Sprint ID:
          <input
            type="number"
            value={sprintId}
            onChange={(e) => setSprintId(e.target.value)}
            style={{ marginLeft: "10px", marginRight: "20px" }}
          />
        </label>

        <label>
          Team ID:
          <input
            type="number"
            value={teamId}
            onChange={(e) => setTeamId(e.target.value)}
            style={{ marginLeft: "10px", marginRight: "20px" }}
          />
        </label>

        <button onClick={fetchKpis}>Load KPIs</button>
      </div>

      {/* LOADING / ERROR */}
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* TEAM KPIs */}
      {teamKpis && (
        <div style={{ marginBottom: "30px" }}>
          <h2>Team KPIs</h2>
          <ul>
            <li>Avg Tasks Per User: {teamKpis.avgTasksPerUser}</li>
            <li>Avg Hours Per User: {teamKpis.avgHoursPerUser}</li>
            <li>Total Tasks Assigned: {teamKpis.totalTasksAssigned}</li>
            <li>Total Tasks Completed: {teamKpis.totalTasksCompleted}</li>
            <li>Total Hours Worked: {teamKpis.totalHoursWorked}</li>
          </ul>
        </div>
      )}

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
    </div>
  );
}

export default App;

/*
      <NavBar />
      <BrowserRouter>
        <SideBar />
      </BrowserRouter>


*/