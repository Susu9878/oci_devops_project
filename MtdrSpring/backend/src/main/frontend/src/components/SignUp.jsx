import "./styledComponents/signUp.css"
import {House} from "lucide-react";
import { Outlet, Link, useLocation, Route, Routes } from "react-router-dom";


function SignUp(){
    return(
        <div className="feature">
            <h1>Sign Up</h1>
            <Link to="/"><House /></Link>
        </div>
    );
}

export default SignUp;