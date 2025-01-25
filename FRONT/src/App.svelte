<script>
    import UsersList from "./pages/UsersList.svelte";
    import RegisterForm from "./pages/RegisterForm.svelte";
    import UserDetails from "./pages/UserDetails.svelte";
    import CreateRent from "./pages/CreateRent.svelte";
    import {navigate, getCurrentRoute} from "./stores/router.svelte.js";
    import UpdateUserForm from "./pages/UpdateUserForm.svelte";
    import RentList from "./pages/RentList.svelte";
    import NotFound from "./pages/NotFound.svelte";
    import Notification from "./components/Notification.svelte";
    import {getNotification} from "./stores/notifier.svelte.js";
    import Login from "./pages/Login.svelte";
    import {getUsername, isAdmin, isClient, isManager, logout} from "./stores/JWT.svelte.js";

    const routes = [
      { pattern: /^\/$/, component:UsersList, isPermitted: ()=>{return isAdmin() || isManager() || isClient()}},
      { pattern: /^\/register$/ , component: RegisterForm, isPermitted: ()=>{return ! (isAdmin() || isManager() ||  isClient())} },
      { pattern: /^\/createRent$/ , component: CreateRent, isPermitted: ()=>{return isAdmin() || isManager() || isClient()}},
      { pattern: /^\/userDetails@[0-9a-z-]{36}$/ , component: UserDetails, isPermitted: ()=>{return isAdmin() || isManager() ||  isClient()}},
      { pattern: /^\/updateUser@[0-9a-z-]{36}$/ , component: UpdateUserForm, isPermitted: ()=>{return isAdmin() || isManager() ||  isClient()}},
      { pattern: /^\/rentList$/ , component: RentList, isPermitted: ()=>{return isAdmin() || isManager()}},
      { pattern: /^\/login$/ , component: Login, isPermitted: ()=>{return ! (isAdmin() || isManager() ||  isClient())}},

    ]

    let RouteComponent = $state();
    $effect(()=>{
      RouteComponent = routes[getCurrentRoute()];
      const match = routes.find(route => route.pattern.test(getCurrentRoute()));
      RouteComponent = match && match.isPermitted() ? match.component : NotFound;
    })

    let notification = $state("")
    $effect(()=>{
        notification = getNotification()
    })


</script>

<nav class="bg-gray-100 border-b border-gray-300 shadow-sm">
    {#if (isAdmin() || isManager() ||  isClient())}
        <h1>Welcome {getUsername()} - {#if isClient()}Client{/if}{#if isManager()}Manager{/if}{#if isAdmin()}Admin{/if}</h1>
    {/if}
    <ul class="flex space-x-4 p-4">
        {#if (! (isAdmin() || isManager() ||  isClient()))}
            <li>
                <a href="/register" onclick={(e) =>{e.preventDefault(); navigate("/register") }} class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Register</a>
            </li>
        {/if}
        {#if (isAdmin()||isManager()||isClient())}
            <li>
                <a href="/" onclick={(e) =>{e.preventDefault(); navigate("/") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Users</a>
            </li>
        {/if}
        {#if (isAdmin()||isManager()||isClient())}
            <li>
                <a href="/createRent" onclick={(e) =>{e.preventDefault(); navigate("/createRent") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Create rent</a>
            </li>
        {/if}
        {#if (isAdmin()||isManager())}
            <li>
                <a href="/rentList" onclick={(e) =>{e.preventDefault(); navigate("/rentList") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Rent list</a>
            </li>
        {/if}
        {#if (! (isAdmin() || isManager() ||  isClient()))}
            <li>
                <a href="/login" onclick={(e) =>{e.preventDefault(); navigate("/login") } } class="px-4 py-2 text-gray-700 font-medium rounded hover:bg-gray-200 hover:text-gray-900">Login</a>
            </li>
        {/if}
        {#if (isAdmin()||isManager()||isClient())}
            <li>
                <a href="/logout" onclick={(e) =>{e.preventDefault(); logout() } }>Logout</a>
            </li>
        {/if}
    </ul>
</nav>
<main>

    <RouteComponent/>
    {#if notification.length>0}
        <Notification message={notification} callback={()=>{notification=''}}/>
    {/if}
</main>