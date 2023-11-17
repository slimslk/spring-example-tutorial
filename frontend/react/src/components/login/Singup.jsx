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

    const { singup } = useAuth();
    const navigate = useNavigate();

    return (
        <Formik 
            validateOnMount = {true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                    .email("Must be valid email")
                    .required("Email is required"),
                    password: Yup.string()
                    .min(3, "Password cannot be less than 3 characters")
                    .max(30, "Password cannot be more than 30 characters")
                    .required("Password is requered"),
                    passwordConfirmation: Yup.string()
                    .required("Confirm password is required")
                    .oneOf([Yup.ref('password'),null],"Password must match")
                })
            }
            initialValues={
                {
                username : '',
                password : '',
                passwordConfirmation: ''
                }
            }
            onSubmit={ (values, {setSubmitting}) => {
                setSubmitting(true)
                const singupValues = {
                    name: "Change your name",
                    email: values.username,
                    age: 999,
                    gender: "MALE",
                    password: values.password,
                }
                console.log(singupValues)
                singup(singupValues).then(res => {
                    navigate("/dashboard/customers")
                    console.log(res);
                }).catch(err => {
                    errorMessage("Bad credentials", "Username or password are incorrect");
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
                    <MyTextInput 
                        label="Confirm password"
                        name="passwordConfirmation"
                        type="password"
                        placeholder="Confirm your password"
                    />

                    <Button 
                        type={"submit"}
                        disabled = {isValid || isSubmitting}
                        colorScheme={"blue"}
                    >
                        Register Now
                    </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

const  Singup = () => {
  return (
    <Flex
      minH={'100vh'}
      align={'center'}
      justify={'center'}
      bg={useColorModeValue('gray.50', 'gray.800')}>
      <Stack spacing={8} mx={'auto'} width={500} minW={100} maxW={'lg'} py={12} px={6}>
        <Stack align={'center'}>
          <Heading fontSize={'4xl'}>Registration</Heading>
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
                Already have an account? <Link color="blue.600" href='/login'>Login now</Link>
            </Text>

          </Stack>
        </Box>
      </Stack>
    </Flex>
  )
}

export default Singup;