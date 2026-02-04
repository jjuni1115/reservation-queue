<script setup>

import {queueStatus} from '@/api/queueApi'
import {useQueueStore} from '@/stores/queueStore'
import {useRouter} from 'vue-router'
import {onMounted, onUnmounted} from "vue";

const store = useQueueStore();
const router = useRouter();

let timer;

const poll = async () => {
  console.log(store.token)
  const res = await queueStatus(store.token)
  store.setStatus(res.data.status, res.data.rank)

  if (res.status === 'ENTER') {
    router.push('/enter')
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


  <h2>대기 중...</h2>
  <p>현재 순번: {{ store.rank }}</p>


</template>

<style scoped>

</style>