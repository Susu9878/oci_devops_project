import userIcon from "../user.png";
import "./styledComponents/navBar.css";

function NavBar() {
  return (
    <nav>
      <div className="header">
        <div className="header-container">
          <img src={userIcon} className="icon" alt="user" />
          <h3 className="user-role">
            USER
            <strong>position</strong>
          </h3>
        </div>
      </div>
      <div className="styledBar"/>
    </nav>
  );
}

export default NavBar;
