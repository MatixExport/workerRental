<script lang="ts">
    import ValidationError from "../components/ValidationError.svelte";
    import {getCurrentRoute, navigate} from "../stores/router.svelte.js";
    import Notification from "../components/Notification.svelte";
    import ConfirmNotification from "../components/ConfirmNotification.svelte";

    let user = $state(null)
    let id = $state(getId(getCurrentRoute()))
    getUser()
    let notification = $state("")
    let finalNotification = $state("")
    let passwordErrors = $state<String[]>([]);
    let confirmNotification

    function getId(path){
        const regex = /^\/updateUser@([0-9a-z-]{36})$/;
        const match = regex.exec(path);

        if (match) {
            return match[1];
        } else {
            return null
        }
    }

    function getUser(){
        const uri = `http://localhost:8080/users/${id}`
        fetch(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result => user = result)
                }
                else if(response.status === 400){
                    finalNotification = "Error occurred: invalid uuid"
                }
                else if(response.status === 404){
                    finalNotification = "User not found"
                }
                else{
                    finalNotification = "Unknown error occurred"
                }
            })
    }



    function validate(){
        passwordErrors = []
        if(user.password.length < 3){
            passwordErrors.push("Password to short")
        }
    }

    function submit(){
        validate()
        if(passwordErrors.length===0){
            const uri = `http://localhost:8080/users/${id}`;
            fetch(uri, {
                method: "POST",
                headers: {'Content-Type': 'application/json;charset=UTF-8'},
                body: JSON.stringify(user)
            }).then(
                response =>{
                    if(response.ok){
                        user.password=""
                        notification = "User updated"
                    }
                    else if(response.status === 404){
                        notification = "User does not exist"
                    }
                    else{
                        notification = "Unknown error occurred"
                    }

                }
            )
        }
    }


</script>
{#if finalNotification.length>0}
    <Notification message={finalNotification} callback={()=>{navigate("/")}}></Notification>
{:else}
    {#if user===null}
        <p>Loading</p>
    {:else}
    <div class="w-full h-full absolute bg-black bg-opacity-50 top-0 left-0">
        <div class="w-1/2 h-1/2 top-1/4 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
            <p>Updating user: {user.login}</p>
            <form oninput={validate} >
                <label for="password" class="text-blue-500 mb-2">New password:</label>
                <input name="password" type="password" autocomplete="current-password"
                       bind:value={user.password}
                       class:bg-red-500={passwordErrors.length>0} class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md"
                >
                {#each passwordErrors as error}
                    <ValidationError message={error}/>
                {/each}
                <input type="button" onclick={confirmNotification.show} value="Update"
                       class="w-full mr-0.5 mt-10 align-middle text-center rounded bg-red-500 py-3 px-6 text-xs uppercase
                       text-white shadow-md shadow-red-500/20 transition-all hover:shadow-red-600/40">
            </form>
        </div>
    </div>
    {#if notification.length > 0}
        <Notification message={notification} callback={()=>{notification=""}}/>
    {/if}


    <ConfirmNotification bind:this={confirmNotification} message="Are you sure?" accept={()=>{submit();}} />
    {/if}
{/if}