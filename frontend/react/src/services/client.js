import axios, { HttpStatusCode } from "axios"

const getCustomers = async () => {
    try {
     return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`)
    } catch (error) {
        throw error;
    }
}

const createNewCustomer = async (customer) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`, customer)
    } catch (error) {
        throw error
    }
}

const deleteCustomer = async (id) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`)
    }catch (error) {
        throw error;
    }
}

export const updateCustomer = async (id, customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`, customer)
    } catch (error) {
        throw error
    }
}

export { getCustomers, createNewCustomer, deleteCustomer }