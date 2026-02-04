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

  <h1>선착순 예매</h1>
  <button @click="start">예매시작</button>

</template>

<style scoped>

</style>