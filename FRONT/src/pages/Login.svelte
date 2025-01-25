<script >
    import ValidationError from "../components/ValidationError.svelte";
    import {fetchLogin} from "../stores/JWT.svelte.js";

    let user = $state({login:"",password:""})

    let loginErrors = $state();
    let passwordErrors = $state();


    function submit(){
        fetchLogin(user.login, user.password)
    }

</script>

<div class="w-1/2 h-1/2 top-1/5 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
    <h1 class="text-5xl text-blue-700 mb-2">Login</h1>
    <form class="p-4 bg-gray-50 border border-gray-300 rounded-lg shadow-md space-y-4">
        <label for="username" class="text-blue-700 mb-2 text-2xl">Login:</label>
        <input name="username" autocomplete="username"
               bind:value={user.login}
               class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        >
        {#each loginErrors as error}
            <ValidationError message={error}/>
        {/each}

        <label for="password" class="text-blue-700 mb-2 text-2xl">Password:</label>
        <input name="password" type="password" autocomplete="current-password"
               bind:value={user.password}
               class="border-2 border-solid bg-opacity-50 mt-1 p-2 w-full rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        >
        {#each passwordErrors as error}
            <ValidationError message={error}/>
        {/each}

        <input type="button" onclick={submit} value="Login"
               class="w-full mr-0.5 mt-10 align-middle text-center rounded bg-blue-500 py-3 px-6 text-xs uppercase
                   text-white shadow-md shadow-blue-500/20 transition-all hover:shadow-blue-600/60">
    </form>
</div>



