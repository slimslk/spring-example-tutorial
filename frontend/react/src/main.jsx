import React from 'react'
import ReactDOM from 'react-dom/client'
import Customer from './Customer.jsx'
import { ChakraProvider } from '@chakra-ui/react'
import './index.css'
import { createStandaloneToast } from '@chakra-ui/react'
import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import Login from './components/login/Login.jsx'
import AuthProvaider from './components/context/AuthContext.jsx'
import Singup from './components/login/Singup.jsx'
import ProtectedRout from './components/shared/ProtectedRout.jsx'
import Dashboard from './Dashboard.jsx'
import Home from './Home.jsx'


const { ToastContainer } = createStandaloneToast();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />
  },
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/dashboard/customers",
    element:<ProtectedRout><Customer /></ProtectedRout> 
  },
  {
    path: "/dashboard",
    element:<ProtectedRout><Dashboard /></ProtectedRout> 
  },
  {
    path: "/signup",
    element: <Singup />
  }
]);


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ChakraProvider>
      <AuthProvaider>
        <RouterProvider router={router} />
      </AuthProvaider>
      <ToastContainer />
    </ChakraProvider>
  </React.StrictMode>,
)
