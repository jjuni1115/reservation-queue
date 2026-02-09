<script setup>
import {entry} from '@/api/queueApi'
import {useQueueStore} from '@/stores/queueStore'
import {useRouter} from 'vue-router'

const store = useQueueStore();
const router = useRouter();

const start = async () => {
  const res = await entry();
  console.log(res.data.token)

  store.setToken(res.data.token)

  if (res.data.status === 'ENTER') {
    router.push('/enter')
  } else {
    store.setStatus(res.data.token, res.data.rank)
    router.push('/wait')
  }
}

</script>

<template>
  <div class="entry-page page-enter">
    <div class="hero stagger">
      <div class="hero-badge">Reservation Queue</div>
      <h1>선착순 예매</h1>
      <p class="lead">
        좌석 확보를 위한 대기열에 안전하게 연결합니다.<br />
        순서가 되면 자동으로 예매 페이지로 이동합니다.
      </p>

      <div class="cta">
        <button class="btn btn-primary" @click="start">예매 시작</button>
        <span class="cta-note">대기 중 새로고침은 피해주세요.</span>
      </div>
    </div>

    <div class="panel stagger">
      <div class="panel-card">
        <h2>진행 방식</h2>
        <ul class="features">
          <li>접속 순서대로 대기열을 부여합니다.</li>
          <li>입장 순서가 되면 자동으로 이동합니다.</li>
          <li>세션 종료 시 나가기를 꼭 눌러주세요.</li>
        </ul>
      </div>
      <div class="panel-card highlight">
        <h2>현재 상태</h2>
        <p>
          실시간 대기열이 안정적으로 운영 중입니다.
          빠른 입장을 위해 지금 바로 시작하세요.
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.entry-page {
  min-height: 100vh;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 32px;
  align-items: center;
  padding: 56px clamp(24px, 6vw, 80px);
  color: var(--paper-100);
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  border-radius: 999px;
  background: rgba(248, 250, 252, 0.12);
  border: 1px solid rgba(248, 250, 252, 0.2);
  font-size: 12px;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  margin-bottom: 16px;
}

h1 {
  font-size: clamp(36px, 5vw, 56px);
  margin: 0 0 12px;
}

.lead {
  margin: 0 0 24px;
  color: rgba(248, 250, 252, 0.76);
  font-size: 16px;
}

.cta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.cta-note {
  font-size: 12px;
  color: rgba(248, 250, 252, 0.6);
}

.panel {
  display: grid;
  gap: 16px;
}

.panel-card {
  background: rgba(248, 250, 252, 0.95);
  color: var(--ink-900);
  border-radius: 20px;
  padding: 24px;
  box-shadow: var(--shadow-soft);
}

.panel-card h2 {
  margin: 0 0 12px;
  font-size: 18px;
}

.panel-card p {
  margin: 0;
  color: var(--ink-500);
  font-size: 14px;
}

.panel-card.highlight {
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.1), rgba(20, 184, 166, 0.12));
  border: 1px solid rgba(249, 115, 22, 0.25);
}

.features {
  margin: 0;
  padding: 0;
  list-style: none;
  display: grid;
  gap: 10px;
  font-size: 14px;
  color: var(--ink-500);
}

.features li::before {
  content: "•";
  margin-right: 8px;
  color: var(--accent-500);
}

@media (max-width: 720px) {
  .entry-page {
    padding: 40px 24px;
  }
}
</style>
