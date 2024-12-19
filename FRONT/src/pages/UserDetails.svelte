<script>
    //TODO update view on rent finish
    import {getGlobalProps} from '../stores/router.svelte.js';
    import Rent from "../components/Rent.svelte";

    let user = getGlobalProps();

    let currentRents = $state([])
    let endedRents = $state([])

    function getCurrentRents() {
        currentRents = []
        const uri = `http://localhost:8080/rents/current/users/` + user.id
        fetch(uri, {method: "GET"})
            .then(response => {
                response.json().then(result => currentRents = result)
            })
    }

    function getEndedRents() {
        currentRents = []
        const uri = `http://localhost:8080/rents/ended/users/` + user.id
        fetch(uri, {method: "GET"})
            .then(response => {
                response.json().then(result => endedRents = result)
            })
    }


    function loadData() {
        getEndedRents();
        getCurrentRents();
    }

    loadData()
</script>

<h1>{user.login}</h1>
<h3>Active: {user.active}</h3>
<h3>Id: {user.id}</h3>
<h3>Type: {user.type}</h3>

<div class="rounded border bg-gray-100 p-2 m-2">
    <h1>Current rents:</h1>
    {#if currentRents.length > 0}
        {#each currentRents as rent}
            <Rent {rent}></Rent>
        {/each}
    {:else}
        <p> Nothing to show </p>
    {/if}
</div>
<div class="rounded border bg-gray-100 p-2 m-2">
    <h1>Past rents:</h1>
    {#if endedRents.length > 0}
        {#each endedRents as rent}
            <Rent {rent}></Rent>
        {/each}
    {:else}
        <p> Nothing to show </p>
    {/if}
</div>
