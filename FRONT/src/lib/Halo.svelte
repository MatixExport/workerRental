<script>
    let username = $state("")

    let errors = $state("")

    let valid = $derived(username.length > 15)

    function validate(){
        if(!valid){
            errors = "Username to short"
        }
    }
</script>

<form oninput={validate} >
    <input id="username" bind:value={username}>
    {#if !valid}
        <p> {errors} </p>
    {/if}
</form>

