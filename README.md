# Reservation Queue

Redis 기반 대기열(선착순 예매) 시스템 예제입니다.  
프로젝트는 `queue-backend`(Spring Boot)와 `queue-frontend`(Vue 3 + Vite)로 구성되어 있습니다.

## 프로젝트 구조

```text
reservation-queue/
├── queue-backend/   # 대기열 API, Redis 연동, 승급 스케줄러
└── queue-frontend/  # 사용자 진입/대기/입장 화면
```

## 주요 기능

- 대기열 진입 토큰 발급
- Redis Sorted Set 기반 순번 관리
- 주기적 승급(대기 -> 입장)
- 입장 세션 만료(TTL) 처리
- 사용자의 명시적 퇴장 처리

## 기술 스택

- Backend: Java 17, Spring Boot 3, Spring Data Redis
- Frontend: Vue 3, Vite, Pinia, Vue Router, Axios
- Infra: Redis

## 사전 준비

- Java 17+
- Node.js 18+ (권장)
- Redis 6+

Redis 기본 연결 정보:

- Host: `localhost`
- Port: `6379`

`queue-backend/src/main/resources/application.yml`에서 변경할 수 있습니다.

## 실행 방법

### 1) Redis 실행

```bash
redis-server
```

Redis Key Expiration 이벤트 리스너를 사용하므로, 로컬 Redis에 아래 설정이 필요합니다.

```bash
redis-cli config set notify-keyspace-events Ex
```

### 2) Backend 실행

```bash
cd queue-backend
./gradlew bootRun
```

기본 포트: `8080`

### 3) Frontend 실행

```bash
cd queue-frontend
npm install
npm run dev
```

기본 개발 서버: `http://localhost:5173`

## 사용자 흐름

1. `/`에서 "예매 시작" 클릭
2. 백엔드가 토큰 발급 후 상태 반환
3. `WAIT`이면 `/wait`에서 3초 간격으로 상태 폴링
4. `ENTER`가 되면 `/enter`로 자동 이동
5. "나가기" 클릭 시 퇴장 API 호출 후 초기 화면 이동

## API 요약

Base URL: `http://localhost:8080/api/entry`

### `POST /api/entry`

대기열 진입 토큰 발급

Response 예시:

```json
{
  "token": "uuid",
  "status": "WAIT",
  "rank": 12
}
```

### `GET /api/entry/check-status?token={token}`

토큰 상태 조회

Response 예시:

```json
{
  "status": "WAIT",
  "rank": 8
}
```

또는

```json
{
  "status": "ENTER"
}
```

또는

```json
{
  "status": "EXIT"
}
```

### `POST /api/entry/exit?token={token}`

입장 세션 종료(퇴장)

## 내부 동작 메모

- 대기열: Redis ZSet `reservation:queue`
- 토큰 상태: `reservation:token:{token}`
- 입장 집합: `reservation:entered`
- 활성 입장자 수: `reservation:activeCount`
- 승급 스케줄: 5초 간격 (`@Scheduled`)
- 최대 동시 입장 수: 10명 (`EntryService.MAX_ACTIVE`)
- 입장 토큰 TTL: 10초 (`EntryService.TOKEN_TTL`)

## 부하 테스트 (선택)

`queue-backend/load-test.js`는 k6 스크립트입니다.

```bash
cd queue-backend
k6 run load-test.js
```

## 참고

- 프론트엔드 API 베이스 URL은 `queue-frontend/src/api/queueApi.js`에 하드코딩되어 있습니다.
- 배포 환경에서는 CORS 및 API URL을 환경별 설정으로 분리하는 것을 권장합니다.
