
let navigationProps = $state()
let currentRoute = $state()

export function navigate(path, props) {
    if(props){
        navigationProps = props;
    }
    window.history.pushState({}, "", path);
    currentRoute = path

}

window.addEventListener("popstate", () => {
    currentRoute = window.location.pathname;
});

export function getGlobalProps(){
    return navigationProps;
}

export function getCurrentRoute(){
    return currentRoute
}