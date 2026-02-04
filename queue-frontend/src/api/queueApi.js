import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api/entry",
    timeout: 3000,
})


export const entry = () => {
    return api.post("")
}

export const queueStatus = (token) => {
    return api.get("/check-status", {
        params: {token}
    })
}

export const exit = (token) => {
    return api.post("/exit", null, {
        params: { token }
    })
}