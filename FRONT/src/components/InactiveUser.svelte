<script>
    import {navigate} from "../stores/router.svelte.js";
    import ConfirmNotification from "./ConfirmNotification.svelte";
    import {notify} from "../stores/notifier.svelte.js";
    import {fetchWithJwt, isAdmin, isManager} from "../stores/JWT.svelte.js";

    let {user, getUsers} = $props()
    function activate(id){
        const uri = `http://localhost:8080/users/${id}/activate`
        fetchWithJwt(uri, {method: "POST"}).then(
            result =>{
            if(result.status === 200){
                getUsers()
                notify("User activated")
            }
            else{
                notify("Error occurred")
            }
        })
    }

    let notification;
</script>

<div class="p-4 bg-red-100 border border-red-400 rounded-lg shadow-md">
    <h2 class="text-lg font-bold text-red-700">{user.login}</h2>
    <p class="text-sm text-red-600">ID: {user.id}</p>
    <span class="text-xs font-semibold text-red-700 uppercase">Type: {user.type}</span>
    <div class="mt-4 flex space-x-2">
        <input type="button" value="update" onclick={()=>navigate(`/updateUser@${user.id}`)} class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700">
        <input type="button" value="details" onclick={()=>navigate(`/userDetails@${user.id}`)} class="px-4 py-2 text-sm font-medium text-red-600 border border-red-600 rounded hover:bg-red-200">
        {#if isAdmin() || isManager() }
            <input type="button" value="activate" onclick={()=>{
                        notification.setAccept(()=>{activate(user.id)});
                        notification.show()
                    }} class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700">
        {/if}
    </div>
</div>

<ConfirmNotification bind:this = {notification} message="Are you sure?"/>