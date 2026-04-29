import { createBrowserRouter } from "react-router-dom";
import SideBar from "./SideBar";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import AI_Feature from "./AI_feature";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <SideBar />,
    children: [
      { index: true, element: <Homepage /> },
      { path: "analytics", element: <Analytics /> },
      { path: "feature", element: <AI_Feature /> },
    ],
  },
]);