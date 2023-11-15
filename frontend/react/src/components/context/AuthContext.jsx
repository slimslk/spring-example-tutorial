import { 
    createContext,
    useContext,
    useEffect,
    useState
} from "react";
import { getCustomerByUsername, login as performLogin } from "../../services/client";
import { createNewCustomer as singupNewCustomer} from "../../services/client";
import { jwtDecode } from "jwt-decode";
import { errorMessage } from "../../services/notification";

const AuthContext = createContext({})

const AuthProvaider = ({ children }) => {

    const [customer, setCustomer] = useState(null);

    useEffect( () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token)
            getCustomerByUsername(token.sub).then( res => {
                console.log(res.data)
                setCustomer(res.data)  
            }).catch( err => {
                errorMessage(err.code, err.response.data.message)
            })   
        }
        }, [])

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                const token = localStorage.setItem('access_token', jwtToken);
                const decodedToken = jwtDecode(jwtToken)
                getCustomerByUsername(decodedToken.sub).then( res => {
                    console.log(res.data)
                    setCustomer(res.data)  
                }).catch( err => {
                    errorMessage(err.code, err.response.data.message)
                })
                resolve(res);
            }).catch( err => {
                reject(err);
            }) 
        })
    }

    const getAuthCustomerByUsername = async () => {
        const token = localStorage.getItem("access_token");

        if(!token) {
            setCustomer(null);
            return;
        }

        const decodedToken = jwtDecode(token);

        return new Promise((resolve, reject) => {
          getCustomerByUsername(decodedToken.sub).then(res => {
            console.log("get customer " + res.data);
              setCustomer({
                  ...res.data.customerDTO
              })
              resolve(res);
          }).catch( err => {
              reject(err);
          }) 
        })  
    }

    const isUserAuthenticated = () => {
        const token = localStorage.getItem("access_token")
        if(!token) {
            return false;
        }
        const decodedToken = jwtDecode(token);

        if(Date.now>decodedToken.exp * 1000) {
            logout();
            return false;
        }
        return true;
    }

    const singup = async (usenameAndPassword) => {
        return new Promise((resolve, reject) => {
            singupNewCustomer(usenameAndPassword).then( res => {
                const jwtToken = res.headers["authorization"];
                localStorage.setItem('access_token', jwtToken);
                resolve(res)   
            }).catch( err => {
                reject(err);
            })
        })
    }

    const logout = () => {
        localStorage.removeItem("access_token")
        setCustomer(null)
    }

    return <AuthContext.Provider value={{
        customer,
        login,
        logout,
        singup,
        isUserAuthenticated,
        getAuthCustomerByUsername
    }}>
        {children}
        </AuthContext.Provider>
}

export const useAuth = () => useContext(AuthContext)

export default AuthProvaider;