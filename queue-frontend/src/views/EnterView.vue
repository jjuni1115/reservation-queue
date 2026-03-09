<script setup>
import { exit } from '@/api/queueApi.js'
import { useQueueStore } from '@/stores/queueStore'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const store = useQueueStore()
const router = useRouter()
const loading = ref(false)

const leave = async () => {
  loading.value = true
  try {
    await exit(store.token)
    store.clear()
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="enter-page page-enter">
    <div class="card stagger">
      <div class="icon">🎉</div>

      <h1>입장이 완료되었습니다</h1>
      <p class="desc">
        현재 예매 페이지에 정상적으로 접속 되었습니다.<br />
        예매를 마치면 반드시 나가기를 눌러주세요.
      </p>

      <div class="info">
        <span>세션 토큰</span>
        <code>{{ store.token }}</code>
      </div>

      <button
        class="btn btn-primary"
        :disabled="loading"
        @click="leave"
      >
        {{ loading ? '정리 중...' : '나가기' }}
      </button>
    </div>

    <div class="side-note stagger">
      <h2>예매 완료 체크</h2>
      <p>좌석 선택 후 결제까지 마무리하면 자동으로 예매가 확정됩니다.</p>
      <p class="small">나가기 버튼을 누르면 세션이 정리됩니다.</p>
    </div>
  </div>
</template>

<style scoped>
.enter-page {
  min-height: 100vh;
  padding: 56px clamp(24px, 6vw, 80px);
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 28px;
  align-items: center;
}

.card {
  background: rgba(248, 250, 252, 0.98);
  color: var(--ink-900);
  padding: 36px;
  border-radius: 20px;
  box-shadow: var(--shadow-strong);
  text-align: left;
}

.icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(249, 115, 22, 0.15);
  margin-bottom: 16px;
}

h1 {
  margin: 0 0 12px;
  font-size: clamp(28px, 3vw, 36px);
}

.desc {
  margin: 0 0 24px;
  color: var(--ink-500);
  font-size: 14px;
  line-height: 1.6;
}

.info {
  background: var(--paper-200);
  border-radius: 14px;
  padding: 16px;
  margin-bottom: 24px;
  display: grid;
  gap: 8px;
}

.info span {
  font-size: 12px;
  color: var(--ink-500);
}

.info code {
  font-family: inherit;
  font-size: 13px;
  word-break: break-all;
  color: var(--ink-900);
}

.side-note {
  background: rgba(15, 23, 42, 0.4);
  border-radius: 20px;
  padding: 28px;
  color: rgba(248, 250, 252, 0.92);
  border: 1px solid rgba(148, 163, 184, 0.2);
}

.side-note h2 {
  margin: 0 0 12px;
  font-size: 18px;
}

.side-note p {
  margin: 0 0 12px;
  font-size: 14px;
  color: rgba(248, 250, 252, 0.75);
}

.side-note p.small {
  font-size: 12px;
  margin: 0;
  color: rgba(248, 250, 252, 0.6);
}
</style>
