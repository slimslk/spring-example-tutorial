import { 
    Text,
    Wrap,
  } from "@chakra-ui/react"
  import SidebarWithHeader from "./components/shared/SideBar"
  
  function Dashboard() {
    return(
    <SidebarWithHeader>
        <Text fontSize={"6xl"}>Dashboard</Text>
    </SidebarWithHeader>
  )}
  export default Dashboard;
  