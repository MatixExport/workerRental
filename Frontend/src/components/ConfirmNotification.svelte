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



<div class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" class:hidden={!displayed}>
    <div class="bg-white w-1/3 p-6 rounded-lg shadow-lg text-center">
        <h2 class="text-xl font-bold text-gray-800 mb-4">{message}</h2>
        <div class="flex justify-center gap-4">
            <button
                    onclick={()=>{accept(); displayed=false;}}
                    class="px-6 py-2 bg-green-500 text-white text-sm font-semibold rounded-lg hover:bg-green-600 transition"
            >
                Accept
            </button>
            <button
                    onclick={()=>{cancel();displayed=false;}}
                    class="px-6 py-2 bg-gray-300 text-gray-700 text-sm font-semibold rounded-lg hover:bg-gray-400 transition"
            >
                Cancel
            </button>
        </div>
    </div>
</div>
