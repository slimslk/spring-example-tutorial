
import { Button, Center, Flex, Stack } from "@chakra-ui/react"
import { useNavigate } from "react-router-dom";

function Home() {

    const navigate = useNavigate();

    return (        

<Flex width={"100vw"} height={"100vh"} alignContent={"center"} justifyContent={"center"}>
    <Stack spacing={"10"} justifyContent={"center"}>
        <Center>
          <Button colorScheme='blue' onClick={() => {
            navigate('/login')
          }}>Login</Button> 
        </Center>
        <Center>
          <Button colorScheme='blue' onClick={() => {
            navigate('/signup')
          }}>Signup</Button>
        </Center>
    </Stack>
</Flex>
    )
}

export default Home;