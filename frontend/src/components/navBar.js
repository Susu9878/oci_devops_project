import userIcon from "../user.png";

function NavBar(){
  return (
    <div>
      <div className="header">
        <div className="header-container">
          <image source={userIcon} className="icon" />
          <h3 className="user-role">
            USER
            <strong>position</strong>
          </h3>
          <div className="kpi-container">
            <div className="sprint-check">
              <h3 className="sprint-text">Actual Sprint</h3>
            </div>
            <div className="task-check">
              <h3 className="task-text">Completed/Pending</h3>
            </div>
          </div>
        </div>
      </div>
      <div className="styledBar" />
      <div className="sidebar" />
    </div>
  );  
}

export default NavBar;