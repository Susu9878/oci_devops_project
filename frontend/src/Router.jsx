import { createBrowserRouter } from "react-router";
import Layout from "./components/Layout";
import HomePage from "./components/Homepage";
import Analytics from "./components/Analytics";
import AI_Feature from "./components/AI_feature";

export const Router = createBrowserRouter([
  {
    path: "/",
    Component: Layout,
    children: [
      { index: true, Component: HomePage },
      { path: "starred", Component: Analytics },
      { path: "starred", Component: AI_Feature },
    ],
  },
]);
