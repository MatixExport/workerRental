<script>
    import {getCurrentRoute, navigate} from '../stores/router.svelte.js';
    import Rent from "../components/Rent.svelte";
    import Notification from "../components/Notification.svelte";

    let user = $state(null)
    let id = $state(getId(getCurrentRoute()))
    getUser()
    let notification = $state("")

    let currentRents = $state([])
    let endedRents = $state([])

    function getId(path){
        const regex = /^\/userDetails@([0-9a-fA-F\-]{36})$/;
        const match = regex.exec(path);

        if (match) {
            return match[1];
        } else {
            return null
        }
    }

    function getUser(){
        const uri = `http://localhost:8080/users/${id}`
        fetch(uri, {method: "GET"})
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
        const uri = `http://localhost:8080/rents/current/users/${id}`
        fetch(uri, {method: "GET"})
            .then(response => {
                response.json().then(result => currentRents = result)
            })
    }

    function getEndedRents() {
        currentRents = []
        const uri = `http://localhost:8080/rents/ended/users/${id}`
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

{#if notification.length>0}
    <Notification message="User not found" callback={()=>{navigate("/")}}></Notification>
{:else}
    {#if user===null}
        <p>Loading</p>
    {:else}
        <h1>{user.login}</h1>
        <h3>Active: {user.active}</h3>
        <h3>Id: {user.id}</h3>
        <h3>Type: {user.type}</h3>


        <div class="rounded border bg-gray-100 p-2 m-2">
            <h1>Current rents:</h1>
            {#if currentRents.length > 0}
                {#each currentRents as rent}
                    <Rent {rent} onFinish={()=>{loadData()}}></Rent>
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
        {/if}
{/if}