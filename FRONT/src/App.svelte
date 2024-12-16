<script>
    import {currentRoute, navigate} from "./stores/router.js";
    import UsersList from "./pages/UsersList.svelte";
    import RegisterForm from "./pages/RegisterForm.svelte";
    import UserDetails from "./pages/UserDetails.svelte";

  const routes = {
      "/":UsersList,
      "/register":RegisterForm
  }

  let RouteComponent = $state(UsersList);
  $effect(()=>{
      switch ($currentRoute){
          case "/":
              RouteComponent = UsersList
              break;
          case "/register":
              RouteComponent = RegisterForm
              break;
          case "/userDetails":
              RouteComponent = UserDetails
              break;
      }
  })


</script>

<main>
    <a href="/register" onclick={(e) =>{e.preventDefault(); navigate("/register") } }>register</a>
    <RouteComponent/>

</main>