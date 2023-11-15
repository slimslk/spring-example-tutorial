import { Alert, AlertDescription, AlertIcon, Box, FormLabel, Input} from '@chakra-ui/react';
import { useField } from 'formik';

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

  export default MyTextInput;