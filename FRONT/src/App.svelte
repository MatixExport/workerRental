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

    let notification = $state("")
    $effect(()=>{
        notification = getNotification()
    })

    const navbarRoutes = [
        {
            display:"Register",
            route:"/register"
        },
        {
            display:"Users",
            route:"/"
        },
        {
            display:"Create rent",
            route:"/createRent"
        },
        {
            display:"Rent list",
            route:"/rentList"
        }
    ]




</script>
<nav class="bg-gray-800">
    <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
      <div class="relative flex items-center justify-between">
        <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">

        </div>
        <div class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
          <div class="hidden sm:ml-6 sm:block">
            <div class="flex space-x-4">
                {#each navbarRoutes as route}
                    <a href={route['route']}  class="rounded-md px-3 py-2 text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white">
                        {route['display']}
                    </a>
                {/each}         
            </div>
          </div>
        </div>
      </div>
    </div>
  
    <div class="sm:hidden" id="mobile-menu">
      <div class="space-y-1 px-2 pb-3 pt-2">
            {#each navbarRoutes as route}
                <a href={route['route']}  class="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white">
                    {route['display']}
                </a>
            {/each}  
      </div>
    </div>
  </nav>
<main>

    <RouteComponent/>
    {#if notification.length>0}
        <Notification message={notification} callback={()=>{notification=''}}/>
    {/if}
</main>