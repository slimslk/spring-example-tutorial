
import { Alert, AlertDescription, AlertIcon, Box, FormLabel, Input, Select, Stack,
} from '@chakra-ui/react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { errorMessage, successMessage } from '../services/notification';
import { updateCustomer } from "../services/client";

const MyTextInput = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
      <Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Input className="text-input" {...field} {...props} />
        {meta.touched && meta.error ? (
        <Alert className="error" status='error'>
            <AlertIcon/>
            <AlertDescription>{meta.error}</AlertDescription>
        </Alert>
        ) : null}
      </Box>
    );
  };
  
  
  const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
      <Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Select {...field} {...props} />
        {meta.touched && meta.error ? (
        <Alert className="error" status='error'>
            <AlertIcon/>
            <AlertDescription>
                {meta.error}
            </AlertDescription>
        </Alert>
        ) : null}
      </Box>
    );
  };
  
  // And now we can use these
  const UpdateCustomerForm = ( {customerId, initialValues, updateCustomers }) => {

    return (
      <>
        <Formik
          initialValues={{
            name: initialValues.name,
            email: initialValues.email,
            age: initialValues.age,
            gender: initialValues.gender, // added for our select
          }}
          validationSchema={Yup.object({
            name: Yup.string()
              .min(2, 'Must be 2 characters or greater')
              .max(15, 'Must be 15 characters or less')
              .required('Required'),
            email: Yup.string()
              .email('Invalid email address')
              .required('Required'),
            age: Yup.number()
              .min(18, 'Age should be greater than 18')
              .max(300, "Age should be less than 300")
              .required('Requered'),
            gender: Yup.string()
              .oneOf(
                ['MALE', 'FEMALE'],
                'Invalid gender type'
              )
              .required('Required'),
          })}
          onSubmit={( customer ) => {
            updateCustomer(customerId, customer)
            .then(res => {
                successMessage('Customer updated', `Customer was successfully updated`);
                updateCustomers();
            })
            .catch(error => {
                console.log(error)
                errorMessage(error.code, error.response.data.message);
            })
            .finally( () => {

            })
          }}
        >
          <Form id='update-customer-form'>
            <Stack spacing={5}>
                <MyTextInput
                label="Name"
                name="name"
                type="text"
                placeholder="Jane"
                />
    
                <MyTextInput
                label="Email Address"
                name="email"
                type="email"
                placeholder="jane@formik.com"
                />
    
                <MyTextInput
                label="Age"
                name="age"
                type="number"
                placeholder="0"
                />
    
                <MySelect label="Gender" name="gender">
                <option value="">Select gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                </MySelect>
            </Stack>
          </Form>
        </Formik>
      </>
    );
};

export default UpdateCustomerForm;