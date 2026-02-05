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
  <div class="container">
    <div class="card">
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
        새로고침하지 말고 잠시만 기다려주세요 🙏
      </p>
    </div>
  </div>
</template>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a, #1e293b);
  display: flex;
  align-items: center;
  justify-content: center;
}

.card {
  background: #ffffff;
  width: 360px;
  padding: 36px;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e5e7eb;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  margin: 0 auto 20px;
  animation: spin 1s linear infinite;
}

h2 {
  font-size: 20px;
  margin-bottom: 8px;
}

.desc {
  font-size: 14px;
  color: #555;
  margin-bottom: 24px;
  line-height: 1.5;
}

.rank-box {
  background: #f1f5f9;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.rank-box span {
  display: block;
  font-size: 12px;
  color: #666;
}

.rank-box strong {
  font-size: 28px;
  color: #0f172a;
}

.hint {
  font-size: 12px;
  color: #777;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>