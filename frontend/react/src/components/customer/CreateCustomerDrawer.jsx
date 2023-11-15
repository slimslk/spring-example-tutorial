import { useDisclosure } from "@chakra-ui/hooks";
import { 
  Button,
  Stack,
  Drawer,
  DrawerBody,
  DrawerCloseButton,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
} from "@chakra-ui/react";

import SignupForm from "./CreatCustomerForm";

const AddIcon = () => '+';

const DrawerForm = ({ updateCustomers }) => {
  const { isOpen, onOpen, onClose } = useDisclosure()

  return (
    <div>
        <Drawer
          size={'md'}
          isOpen={isOpen}
          placement='right'
          onClose={onClose}
        >
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader borderBottomWidth='1px'>
              Create a new customer
            </DrawerHeader>
  
            <DrawerBody>
              <Stack spacing='24px'>
                  <SignupForm
                    updateCustomers = {updateCustomers}
                  />
              </Stack>
            </DrawerBody>
  
            <DrawerFooter borderTopWidth='1px'>
              <Button variant='outline' mr={3} onClick={onClose}>
                Cancel
              </Button>
              <Button 
                colorScheme='blue'
                type="submit"
                form="create-customer-form"
              >
                Submit
              </Button>
            </DrawerFooter>
          </DrawerContent>
        </Drawer>
    </div>

  )
}

export default DrawerForm;
