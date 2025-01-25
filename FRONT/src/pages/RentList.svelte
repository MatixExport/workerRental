<script>
    import Rent from "../components/Rent.svelte";
    import {notify} from "../stores/notifier.svelte.js";
    import {fetchWithJwt} from "../stores/JWT.svelte.js";

    let allRents = $state([])
    let currentRents = $derived(allRents.filter((rent)=>{return rent.endDate == null}))
    let endedRents = $derived(allRents.filter((rent)=>{return rent.endDate != null}))


    function getRents() {
        const uri = `http://localhost:8080/rents`
        fetchWithJwt(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result =>allRents = result)
                }
                else {
                    notify("Backend error")
                }
            })
    }
    getRents()
</script>
<div class="p-8 bg-gray-100 min-h-screen">
    <div class="mb-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Current Rents</h2>
        {#if currentRents.length > 0}
            {#each currentRents as rent}
                <Rent {rent} onFinish={()=>{getRents()}}></Rent>
            {/each}
        {:else}
            <p> Nothing to show </p>
        {/if}
    </div>
    <div class="mb-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Past Rents</h2>
        {#if endedRents.length > 0}
            {#each endedRents as rent}
                <Rent {rent}></Rent>
            {/each}
        {:else}
            <p> Nothing to show </p>
        {/if}
    </div>
</div>