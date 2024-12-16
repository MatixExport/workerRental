import { writable } from "svelte/store";


export const navigationProps = writable({});
export const currentRoute = writable(window.location.pathname);


export function navigate(path, props) {
    if(props){
        navigationProps.set(props);
    }
    window.history.pushState({}, "", path);
    currentRoute.set(path);
}

window.addEventListener("popstate", () => {
    currentRoute.set(window.location.pathname);
});
