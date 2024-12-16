<script>
    let { rent } = $props();

    function finishRent(rentId){
        const uri = `http://localhost:8080/rents/`+rentId+'/finish'
        fetch(uri, {method: "POST"})
    }
</script>

<p class="border bg-gray-400 p-2 rounded">
    id: {rent.id}<br>
    start date: {rent.startDate}<br>
    {#if rent.endDate != null}
        end date: {rent.endDate}<br>
    {/if}
    workerId: {rent.workerID}<br>
    {#if rent.endDate === null}
        <input type="button" value="finish" onclick={()=>finishRent(rent.id)} class="basis-1/2 border-black border-2 border-solid bg-red-300 p-2 hover:bg-red-500">
    {/if}
</p>