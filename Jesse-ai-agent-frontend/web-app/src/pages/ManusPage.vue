<template>
  <ChatRoom
    title="AI 超级智能体"
    :messages="messages"
    :streaming="streaming"
    @send="handleSend"
  />
</template>

<script setup>
import { onBeforeUnmount, ref } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { requestSseWithEventSource } from '../services/sseEventSource'

const messages = ref([])
const streaming = ref(false)

let eventSource = null

function newId() {
  if (crypto?.randomUUID) return crypto.randomUUID()
  return `id_${Math.random().toString(16).slice(2)}_${Date.now()}`
}

function pushMessage(role, content) {
  messages.value.push({ id: newId(), role, content })
}

onBeforeUnmount(() => {
  eventSource?.close()
  eventSource = null
})

async function handleSend(text) {
  if (streaming.value) return
  if (!text.trim()) return

  pushMessage('user', text)

  const aiIndex = messages.value.length
  messages.value.push({ id: newId(), role: 'ai', content: '' })

  streaming.value = true

  try {
    const url = `/api/ai/manus/chat?message=${encodeURIComponent(text)}`

    await requestSseWithEventSource({
      url,
      onMessage: (evtText) => {
        const msg = messages.value[aiIndex]
        if (!msg) return
        msg.content += evtText
      },
      onEventSource: (es) => {
        eventSource = es
      },
      onError: () => {}
    })
  } catch (e) {
    const msg = messages.value[aiIndex]
    if (msg) msg.content += '\n[流式请求失败]'
  } finally {
    streaming.value = false
    eventSource?.close()
    eventSource = null
  }
}
</script>

