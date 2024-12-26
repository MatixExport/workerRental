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
    let isMobileNavbarOpen = $state(false);
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
      <div class="relative flex h-16 items-center justify-between">

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


        <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">

          <button type="button" onclick={(e)=>{isMobileNavbarOpen = !isMobileNavbarOpen}} class="relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white" aria-controls="mobile-menu" aria-expanded="false">
            <span class="absolute -inset-0.5"></span>
            <span class="sr-only">Open main menu</span>

            <svg class="block size-6" class:hidden={isMobileNavbarOpen}  fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" data-slot="icon">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
            </svg>

            <svg class="block size-6" class:hidden={!isMobileNavbarOpen} fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" data-slot="icon">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
            </svg>
          </button>
        </div>


    </div>

    <div class="sm:hidden" class:hidden={!isMobileNavbarOpen} id="mobile-menu">
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