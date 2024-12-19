<script>
    let { message="", accept=()=>{}, cancel=()=>{}, defaultDisplayed=false } = $props();
    let displayed = $state(defaultDisplayed)
    export function show(){
        displayed = true
    }

    export function setAccept(callback){
        accept = callback;
    }
    export function setCancel(callback){
        cancel = callback;
    }
</script>

<div class="w-full h-full absolute bg-black bg-opacity-50 top-0 left-0 " class:hidden={!displayed}>
    <div class="w-1/2 h-1/2 top-1/4 left-1/4 absolute bg-white border-red-500 p-20 rounded-2xl">
        <p>{message}</p>
        <button class="p-2 bg-gray-400 m-2" onclick={()=>{accept(); displayed=false;}}>Accept</button>
        <button class="p-2 bg-gray-400 m-2" onclick={()=>{cancel();displayed=false;}}>Cancel</button>
    </div>
</div>
