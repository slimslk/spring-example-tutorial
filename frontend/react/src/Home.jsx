import { Box, Button } from "@chakra-ui/react"
import { useNavigate } from "react-router-dom";

function Home() {

    const navigate = useNavigate();

    return (
        <Box>
            <Button colorScheme='blue' onClick={() => {
                navigate('/login')
            }}>Login</Button>
            <Button colorScheme='blue' onClick={() => {
                navigate('/signup')
            }}>Signup</Button>
        </Box>
    )
}

export default Home;