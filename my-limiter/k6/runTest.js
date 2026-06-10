import http from 'k6/http';
import { check } from 'k6';

export let options = {
    vus: 50,
    duration: '30s',
};

export default function () {
    let result = http.get('http://localhost:8080/limiter/hit_me', {
        headers: { 'X-Client-Id': `client_${Math.floor(Math.random() * 5)}` },
    });

    check(result, {
        'status is 200': (res) => res.status === 200,
        'status is 429': (res) => res.status === 429,
    });
}