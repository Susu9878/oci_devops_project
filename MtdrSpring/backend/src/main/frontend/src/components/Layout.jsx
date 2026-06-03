import userIcon from "../user.png";
import "./styledComponents/layout.css";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import AI_Feature from "./AI_feature";
import { Outlet, Link, useLocation, Route, Routes } from "react-router-dom";
import { Menu } from 'lucide-react';

function Layout() {
  const location = useLocation();
  return (
    <>
        <nav>
      <div className="header">
        <div className="header-container">

          <img src={userIcon} className="icon" alt="user" />
          <h3 className="user-role">
            USER
            <strong>position</strong>
          </h3>
          <Link to="/login"><button className="loginButton">Login</button></Link>
          <div>

          </div>
        </div>
      </div>
      <div className="styledBar" />
    </nav>
    <div className="layout-container">
      <div className="sidebar">
        <div className="side-container">
          <Link to="/" className="linkStyle"><p>Home</p></Link>
          <Link to="/analytics" className="linkStyle"><p>Analytics</p></Link>
          <Link to="/feature" className="linkStyle"><p>Feature</p></Link>
          <li className="dropdown">
            <p>
              Management
            </p>
            <div className="dropdown-content">
              <Link to="/task" className="linkStyle"><p>Add task</p></Link>
              <Link to="/hours" className="linkStyle"><p>Register hours</p></Link>
            </div>
          </li>
          
        </div>
      </div>
      
      <div className="main-content">
        <Outlet />
      </div>

    </div>
    </>
  );
}

export default Layout;


/* 
*/