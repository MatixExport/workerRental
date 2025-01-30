import {notify} from "./notifier.svelte.js";
import { jwtDecode } from "jwt-decode";
import {navigate} from "./router.svelte.js";
import config from "../config";

let token = $state(localStorage.getItem("token"));
let admin = $state(false)
let client = $state(false)
let manager = $state(false)
let username = $state("")
loadUserFromToken()
export function isAdmin(){
    return admin
}
export function isManager(){
    return manager
}
export function isClient(){
    return client
}
export function getUsername(){
    return username
}


export function fetchWithJwt(url, options){
    const headers = {
        ...options.headers,
        "Authorization": `Bearer ${token}`
    }
    console.log(headers)

    const newOptions = {
        ...options,
        headers: headers,
    }

    return fetch(url, newOptions)
}

export async function fetchLogin(login, password){
    try{
        const uri = `${config.BASE_URL}/login`;
        return fetch(uri, {
            method: "POST",
            headers: {'Content-Type': 'application/json;charset=UTF-8'},
            body: JSON.stringify({login: login, password: password})
        }).then(
            response =>{
                if(response.ok){
                    return response.text()
                }
                else {
                    throw response.text()
                }
            }
        ).then(newToken => {
            token = newToken
            localStorage.setItem("token", token)
            loadUserFromToken()
            navigate("/")
        })
    }
    catch (error){
        notify(error)
    }

}

function loadUserFromToken() {
    if (token) {
        try {
            const decodedToken = jwtDecode(token);
            admin = decodedToken.groups.includes("ADMIN");
            client = decodedToken.groups.includes("CLIENT");
            manager = decodedToken.groups.includes("MANAGER");
            username = decodedToken.sub;

            setTimeout(logout, 1000 * 60 * 10)
        } catch (error) {
            console.error("Failed to decode token:", error);
        }
    } else {
        console.log("No token found in localStorage.");
    }
}

export function getToken(){
    return token;
}

export function logout(){
    token = ""
    localStorage.removeItem("token")
    admin = false
    client = false
    manager = false
    username = ""
    navigate("/login")
}