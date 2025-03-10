let currentRoute = $state()

export function navigate(path) {
    window.history.pushState({}, "", path);
    currentRoute = path

}

window.addEventListener("popstate", () => {
    currentRoute = window.location.pathname;
});

export function getCurrentRoute(){
    return currentRoute
}

currentRoute = window.location.pathname;