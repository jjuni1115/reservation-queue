<script setup>
import { exit } from '@/api/queueApi'
import { useQueueStore } from '@/stores/queueStore'
import {useRouter} from 'vue-router'

const store = useQueueStore()
const router = useRouter()

const leave = async () => {
  await exit(store.token)
  store.clear()
  router.push("/")

}
</script>

<template>
  <h1>🎉 입장 성공</h1>
  <button @click="leave">나가기</button>
</template>