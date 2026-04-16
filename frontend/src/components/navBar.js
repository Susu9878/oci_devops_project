import userIcon from "../user.png";
import './styledComponents/navBar.css'

function NavBar() {
  return (
    <nav>
      <div className="header">
        <div className="header-container">
          <image source={userIcon} className="icon" />
          <h3 className="user-role">
            USER
            <strong>position</strong>
          </h3>
        </div>
      </div>
    </nav>
  );
}
export default NavBar;
