import "./styledComponents/sideBar.css";

function sideBar() {
  return (
    <div>
      <head>
        <link
          href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css"
          rel="stylesheet"
        />
      </head>
        <lu>
          <li>
            <p>HOME</p>
          </li>
          <li>
            <div className="dropdown">
              <button id="dropdownBtn">MANAGEMENT
                <i class="bx bx-chevron-down" id="arrow"></i>
              </button>
              <div i="dropdownContent" className="dropdown-content">
                <a href="#">Add task</a>
                <a href="#">Remove task</a>
                <a href="#">Review task</a>
              </div>
            </div>
          </li>
          <li>
            <p>ANALYTICS</p>
          </li>
        </lu>
    </div>
  );
}
export default sideBar;
