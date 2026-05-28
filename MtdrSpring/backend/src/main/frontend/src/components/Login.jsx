import { useState } from "react";
import "./styledComponents/login.css"
import { Outlet, Link, useLocation, Route, Routes } from "react-router-dom";


function Login(){
    const [user, setUser] = useState('');
    const [password, setPassword] = useState('');

    return(
        <div className="login">
            <h1>Login</h1>
            <form className="box">
                <label>Username:</label>
                <input
                    type= "string"
                    value={user}
                    onChange={(e) => setUser(e.target.value)}
                />
                <label>Password:</label>
                <input
                    type= "string"
                    value={password}
                    onChange={(e) => setUser(e.target.value)}
                />
                <button>Login</button>
            </form>
            <Link to="/signUp"> Don't have an account? Sign Up</Link>

        </div>
    );
}

export default Login;