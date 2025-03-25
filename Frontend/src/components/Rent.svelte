<script>
    import ConfirmNotification from "./ConfirmNotification.svelte";
    import {fetchWithJwt} from "../stores/JWT.svelte.js";
    import config from "../config";

    let { rent, onFinish= ()=>{} } = $props();

    let confirmNotification

    async function finishRent(rentId){
        const uri = `${config.BASE_URL}/rents/`+rentId+'/finish'
        await fetchWithJwt(uri, {method: "POST"})
        onFinish();
    }
</script>

<div class="bg-white p-6 rounded-lg shadow-lg m-6">
    <div class="grid gap-2 text-sm text-gray-700">
        <p><span class="font-semibold">ID:</span>{rent.id}</p>
        <p><span class="font-semibold">workerId:</span>{rent.workerID}</p>
        <p><span class="font-semibold">userId:</span>{rent.userID}</p>
        <p><span class="font-semibold">start date:</span>{rent.startDate}</p>
            {#if rent.endDate != null}
                <p><span class="font-semibold">end date:</span>{rent.endDate}</p>
            {/if}

            {#if rent.endDate === null}
                <input type="button" value="Finish" onclick={confirmNotification.show} class="mt-4 w-full px-4 py-2 text-sm font-semibold text-white bg-red-500 rounded-lg hover:bg-red-600 transition">
            {/if}
    </div>
</div>



<ConfirmNotification bind:this={confirmNotification} message="Are you sure?" accept={()=>{finishRent(rent.id);}}/>
