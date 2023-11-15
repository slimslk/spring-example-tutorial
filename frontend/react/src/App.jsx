import { 
  Wrap,
  WrapItem,
  Spinner,
  Text 
} from "@chakra-ui/react"
import SidebarWithHeader from "./components/shared/SideBar"
import { getCustomers } from "./services/client"
import { useEffect, useState } from "react"
import UserCard from "./components/customer/Usercard";
import DrawerForm from "./components/customer/CreateCustomerDrawer";
import { errorMessage } from "./services/notification";

function App() {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [err, setError] = useState('');

  const updateCustomers = () => {
    setLoading(true)
    getCustomers().then(res => {
      setCustomers(res.data);
    }).catch(err => {
      setError(err.response.data.message)
      errorMessage(
        err.code,
        err.response.data.message
      )
      console.log(err)
    }).finally(() => {
      setLoading(false);
    })
  }

  useEffect( () => {
      updateCustomers()
  }, [])

  if(loading) {
    return(
      <SidebarWithHeader>
        <Spinner />
      </SidebarWithHeader>
    )
  }

  if(err){
    return(
      <SidebarWithHeader>
        <DrawerForm updateCustomers = { updateCustomers } />
        <Text>Oppps there was an error</Text>
      </SidebarWithHeader>
    )
  }

  if(customers.length <=0){
    return(
      <SidebarWithHeader>
        <DrawerForm updateCustomers = { updateCustomers } />
        <Text>No customers avaiable</Text>
      </SidebarWithHeader>
    )
  }

  return(
  <SidebarWithHeader>
    <DrawerForm updateCustomers = { updateCustomers } />
    <Wrap spacing={75} justify='center'>
      {customers.map((customer, index) => (
        <WrapItem key={index}>
          <UserCard {...customer}
          updateCustomers={updateCustomers} />
        </WrapItem>
      )
      )}
    </Wrap>
  </SidebarWithHeader>
)}
export default App
