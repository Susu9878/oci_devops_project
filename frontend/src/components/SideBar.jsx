import userIcon from "../user.png";
import "./styledComponents/SideBar.css";
import Homepage from "./Homepage";
import Management from "./Management";
import Analytics from "./Analytics";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";

function SideBar() {
  return (
    <>
      <nav className="sidebar">
        <ul className="side-container">
          <li className="home">
            <Link to="/" className="linkStyle">HOME</Link>
          </li>

          <li className="dropdown">
            <Link to="/management" className="linkStyle">MANAGEMENT</Link>
            <div className="dropdown-content">
              <a href="#">Add task</a>
              <a href="#">Remove task</a>
              <a href="#">Review task</a>
              <a href="#">Edit Sprint</a>
            </div>
          </li>

          <li className="analytics">
            <Link to="/analytics" className="linkStyle">ANALYTICS</Link>
          </li>
        </ul>
      </nav>

      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/management" element={<Management />} />
        <Route path="/analytics" element={<Analytics />} />
      </Routes>
    </>
  );
}

export default SideBar;
