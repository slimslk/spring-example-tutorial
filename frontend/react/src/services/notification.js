import { createStandaloneToast } from '@chakra-ui/react'

const { toast } = createStandaloneToast()

const notification = (title, message, status) => {
    toast({
        title: `${title}`,
        description: `${message}`,
        status: `${status}`,
        duration: 3000,
        isClosable: true,
      })
}

const successMessage = (title, message) => {
    notification(
        title,
        message,
        'success',
      )
}

const errorMessage = (title, message) => {
    notification(
        title,
        message,
        'error'
    )
}

export {
    successMessage,
    errorMessage,
}