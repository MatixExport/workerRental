<script>
    import ActiveUser from "../components/ActiveUser.svelte";
    import InactiveUser from "../components/InactiveUser.svelte";
    import {notify} from "../stores/notifier.svelte.js";
    import {SERVER_URI} from "../config/config"

    let users = $state([])

    let login = $state("");

    function getUsers(){
        const uri = `${SERVER_URI}/users`
        fetch(uri, {method: "GET"}).then(response => {
            if(response.status === 200){
                response.json().then(result => users = result)
            }
            else {
                notify("Backend error")
            }
        })
    }

    function getUsersByLogin(){
        if(login.length===0){
            getUsers()
        }
        const uri = `${SERVER_URI}/users/loginContains/${login}`
        fetch(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result => users = result)
                }
                else {
                    notify("Backend error")
                }
            })
    }
    getUsers();

</script>
<div class="p-4">
    <input
        type="text"
        placeholder="Filter users..."
        class="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
        bind:value={login}
        oninput="{getUsersByLogin}">
</div>


<div class="grid grid-cols-1 md:grid-cols-4 gap-4 p-4">
    {#each users as user}
        {#if user.active}
            <ActiveUser {user} getUsers={getUsers}/>
        {:else}
            <InactiveUser {user} getUsers={getUsers}/>
        {/if}
    {/each}
</div>



