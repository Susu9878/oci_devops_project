import userIcon from "../user.png";
import "./styledComponents/SideBar.css";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import Add from "./managementComponents/Add.jsx";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Delete from "./managementComponents/Delete.jsx";
import Review from "./managementComponents/Review.jsx";
import SprintCreate from "./managementComponents/SprintCreate.jsx";

function SideBar() {
  return (
    <>
      <nav className="sidebar">
        <ul className="side-container">
          <li className="home">
            <Link to="/" className="linkStyle">
              HOME
            </Link>
          </li>

          <li className="dropdown">
            <p>MANAGEMENT</p>
            <div className="dropdown-content">
              <Link to="/add" className="linkStyle">
                Add task
              </Link>
              <Link to="/delete" className="linkStyle">
                Remove task
              </Link>
              <Link to="/review" className="linkStyle">
                Review task
              </Link>
              <Link to="/sprint" className="linkStyle">
                Edit Sprint
              </Link>
            </div>
          </li>

          <li className="analytics">
            <Link to="/analytics" className="linkStyle">
              ANALYTICS
            </Link>
          </li>
        </ul>
      </nav>

      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/analytics" element={<Analytics />} />
        <Route path="/add" element={<Add />} />
        <Route path="/delete" element={<Delete />} />
        <Route path="/review" element={<Review />} />
        <Route path="/sprint" element={<SprintCreate />} />
      </Routes>
    </>
  );
}

export default SideBar;
