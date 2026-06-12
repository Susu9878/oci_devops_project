import { useState } from "react";
import "./styledComponents/signUp.css";
import { ChevronLeft } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import API_LIST from "../API";

function SignUp() {
  const [username, setUsername] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleSignUp = async (e) => {
    e.preventDefault();

    try {
      console.log({
        username,
        email,
        phoneNumber,
        password,
      });

      const response = await fetch(`${API_LIST}/api/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          email,
          phoneNumber,
          password,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      const data = await response.json();

      console.log(data);

      localStorage.setItem("token", data.token);

      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Registration failed");
    }
  };

  return (
    <div className="colorBg">
      <div className="login">
        <Link to="/">
          <ChevronLeft className="backBack" />
        </Link>
        <h1 className="h1L">Sign Up</h1>
        <form onSubmit={handleSignUp}>
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
          />

          <label>Phone Number</label>
          <input
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            placeholder="Phone number"
          />

          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email address"
          />

          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
          />

          <button type="submit" className="signBtb">
            Create Account
          </button>
        </form>
      </div>
    </div>
  );
}

export default SignUp;
