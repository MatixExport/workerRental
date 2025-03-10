<script lang="ts">
    import ValidationError from "../components/ValidationError.svelte";
    import {getCurrentRoute, navigate} from "../stores/router.svelte.js";
    import Notification from "../components/Notification.svelte";
    import ConfirmNotification from "../components/ConfirmNotification.svelte";
    import {notify} from "../stores/notifier.svelte";
    import {fetchWithJwt, isAdmin, isClient, isManager} from "../stores/JWT.svelte.js";
    import config from "../config";

    let user = $state(null)
    let id = $state(getId(getCurrentRoute()))
    let ifMatch = $state("")
    getUser()
    let notification = $state("")
    let passwordErrors = $state<String[]>([]);
    let confirmNotification

    function getId(path){
        const regex = /^\/updateUser@([0-9a-z-]{36})$/;
        const match = regex.exec(path);

        return match[1];
    }

    function getUser(){
        let uri
        if(isAdmin() || isManager()){
            uri = `${config.BASE_URL}/users/${id}/signed`
        }
        else {
            uri = `${config.BASE_URL}/users/self/signed`
        }
        fetchWithJwt(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    ifMatch = response.headers.get("ETag");
                    response.json().then(result => user = result)
                }
                else if(response.status === 400){
                    notification = "Error occurred: invalid uuid"
                }
                else if(response.status === 404){
                    notification = "User not found"
                }
                else if(response.status === 409){
                    notification = "Password to long"
                }
                else{
                    notification = "Unknown error occurred"
                }
            })
    }



    function validate(){
        passwordErrors = []
        if(user.password.length < 3){
            passwordErrors.push("Password too short")
        }
        else if(user.password.length > 30){
            passwordErrors.push("Password too long")
        }
    }

    function submit(){
        validate()
        if(passwordErrors.length===0){
            let uri
            if(isAdmin() || isManager()){
                uri = `${config.BASE_URL}/users/${id}/signed`
            }
            else {
                uri = `${config.BASE_URL}/users/self/signed`
            }
            fetchWithJwt(uri, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    "If-Match": ifMatch.substring(1,ifMatch.length-1)
                },
                body: JSON.stringify(user)
            }).then(
                response =>{
                    user.password=""
                    user.oldPassword=""
                    if(response.ok){
                        notify("User updated")
                    }
                    else if(response.status === 404){
                        notify("User does not exist")
                    }
                    else if(response.status === 409){
                        user.login = user.login+" O_O"
                        notify("Signature mismatch - data was tampered with")
                    }
                    else if(response.status === 401){
                        notify("Wrong previous password")
                    }
                    else{
                        notify("Unknown error occurred")
                    }
                }
            )
        }
    }


</script>
{#if notification.length>0}
    <Notification message={notification} callback={()=>{navigate("/")}}></Notification>
{:else}
    {#if user===null}
        <p>Loading</p>
    {:else}
        <div class="w-1/2 h-1/2 top-1/5 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
            <h1 class="text-5xl text-blue-700 mb-2">Updating user: {user.login}</h1>
            <form oninput={validate} class="p-4 bg-gray-50 border border-gray-300 rounded-lg shadow-md space-y-4">
                <label for="login" class="text-gray-400 mb-2 text-2xl">Login:</label>
                <input name="login" type="text"
                    bind:value={user.login}
                    disabled
                    class="border-2 border-gray-300 bg-gray-100 bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-gray-400 cursor-not-allowed"
                >
                <label for="type" class="text-gray-400 mb-2 text-2xl">Type:</label>
                <input name="type" type="text"
                    bind:value={user.type}
                    disabled
                    class="border-2 border-gray-300 bg-gray-100 bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-gray-400 cursor-not-allowed"
                >
                {#if isClient()}
                    <label for="password_old" class="text-blue-700 mb-2 text-2xl">Old password:</label>
                    <input name="password_old" type="password" autocomplete="current-password"
                           bind:value={user.oldPassword}
                           class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                {/if}
                <label for="password" class="text-blue-700 mb-2 text-2xl">New password:</label>
                <input name="password" type="password" autocomplete="current-password"
                       bind:value={user.password}
                       class:bg-red-500={passwordErrors.length>0} class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                {#each passwordErrors as error}
                    <ValidationError message={error}/>
                {/each}
                <input type="button" onclick={confirmNotification.show} value="Update"
                       class="w-full mr-0.5 mt-10 align-middle text-center rounded bg-blue-500 py-3 px-6 text-xs uppercase
                   text-white shadow-md shadow-blue-500/20 transition-all hover:shadow-blue-600/60">
            </form>
        </div>

    <ConfirmNotification bind:this={confirmNotification} message="Are you sure?" accept={()=>{submit();}} />
    {/if}
{/if}