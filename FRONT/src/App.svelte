<script>
    import UsersList from "./pages/UsersList.svelte";
    import RegisterForm from "./pages/RegisterForm.svelte";
    import UserDetails from "./pages/UserDetails.svelte";
    import CreateRent from "./pages/CreateRent.svelte";
    import {navigate, getCurrentRoute} from "./stores/router.svelte.js";
    import UpdateUserForm from "./pages/UpdateUserForm.svelte";
    import RentList from "./pages/RentList.svelte";
    import NotFound from "./pages/NotFound.svelte";

  const routes = [
      { pattern: /^\/$/, component:UsersList },
      { pattern: /^\/register$/ , component: RegisterForm },
      { pattern: /^\/createRent$/ , component: CreateRent},
      { pattern: /^\/userDetails@[0-9a-z-]{36}$/ , component: UserDetails},
      { pattern: /^\/updateUser@[0-9a-z-]{36}$/ , component: UpdateUserForm},
      { pattern: /^\/rentList$/ , component: RentList},

  ]


  let RouteComponent = $state();
  $effect(()=>{
      RouteComponent = routes[getCurrentRoute()];
      const match = routes.find(route => route.pattern.test(getCurrentRoute()));
      RouteComponent = match ? match.component : NotFound;
  })


</script>

<nav class="bg-gray-100 border-b border-gray-300 shadow-sm">
    <ul class="flex space-x-4 p-4">
        <li>
            <a href="/register" onclick={(e) =>{e.preventDefault(); navigate("/register") }} class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Register</a>
        </li>
        <li>
            <a href="/" onclick={(e) =>{e.preventDefault(); navigate("/") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Users</a>
        </li>
        <li>
            <a href="/createRent" onclick={(e) =>{e.preventDefault(); navigate("/createRent") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Create rent</a>
        </li>
        <li>
            <a href="/rentList" onclick={(e) =>{e.preventDefault(); navigate("/rentList") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Rent list</a>
        </li>



    </ul>
</nav>
<main>
    <RouteComponent/>

</main>