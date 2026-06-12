import { useState } from "react";
import "./styledComponents/login.css";
import { Link, useNavigate } from "react-router-dom";
import API_LIST from "../API";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${API_LIST}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,
          password,
        }),
      });

      if (!response.ok) {
        throw new Error("Login failed");
      }

      const data = await response.json();

      console.log(data);

      localStorage.setItem("token", data.token);

      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Invalid email or password");
    }
  };

  return (
    <div className="colorBg">
      <div className="login">
        <h1 className="h1L">Login</h1>

        <form className="box" onSubmit={handleLogin}>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
          />

          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
          />

          <button type="submit" className="loginBtb">
            Login
          </button>
        </form>
        <div className="linkContainer">
          <Link to="/signUp" className="signLink">
            Don't have an account? Sign Up
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Login;
