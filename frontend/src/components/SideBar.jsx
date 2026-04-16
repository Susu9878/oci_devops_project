import userIcon from "../user.png";
import "./styledComponents/SideBar.css";

function SideBar() {
  return (
    <div className="sidebar">
      <ul className="side-container">
        <li className="home">HOME</li>

        <li className="dropdown">
          MANAGEMENT
          <div className="dropdown-content">
            <a href="#">Add task</a>
            <a href="#">Remove task</a>
            <a href="#">Review task</a>
          </div>
        </li>

        <li className="analytics">ANALYTICS</li>
      </ul>

    </div>
  );
}

export default SideBar;