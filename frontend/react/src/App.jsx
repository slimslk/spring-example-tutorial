import { 
  Wrap,
  WrapItem,
  Spinner,
  Text 
} from "@chakra-ui/react"
import SidebarWithHeader from "./components/shared/SideBar"
import { getCustomers } from "./services/client"
import { useEffect, useState } from "react"
import UserCard from "./components/Usercard";

function App() {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect( () => {
      setLoading(true)
      getCustomers().then(res => {
        setCustomers(res.data);
      }).catch(err => {
        console.log(err)
      }).finally(() => {
        setLoading(false);
      })
  }, [])

  if(loading) {
    return(
      <SidebarWithHeader>
        <Spinner />
      </SidebarWithHeader>
    )
  }

  if(customers.length <=0){
    return(
      <SidebarWithHeader>
        <Text>No customers avaiable</Text>
      </SidebarWithHeader>
    )
  }

  return(
  <SidebarWithHeader>
    <Wrap spacing={75}>
      {customers.map((customer, index) => (
        <WrapItem key={index}>
          <UserCard {...customer} />
        </WrapItem>
      )
      )}
    </Wrap>
  </SidebarWithHeader>
)}
export default App
