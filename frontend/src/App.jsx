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

function App() {
  return (
    <div>
      <NavBar />
      <BrowserRouter>
        <SideBar />
      </BrowserRouter>
    </div>
  );
}

export default App;
