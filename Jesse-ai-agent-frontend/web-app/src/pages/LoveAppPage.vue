<template>
  <ChatRoom
    title="AI 恋爱大师"
    :chatId="chatId"
    :messages="messages"
    :streaming="streaming"
    @send="handleSend"
  />
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { requestSseWithEventSource } from '../services/sseEventSource'

const chatId = ref('')
const messages = ref([])
const streaming = ref(false)

let eventSource = null

function newId() {
  if (crypto?.randomUUID) return crypto.randomUUID()
  // Fallback
  return `id_${Math.random().toString(16).slice(2)}_${Date.now()}`
}

function pushMessage(role, content) {
  messages.value.push({ id: newId(), role, content })
}

onMounted(() => {
  chatId.value = newId()
})

onBeforeUnmount(() => {
  eventSource?.close()
  eventSource = null
})

async function handleSend(text) {
  if (streaming.value) return
  if (!text.trim()) return
  if (!chatId.value) return

  pushMessage('user', text)

  const aiIndex = messages.value.length
  messages.value.push({ id: newId(), role: 'ai', content: '' })

  streaming.value = true

  try {
    const url =
      `/api/ai/love_app/chat/sse?message=${encodeURIComponent(text)}&chatId=${encodeURIComponent(
        chatId.value
      )}`

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

