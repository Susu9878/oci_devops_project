import { createBrowserRouter } from "react-router-dom";
import Layout from "./Layout";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import AI_Feature from "./AI_feature";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <Homepage /> },
      { path: "analytics", element: <Analytics /> },
      { path: "feature", element: <AI_Feature /> },
    ],
  },
]);