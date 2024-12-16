<script lang="ts">
    import type {User} from "../lib/Int.js"
    import {navigate} from "../stores/router";
    let users = $state<User[]>([])

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

    function deactivate(id: string){
        const uri = `http://localhost:8080/users/`+id+'/deactivate'
        fetch(uri, {method: "POST"}).then(()=>getUsers())
    }

    function activate(id: string){
        const uri = `http://localhost:8080/users/`+id+'/activate'
        fetch(uri, {method: "POST"}).then(()=>getUsers())
    }

    getUsers();
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
                <input type="button" value="update" class="basis-1/2 border-black border-2 border-solid bg-green-300 hover:bg-green-500">
                <input type="button" value="details" onclick={()=>navigate("/userDetails", user)} class="basis-1/2 border-black border-2 border-solid bg-green-300 hover:bg-green-500">
                {#if user.active}
                    <input type="button" value="deactivate" onclick={()=>deactivate(user.id)} class="basis-1/2 border-black border-2 border-solid bg-red-300 p-2 hover:bg-red-500">
                {:else}
                    <input type="button" value="activate" onclick={()=>activate(user.id)} class="basis-1/2 border-black border-2 border-solid bg-green-300 p-2 hover:bg-green-500">
                {/if}
            </div>
        </div>
    {/each}
</div>