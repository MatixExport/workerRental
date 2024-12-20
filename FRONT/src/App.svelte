<script>
    import UsersList from "./pages/UsersList.svelte";
    import RegisterForm from "./pages/RegisterForm.svelte";
    import UserDetails from "./pages/UserDetails.svelte";
    import CreateRent from "./pages/CreateRent.svelte";
    import {navigate, getCurrentRoute} from "./stores/router.svelte.js";

  const routes = {
      "/":UsersList,
      "/register":RegisterForm,
      "/createRent":CreateRent,
      "/userDetails":UserDetails
  }

  let RouteComponent = $state();
  $effect(()=>{
      RouteComponent = routes[getCurrentRoute()];
  })


</script>

<main>
    <a href="/register" onclick={(e) =>{e.preventDefault(); navigate("/register") }} class="border p-2 m-2 hover:bg-gray-400">register</a>
    <a href="/" onclick={(e) =>{e.preventDefault(); navigate("/") } } class="border p-2 m-2 hover:bg-gray-400">home</a>
    <a href="/createRent" onclick={(e) =>{e.preventDefault(); navigate("/createRent") } } class="border p-2 m-2 hover:bg-gray-400">createRent</a>

    <RouteComponent/>

</main>