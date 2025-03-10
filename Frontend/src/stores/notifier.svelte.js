let notification = $state("")
export function notify(message){
    notification = message
}
export function getNotification(){
    return notification
}