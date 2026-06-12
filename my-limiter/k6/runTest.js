import http from 'k6/http';
import { check } from 'k6';

export let options = {
    stages: [
        { duration: '10s', target: 50 },
        { duration: '30s', target: 0 },
        { duration: '10s', target: 50 },
    ]
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