<script>
    import ConfirmNotification from "./ConfirmNotification.svelte";

    let { rent, onFinish= ()=>{} } = $props();

    let confirmNotification

    async function finishRent(rentId){
        const uri = `http://localhost:8080/rents/`+rentId+'/finish'
        await fetch(uri, {method: "POST"})
        onFinish();
    }
</script>

<p class="border bg-gray-400 p-2 rounded">
    id: {rent.id}<br>
    workerId: {rent.workerID}<br>
    userId: {rent.userID}<br>
    start date: {rent.startDate}<br>
    {#if rent.endDate != null}
        end date: {rent.endDate}<br>
    {/if}

    {#if rent.endDate === null}
        <input type="button" value="finish" onclick={confirmNotification.show} class="basis-1/2 border-black border-2 border-solid bg-red-300 p-2 hover:bg-red-500">
    {/if}
</p>


<ConfirmNotification bind:this={confirmNotification} message="Are you sure?" accept={()=>{finishRent(rent.id);}}/>
