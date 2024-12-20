<script>
    let { message, callback } = $props();
</script>



<div class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" >
    <div class="bg-white w-1/3 p-6 rounded-lg shadow-lg text-center">
        <h2 class="text-xl font-bold text-gray-800 mb-4">{message}</h2>
        <div class="flex justify-center gap-4">
            <button
                    onclick={callback}
                    class="px-6 py-2 bg-green-500 text-white text-sm font-semibold rounded-lg hover:bg-green-600 transition"
            >
                Ok
            </button>
        </div>
    </div>
</div>
