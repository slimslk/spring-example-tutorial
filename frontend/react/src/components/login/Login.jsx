'use client'
import {
  Flex,
  Box,
  Stack,
  Button,
  Heading,
  useColorModeValue,
  Text,
  Link
} from '@chakra-ui/react'
import { Formik, Form } from 'formik'
import * as Yup from 'yup'
import MyTextInput from '../shared/MyTextInput'
import { useAuth } from '../context/AuthContext'
import { errorMessage } from '../../services/notification'
import { useNavigate } from 'react-router-dom'

const LoginForm = () => {

    const { login } = useAuth();
    const navigate = useNavigate();

    return (
        <Formik 
            validateOnMount = {true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                    .email("Must be valid email")
                    .required("Email is requered"),
                    password: Yup.string()
                    .min(3, "Password cannot be less than 3 characters")
                    .max(30, "Password cannot be more than 30 characters")
                    .required("Password is requered")
                })
            }
            initialValues={
                {
                username : '',
                password : ''
                }
            }
            onSubmit={ (values, {setSubmitting}) => {
                setSubmitting(true)
                login(values).then(res => {
                    navigate("/dashboard/customers")
                }).catch(err => {
                    errorMessage(err.code, "Username or password are incorrect");
                }).finally(() => {
                    setSubmitting(false);
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={5}>
                    <MyTextInput
                        label="Email"
                        name="username"
                        type="text"
                        placeholder="example@email.com"
                    />
                    <MyTextInput 
                        label="Password"
                        name="password"
                        type="password"
                        placeholder="Type your password"
                    />

                    <Button 
                        type={"submit"}
                        disabled = {isValid || isSubmitting}
                        colorScheme={"blue"}
                    >
                        Sign in
                    </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

const  Login = () => {
  return (
    <Flex
      minH={'100vh'}
      align={'center'}
      justify={'center'}
      bg={useColorModeValue('gray.50', 'gray.800')}>
      <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
        <Stack align={'center'}>
          <Heading fontSize={'4xl'}>Sign in to your account</Heading>
        </Stack>
        <Box
          rounded={'lg'}
          bg={useColorModeValue('white', 'gray.700')}
          boxShadow={'lg'}
          p={8}>
          <Stack spacing={4}>
          <LoginForm />
          </Stack>
          <Stack mt={4} alignContent={"center"} justifyContent={"center"}>
            <Text>
                Don't have an account? <Link color="blue.600" href='/signup'>Create an account</Link>
            </Text>
          </Stack>
        </Box>
      </Stack>
    </Flex>
  )
}

export default Login;