import axios from "axios"

const getCustomers = async () => {
    try {
     return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`)
    } catch (error) {
        throw error;
    }
}

export { getCustomers }