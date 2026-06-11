import { createBrowserRouter } from "react-router-dom";
import Layout from "./Layout";
import Homepage from "./Homepage";
import Analytics from "./Analytics";
import AI_Feature from "./AI_feature";
import Hour_Registry from "./Hour_registry";
import CreateTask from "./CreateTask";
import Login from "./Login";
import BaseLayout from "./BaseLayout";
import SignUp from "./SignUp";
import PersonalAnalytics from "./PersonalAnalytics";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <Homepage /> },
      { path: "analytics", element: <Analytics /> },
      { path: "personal-analytics", element: <PersonalAnalytics /> },
      { path: "feature", element: <AI_Feature /> },
      { path: "hours", element: <Hour_Registry /> },
      { path: "task", element: <CreateTask /> },
    ],
  },
  {
    path: "/",
    element: <BaseLayout/>,
    children: [
      { path: "login", element: <Login /> },
      { path: "signUp", element: <SignUp /> },
      { path: "home", element: <Homepage /> },
    ],
  },
]);