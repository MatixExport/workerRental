<script>
    import {navigate} from "../stores/router.svelte";
    import ConfirmNotification from "../components/ConfirmNotification.svelte";
    import ActiveUser from "../components/ActiveUser.svelte";
    import InactiveUser from "../components/InactiveUser.svelte";
    let users = $state([])

    let login = $state("");

    function getUsers(){
        const uri = `http://localhost:8080/users`
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => users = result)
            })
    }

    function getUsersByLogin2(){
        if(login.length===0){
            getUsers()
        }
        const uri = `http://localhost:8080/users/loginContains/${login}`
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => users = result)
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
        oninput="{getUsersByLogin2}">
</div>


<div class="grid grid-cols-4 gap-4 p-4">
    {#each users as user}
        {#if user.active}
            <ActiveUser {user} getUsers={getUsers}/>
        {:else}
            <InactiveUser {user} getUsers={getUsers}/>
        {/if}
    {/each}
</div>



