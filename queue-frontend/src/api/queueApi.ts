import axios from "axios";

export type QueueStatus = 'WAIT' | 'ENTER' | 'EXIT';
export interface QueueStatusResponse {
    rank: number,
    status: QueueStatus,
}

const api = axios.create({
    baseURL: "http://localhost:8080/api/entry",
    timeout: 3000,
})


export const entry = () => {
    return api.post<void>("")
}

export const queueStatus = (token : String) => {
    return api.get<QueueStatus>("/check-status", {
        params: {token}
    })
}

export const exit = (token : String) => {
    return api.post<void>("/exit", null, {
        params: { token }
    })
}