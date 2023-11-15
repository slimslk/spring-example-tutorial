import axios, { HttpStatusCode } from "axios"

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

const getCustomers = async () => {
    try {
     return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
                        getAuthConfig())
    } catch (error) {
        throw error;
    }
}

const getCustomerByUsername = async (username) => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/users/${username}`,
        getAuthConfig())
    } catch (error) {
        throw error;
    }
}

const createNewCustomer = async (customer) => {
    try {
        console.log(customer);
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`, customer)
    } catch (error) {
        throw error
    }
}

const deleteCustomer = async (id) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
        getAuthConfig())
    }catch (error) {
        throw error;
    }
}

export const updateCustomer = async (id, customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`, customer,
        getAuthConfig())
    } catch (error) {
        throw error
    }
}

const login = async (usernameAndpassword) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`, usernameAndpassword,
        getAuthConfig)
    } catch (error) {
        throw error
    }
}

export { getCustomers, createNewCustomer, deleteCustomer, login, getCustomerByUsername }