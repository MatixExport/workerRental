<script>
    import { navigationProps } from '../stores/router.js';
    import Rent from "../components/Rent.svelte";

    let user = $state();
    navigationProps.subscribe(value => (user = value));

    let rents = $state({current:[],ended:[]})

    function getCurrentRents(){
        const uri = `http://localhost:8080/rents/current/users/`+ user.id
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => rents.current = result)
            })
    }

    function getEndedRents(){
        const uri = `http://localhost:8080/rents/ended/users/`+ user.id
        fetch(uri, {method: "GET"})
            .then(response=>{
                response.json().then(result => rents.ended = result)
            })
    }

    getEndedRents();
    getCurrentRents();
</script>

<h1>{user.login}</h1>
<h3>Active: {user.active}</h3>
<h3>Id: {user.id}</h3>
<h3>Type: {user.type}</h3>

<div class="rounded border bg-gray-100 p-2 m-2">
    <h1>Current rents:</h1>
    {#if rents.current.length > 0}
        {#each rents.current as rent}
            <Rent {rent}></Rent>
        {/each}
    {:else}
        <p> Nothing to show </p>
    {/if}
</div>
<div class="rounded border bg-gray-100 p-2 m-2">
    <h1>Past rents:</h1>
    {#if rents.ended.length > 0}
        {#each rents.ended as rent}
            <Rent {rent}></Rent>
        {/each}
    {:else}
    <p> Nothing to show </p>
    {/if}
</div>