<script>
    import {navigate} from "../stores/router.svelte.js";
    import ConfirmNotification from "./ConfirmNotification.svelte";
    import {notify} from "../stores/notifier.svelte.js";
    import {fetchWithJwt, isAdmin, isManager} from "../stores/JWT.svelte.js";
    import config from "../config";

    let {Entities.user, getUsers} = $props()
    function activate(id){
        const uri = `${config.BASE_URL}/users/${id}/activate`
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
    <h2 class="text-lg font-bold text-red-700">{Entities.user.login}</h2>
    <p class="text-sm text-red-600">ID: {Entities.user.id}</p>
    <span class="text-xs font-semibold text-red-700 uppercase">Type: {Entities.user.type}</span>
    <div class="mt-4 flex space-x-2">
        {#if isAdmin() || isManager() }
            <input type="button" value="update" onclick={()=>navigate(`/updateUser@${Entities.user.id}`)} class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700">

            <input type="button" value="details" onclick={()=>navigate(`/userDetails@${Entities.user.id}`)} class="px-4 py-2 text-sm font-medium text-red-600 border border-red-600 rounded hover:bg-red-200">

            <input type="button" value="activate" onclick={()=>{
                        notification.setAccept(()=>{activate(Entities.user.id)});
                        notification.show()
                    }} class="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700">
        {/if}
    </div>
</div>

<ConfirmNotification bind:this = {notification} message="Are you sure?"/>