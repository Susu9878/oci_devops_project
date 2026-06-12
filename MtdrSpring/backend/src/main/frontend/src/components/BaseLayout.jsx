import { Outlet } from "react-router-dom";
import './styledComponents/baseLayout.css'

function BaseLayout() {
  return(
    <div className="fullContent">
      <Outlet />;
    </div>
  ) 
}

export default BaseLayout;