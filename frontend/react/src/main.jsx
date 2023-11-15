import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
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


const { ToastContainer } = createStandaloneToast();

const router = createBrowserRouter([
  {
    path: "/",
    element: <h1>Hello all</h1>
  },
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/dashboard",
    element:<ProtectedRout><App /></ProtectedRout> 
  },
  {
    path: "/singup",
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
