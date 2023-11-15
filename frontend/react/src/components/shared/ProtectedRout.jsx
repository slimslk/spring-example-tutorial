import { useEffect } from "react"
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const ProtectedRout = ({ children }) => {

    const { isUserAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect( () => {
        if( !isUserAuthenticated() ) {
            navigate("/login")
        }

    })

    return isUserAuthenticated() ? children : "";
}

export default ProtectedRout;