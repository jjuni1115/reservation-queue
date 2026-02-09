<script setup>
import { queueStatus } from '@/api/queueApi'
import { useQueueStore } from '@/stores/queueStore'
import { useRouter } from 'vue-router'
import { onMounted, onUnmounted, ref } from 'vue'

const store = useQueueStore()
const router = useRouter()

let timer
const loading = ref(true)

const poll = async () => {
  if (!store.token) {
    router.push('/')
    return
  }

  const res = await queueStatus(store.token)

  store.setStatus(res.data.status, res.data.rank)
  loading.value = false

  if (res.data.status === 'ENTER') {
    router.push('/enter')
  }

  if (res.data.status === 'EXIT') {
    store.clear()
    router.push('/')
  }
}

onMounted(() => {
  poll()
  timer = window.setInterval(poll, 3000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<template>
  <div class="wait-page page-enter">
    <div class="card stagger">
      <div class="spinner"></div>

      <h2>접속자가 많습니다</h2>

      <p class="desc">
        현재 대기열에 있으며<br />
        순서가 되면 자동으로 입장됩니다.
      </p>

      <div class="rank-box">
        <span>내 대기 순번</span>
        <strong v-if="!loading">{{ store.rank }}</strong>
        <strong v-else>…</strong>
      </div>

      <p class="hint">
        새로고침하지 말고 잠시만 기다려주세요.
      </p>
    </div>
  </div>
</template>

<style scoped>
.wait-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 48px 24px;
}

.card {
  width: min(380px, 100%);
  background: rgba(248, 250, 252, 0.98);
  color: var(--ink-900);
  padding: 36px;
  border-radius: 22px;
  text-align: center;
  box-shadow: var(--shadow-strong);
}

.spinner {
  width: 44px;
  height: 44px;
  border: 4px solid rgba(15, 23, 42, 0.12);
  border-top: 4px solid var(--accent-500);
  border-radius: 50%;
  margin: 0 auto 20px;
  animation: spin 1s linear infinite;
}

h2 {
  font-size: 20px;
  margin: 0 0 8px;
}

.desc {
  font-size: 14px;
  color: var(--ink-500);
  margin: 0 0 24px;
  line-height: 1.6;
}

.rank-box {
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.12), rgba(20, 184, 166, 0.12));
  padding: 16px;
  border-radius: 16px;
  margin-bottom: 16px;
  border: 1px solid rgba(15, 23, 42, 0.08);
}

.rank-box span {
  display: block;
  font-size: 12px;
  color: var(--ink-500);
}

.rank-box strong {
  font-size: 28px;
  color: var(--ink-900);
}

.hint {
  font-size: 12px;
  color: var(--ink-500);
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
