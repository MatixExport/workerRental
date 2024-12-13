<script>
    import ValidationError from "./ValidationError.svelte";

    let username = $state("")
    let password = $state("")

    let loginErrors = $state([]);
    let passwordErrors = $state([]);

    function validate(){
        loginErrors = []
        passwordErrors = []
        if(username.length < 3){
            loginErrors.push("Username to short")
        }
        if(password.length < 3){
            passwordErrors.push("Password to short")
        }
    }

    function submit(){
        if(loginErrors.length===0 && passwordErrors.length===0){

        }
    }


</script>

<form oninput={validate} >
    <input id="username" bind:value={username}>
    {#each loginErrors as error}
        <ValidationError message={error}/>
    {/each}
    <input id="password" type="password" bind:value={password}>
    {#each passwordErrors as error}
        <ValidationError message={error}/>
    {/each}
    <input type="button" onclick={submit} value="Register">
</form>

