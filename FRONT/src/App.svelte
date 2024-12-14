<script>
    import {currentRoute, navigate} from "./stores/router.js";
    import UsersList from "./lib/UsersList.svelte";
  import RegisterForm from "./lib/RegisterForm.svelte";

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
      }
  })


</script>

<main>
    <a href="/register" onclick={(e) =>{e.preventDefault(); navigate("/register") } }>register</a>
    <RouteComponent/>

</main>