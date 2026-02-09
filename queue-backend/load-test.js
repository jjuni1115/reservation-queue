import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
    vus: 50,             // 동시에 5000명 요청
    duration: '10s',      // 10초간 지속
};

export default function () {
    const res = http.post(`http://localhost:8080/api/entry`);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.1);
}