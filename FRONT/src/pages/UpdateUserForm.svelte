<script lang="ts">
    import ValidationError from "../components/ValidationError.svelte";
    import {navigate, getGlobalProps} from "../stores/router.svelte.js";
    import Notification from "../components/Notification.svelte";
    import ConfirmNotification from "../components/ConfirmNotification.svelte";

    let user = getGlobalProps();

    let passwordErrors = $state<String[]>([]);

    let notification = $state("")
    let confirmNotification

    function validate(){
        passwordErrors = []
        if(user.password.length < 3){
            passwordErrors.push("Password to short")
        }
    }

    function submit(){
        validate()
        if(passwordErrors.length===0){
            const uri = `http://localhost:8080/client`;
            fetch(uri, {
                method: "POST",
                headers: {'Content-Type': 'application/json;charset=UTF-8'},
                body: JSON.stringify(user)
            }).then(
                response =>{
                    if(response.ok){
                        user.password=""
                        user.login=""
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
<div class="w-full h-full absolute bg-black bg-opacity-50 top-0 left-0">
    <div class="w-1/2 h-1/2 top-1/4 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
        <form oninput={validate} >
            <label for="password" class="text-blue-500 mb-2">Password:</label>
            <input name="password" type="password" autocomplete="current-password"
                   bind:value={user.password}
                   class:bg-red-500={passwordErrors.length>0} class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md"
            >
            {#each passwordErrors as error}
                <ValidationError message={error}/>
            {/each}
            <input type="button" onclick={confirmNotification.show} value="Register"
                   class="w-full mr-0.5 mt-10 align-middle text-center rounded bg-red-500 py-3 px-6 text-xs uppercase
                   text-white shadow-md shadow-red-500/20 transition-all hover:shadow-red-600/40">
        </form>
    </div>
</div>

{#if notification.length > 0}
    <Notification message={notification} callback={()=>{notification=""}}/>
{/if}


<ConfirmNotification bind:this={confirmNotification} message="Are you sure?" accept={()=>{submit();}} />
