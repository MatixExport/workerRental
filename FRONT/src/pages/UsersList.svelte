<script>
    import {navigate} from "../stores/router.svelte";
    import ConfirmNotification from "../components/ConfirmNotification.svelte";
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

    function deactivate(id){
        const uri = `http://localhost:8080/users/`+id+'/deactivate'
        fetch(uri, {method: "POST"}).then(()=>getUsers())
    }

    function activate(id){
        const uri = `http://localhost:8080/users/`+id+'/activate'
        fetch(uri, {method: "POST"}).then(()=>getUsers())
    }

    getUsers();

    let notificationActivate;
    let notificationDeactivate;
</script>

<input bind:value={login} class="border bg-gray-400" oninput="{getUsersByLogin2}">

<div class="flex">
    {#each users as user}
        <div class:bg-green-400={user.active} class:bg-red-400={!user.active}
             class="flex-grow border-solid border-2 border-black m-2 p-2">
            <p>
                {user.login}<br>
                {user.id}<br>
                {user.type}<br>
            </p>
            <div class="flex">
                <input type="button" value="update" onclick={()=>navigate("/updateUser", user)} class="basis-1/2 border-black border-2 border-solid bg-green-300 hover:bg-green-500">
                <input type="button" value="details" onclick={()=>navigate("/userDetails", user)} class="basis-1/2 border-black border-2 border-solid bg-green-300 hover:bg-green-500">
                {#if user.active}
                    <input type="button" value="deactivate" onclick={()=>{
                        notificationDeactivate.setAccept(()=>{deactivate(user.id)});
                        notificationDeactivate.show();
                    }} class="basis-1/2 border-black border-2 border-solid bg-red-300 p-2 hover:bg-red-500">
                {:else}
                    <input type="button" value="activate" onclick={()=>{
                        notificationActivate.setAccept(()=>{activate(user.id)});
                        notificationActivate.show()
                    }} class="basis-1/2 border-black border-2 border-solid bg-green-300 p-2 hover:bg-green-500">
                {/if}
            </div>
        </div>
    {/each}
</div>

<ConfirmNotification bind:this = {notificationActivate} message="Are you sure?" />
<ConfirmNotification bind:this = {notificationDeactivate} message="Are you sure?"/>
