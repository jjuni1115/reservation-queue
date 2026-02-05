<script setup>
import { exit } from '@/api/queueApi'
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
  <div class="container">
    <div class="card">
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
          class="exit-btn"
          :disabled="loading"
          @click="leave"
      >
        {{ loading ? '정리 중...' : '나가기' }}
      </button>
    </div>
  </div>
</template>