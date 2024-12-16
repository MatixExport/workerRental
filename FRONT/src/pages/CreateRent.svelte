<script >
    import ValidationError from "../components/ValidationError.svelte";


    let users= $state()
    let workers= $state()
    let selectedUser = $state(null)
    let selectedWorker = $state(null)
    let startDate = $state(null)
    let errors = $state([])

    function getUsers(){
        const uri = `http://localhost:8080/users`
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => users = result)
            })
    }

    function getWorkers(){
        const uri = `http://localhost:8080/workers`
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => workers = result)
            })
    }

    function validate(){
        errors = []
        if(selectedUser === null){
            errors.push("User must be selected")
        }
        if(selectedWorker === null){
            errors.push("Worker must be selected")
        }
        if(startDate === null){
            errors.push("Date must be selected")
        }
        if(new Date(startDate) <= new Date()){
            errors.push("Date must be from the future")
        }
        return(errors.length === 0)
    }

    function createRent() {
        if(!validate()){
            return
        }
        const uri = `http://localhost:8080/rents/users/${selectedUser.id}/workers/${selectedWorker.id}`;
        fetch(uri, {
            method: "POST",
            headers: {'Content-Type': 'application/json;charset=UTF-8'},
            body: JSON.stringify({startDate: startDate})
        }).then(
            response => {
                if (response.ok) {
                    selectedWorker = null;
                    selectedUser = null;
                    startDate = null;

                    errors.push("Rent created")
                } else if (response.status === 409) {
                    errors.push("Worker occupied")
                } else if (response.status === 403) {
                    errors.push("User is inactive")
                }
                else{
                    errors.push(`Unknown error of status code: ${response.status}`)
                }
            });
    }

    getUsers();
    getWorkers();
</script>

<form>
    <select bind:value={selectedUser} class="border">
        {#each users as user}
            <option value="{user}">{user.login}</option>
        {/each}
    </select>
    <select bind:value={selectedWorker} class="border">
        {#each workers as worker}
            <option value="{worker}">{worker.id}</option>
        {/each}
    </select>
    <input type="datetime-local" bind:value={startDate} class="border">
    <input type="button" onclick={createRent} value="Create rent" class="border hover:bg-gray-400">
</form>
{#each errors as error}
    <ValidationError message={error}/>
{/each}