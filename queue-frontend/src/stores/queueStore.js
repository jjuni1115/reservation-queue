import {defineStore} from "pinia";

export const useQueueStore = defineStore('queueStore', {

    state: () => ({
        token: localStorage.getItem("token"),
        status: null,
        rank: null
    }),
    actions: {
        setToken(token){
            this.token=token
            localStorage.setItem("token", token)
        },
        setStatus(status, rank){
            this.status = status
            this.rank = rank
        },
        clear(){
            this.token = null
            this.status = null
            this.rank = null
            localStorage.removeItem("token")
        }
    }

})