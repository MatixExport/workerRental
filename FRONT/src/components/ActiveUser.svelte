<script>
    import {notify} from "../stores/notifier.svelte.js";
    import {navigate} from "../stores/router.svelte.js";
    import ConfirmNotification from "./ConfirmNotification.svelte";
    import {fetchWithJwt, getUsername, isAdmin, isManager} from "../stores/JWT.svelte.js";
    let {user, getUsers} = $props()

    function deactivate(id){
        const uri = `http://localhost:8080/users/${id}/deactivate`
        fetchWithJwt(uri, {method: "POST"}).then(
            result =>{
                if(result.status === 200){
                    getUsers()
                    notify("User deactivated")
                }
                else{
                    notify("Error occurred")
                }
            })
    }

    let notification;
</script>

<div class="p-4 bg-green-100 border border-green-400 rounded-lg shadow-md">
    <h2 class="text-lg font-bold text-green-700">{user.login}</h2>
    <p class="text-sm text-green-600">ID: {user.id}</p>
    <span class="text-xs font-semibold text-green-700 uppercase">Type: {user.type}</span>
    <div class="mt-4 flex space-x-2">
        {#if getUsername() === user.login || isAdmin() || isManager()}
            <input type="button" value="update" onclick={()=>navigate(`/updateUser@${user.id}`)} class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700">
            <input type="button" value="details" onclick={()=>navigate(`/userDetails@${user.id}`)} class="px-4 py-2 text-sm font-medium text-green-600 border border-green-600 rounded hover:bg-green-200">
        {/if}
        {#if isAdmin() || isManager() }
            <input type="button" value="deactivate" onclick={()=>{
                        notification.setAccept(()=>{deactivate(user.id)});
                        notification.show();
                    }} class="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded hover:bg-red-700">
        {/if}
    </div>
</div>

<ConfirmNotification bind:this = {notification} message="Are you sure?" />