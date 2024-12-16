<script lang="ts">
    import ValidationError from "../components/ValidationError.svelte";
    import type {CreateUser} from "../lib/Int";
    import {navigate} from "../stores/router.js";

    let user: CreateUser = $state({
        "login": "",
        "password": "",
        "type": "CLIENT"
    })

    let loginErrors = $state<String[]>([]);
    let passwordErrors = $state<String[]>([]);
    let userErrors = $state<String[]>([]);

    function validate(){
        userErrors=[]
        loginErrors = []
        passwordErrors = []
        if(user.login.length < 3){
            loginErrors.push("Username to short")
        }
        if(user.password.length < 3){
            passwordErrors.push("Password to short")
        }
    }

    function submit(){
        validate()
        if(loginErrors.length===0 && passwordErrors.length===0){
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
                        userErrors.push("registered")
                        navigate("/");
                    }
                    else if(response.status === 409){
                        userErrors.push("username already exists")
                    }
                    else{
                        userErrors.push("Unknown error occurred")
                    }

                }
            )
        }
    }


</script>
<div class="w-full h-full absolute bg-black bg-opacity-50 top-0 left-0">
    <div class="w-1/2 h-1/2 top-1/4 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
        <form oninput={validate} >
            <label for="username" class="text-blue-500 mb-2">Login:</label>
            <input name="username" autocomplete="username"
                   bind:value={user.login}
                   class:bg-red-500={loginErrors.length>0} class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md"
            >
            {#each loginErrors as error}
                <ValidationError message={error}/>
            {/each}

            <label for="password" class="text-blue-500 mb-2">Password:</label>
            <input name="password" type="password" autocomplete="current-password"
                   bind:value={user.password}
                   class:bg-red-500={passwordErrors.length>0} class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md"
            >
            {#each passwordErrors as error}
                <ValidationError message={error}/>
            {/each}


            {#each userErrors as error}
                <ValidationError message={error}></ValidationError>
            {/each}
            <input type="button" onclick={submit} value="Register"
                   class="w-full mr-0.5 mt-10 align-middle text-center rounded bg-red-500 py-3 px-6 text-xs uppercase
                   text-white shadow-md shadow-red-500/20 transition-all hover:shadow-red-600/40">
        </form>
    </div>
</div>

