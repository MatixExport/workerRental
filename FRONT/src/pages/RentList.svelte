<script>
    import Rent from "../components/Rent.svelte";

    let allRents = $state([])
    let currentRents = $derived(allRents.filter((rent)=>{return rent.endDate == null}))
    let endedRents = $derived(allRents.filter((rent)=>{return rent.endDate != null}))


    function getRents() {
        const uri = `http://localhost:8080/rents`
        fetch(uri, {method: "GET"})
            .then(response => {
                response.json().then(result => allRents = result)
            })
    }
    getRents()
</script>
<div class="rounded border bg-gray-100 p-2 m-2">
    <h1>Current rents:</h1>
    {#if currentRents.length > 0}
        {#each currentRents as rent}
            <Rent {rent} onFinish={()=>{getRents()}}></Rent>
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
