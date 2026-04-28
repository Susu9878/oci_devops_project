import userIcon from "../user.png";
import "./styledComponents/sideBar.css";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import AI_Feature from "./AI_feature";
import { Routes, Route, Link } from "react-router-dom";
import { menu } from 'lucide-react';


function SideBar() {
  //const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  return (
    <>
      <nav className="sidebar">
        <button></button>
        <ul className="side-container">
          <li>
            <Link to="/" className="linkStyle">
              HOME
            </Link>
          </li>
          <li>
            <Link to="/analytics" className="linkStyle">
              ANALYTICS
            </Link>
          </li>
          <li>
            <Link to="/feature" className="linkStyle">
              AI FEATURE
            </Link>
          </li>
        </ul>
      </nav>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/analytics" element={<Analytics />} />
        <Route path="/feature" element={<AI_Feature />} />
      </Routes>
    </>
  );
}

export default SideBar;

/*
 */
