<script>
    import {getCurrentRoute, navigate} from '../stores/router.svelte.js';
    import Rent from "../components/Rent.svelte";
    import Notification from "../components/Notification.svelte";
    import {notify} from "../stores/notifier.svelte.js";
    import {fetchWithJwt, isAdmin, isManager} from "../stores/JWT.svelte.js";
    import config from '../config';

    let user = $state(null)
    let id = $state(getId(getCurrentRoute()))
    getUser()
    let notification = $state("")

    let currentRents = $state([])
    let endedRents = $state([])

    function getId(path){
        const regex = /^\/userDetails@([0-9a-fA-F\-]{36})$/;
        const match = regex.exec(path);
        return match[1];
    }

    function getUser(){
        let uri
        if(isAdmin() || isManager()){
            uri = `${config.BASE_URL}/users/${id}`
        }
        else {
            uri = `${config.BASE_URL}/users/self`
        }
        fetchWithJwt(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result => user = result)
                }
                else if(response.status === 400){
                    notification = "Error occurred: invalid uuid"
                }
                else if(response.status === 404){
                    notification = "User not found"
                }
                else{
                    notification = "Unknown error occurred"
                }
            })
    }

    function getCurrentRents() {
        currentRents = []
        const uri = `${config.BASE_URL}/rents/current/users/${id}`
        fetchWithJwt(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result => currentRents = result)
                }
                else {
                    notify("Backend error")
                }
            })
    }

    function getEndedRents() {
        currentRents = []
        const uri = `${config.BASE_URL}/rents/ended/users/${id}`
        fetchWithJwt(uri, {method: "GET"})
            .then(response => {
                if(response.status === 200){
                    response.json().then(result => endedRents = result)
                }
                else {
                    notify("Backend error")
                }
            })
    }


    function loadData() {
        getEndedRents();
        getCurrentRents();
    }

    loadData()
</script>
<div class="p-6 bg-gray-100 min-h-screen">
    {#if notification.length>0}
        <Notification message="User not found" callback={()=>{navigate("/")}}></Notification>
    {:else}
        {#if user===null}
            <p>Loading</p>
        {:else}
            <div class="bg-white p-6 rounded-lg shadow-md mb-8">
                <h2 class="text-xl font-bold text-gray-800 mb-2">{user.login}</h2>
                <div class="grid gap-2 text-sm text-gray-700">
                    <p><span class="font-semibold">Active:</span> {user.active}</p>
                    <p><span class="font-semibold">ID:</span> {user.id}</p>
                    <p><span class="font-semibold">Type:</span> {user.type}</p>
                </div>
            </div>
            <div class="rounded border bg-gray-100 p-2 m-2">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Current Rents</h2>
                {#if currentRents.length > 0}
                    {#each currentRents as rent}
                        <Rent {rent} onFinish={()=>{loadData()}}></Rent>
                    {/each}
                {:else}
                    <p> Nothing to show </p>
                {/if}
            </div>
            <div class="rounded border bg-gray-100 p-2 m-2">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Past Rents</h2>
                {#if endedRents.length > 0}
                    {#each endedRents as rent}
                        <Rent {rent}></Rent>
                    {/each}
                {:else}
                    <p> Nothing to show </p>
                {/if}
            </div>
            {/if}
    {/if}
</div>