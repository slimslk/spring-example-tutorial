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

import { auto } from "@popperjs/core";
import UpdateCustomerForm from "./UpdateCustomerForm";


const UpdateCustomerDrawer = ({customerId, initialValues, updateCustomers }) => {
  const { isOpen, onOpen, onClose } = useDisclosure()

  return (
    <div>
      <Button colorScheme='blue' onClick={onOpen} mt={9} width={auto}>
          Update customer
      </Button>
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
                  <UpdateCustomerForm
                    customerId = {customerId}
                    initialValues = {initialValues}
                    updateCustomers={updateCustomers}
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
                form="update-customer-form"
              >
                Save
              </Button>
             
            </DrawerFooter>
          </DrawerContent>
        </Drawer>
    </div>
  )
}

export default UpdateCustomerDrawer;
