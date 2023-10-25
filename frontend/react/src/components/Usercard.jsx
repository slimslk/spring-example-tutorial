'use client'

import {
  Heading,
  Avatar,
  Box,
  Center,
  Image,
  Flex,
  Text,
  Stack,
  useColorModeValue,
  Tag,
  Button,
  AlertDialog,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogOverlay,
  useDisclosure
} from '@chakra-ui/react'

import { DeleteIcon } from '@chakra-ui/icons'
import { deleteCustomer } from '../services/client';
import { errorMessage, successMessage } from '../services/notification';

import { useRef } from 'react'
import UpdateCustomerDrawer from './UpdateCustomerDrawer';

export default function UserCard({id, name, email, age, gender, updateCustomers}) {
  
  const userGender = gender === 'MALE' ? 'men' : 'women';

  const { isOpen, onOpen, onClose } = useDisclosure()
  const cancelRef = useRef()

  const deleteUser = (userId) => {
    deleteCustomer(userId)
    .then( res => {
      successMessage('Customer deleted', `${name} was successfully deleted `)
      updateCustomers();
    }
    )
    .catch( err => {
      errorMessage(err.code, err.response.data.message)
    }).finally ( () => {
      onClose();
    })
  }

  return (
    <Center py={6}>
      <Box
        minW={'300px'}
        maxW={'400px'}
        w={'full'}
        bg={useColorModeValue('white', 'gray.800')}
        boxShadow={'2xl'}
        rounded={'md'}
        overflow={'hidden'}>
        <Image
          h={'120px'}
          w={'full'}
          src={
            'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
          }
          objectFit="cover"
          alt="#"
        />
        <Flex justify={'center'} mt={-12}>
          <Avatar
            size={'xl'}
            src={
              `https://randomuser.me/api/portraits/${userGender}/${id}.jpg`
            }
            css={{
              border: '2px solid white',
            }}
          />
        </Flex>

        <Box p={6}>
          <Stack spacing={3} align={'left'} mb={5}>
            <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
              {name}
            </Heading>
            <Text color={'gray.500'}>
              <Tag>Email:</Tag> {email}
            </Text>
            <Text color={'gray.500'}>
              <Tag>Age:</Tag> {age} | {gender}
            </Text>
            
           <UpdateCustomerDrawer
              customerId = {id}
              initialValues = {{name, email, age, gender}}
              updateCustomers={updateCustomers}
            />

            <Button onClick={onOpen}
            colorScheme={'red'}
            leftIcon={<DeleteIcon/>}
            >
              Delete customer
            </Button>

            <AlertDialog
              isOpen={isOpen}
              leastDestructiveRef={cancelRef}
              onClose={onClose}
            >
              <AlertDialogOverlay>
                <AlertDialogContent>
                  <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                    Delete Customer
                  </AlertDialogHeader>

                  <AlertDialogBody>
                    Are you sure whant to delete <b>{name}</b>? You can't undo this action afterwards.
                  </AlertDialogBody>

                  <AlertDialogFooter>
                    <Button ref={cancelRef} onClick={onClose}>
                      Cancel
                    </Button>
                    <Button colorScheme='red' onClick={() => {
                      deleteUser(id)
                    }} ml={3}>
                      Delete
                    </Button>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialogOverlay>
            </AlertDialog>
          </Stack>
        </Box>
      </Box>
    </Center>
  )
}