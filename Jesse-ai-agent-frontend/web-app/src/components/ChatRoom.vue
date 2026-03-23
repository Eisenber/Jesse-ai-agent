<template>
  <div class="chat-page">
    <div class="lain-bg" aria-hidden="true">
      <img class="lain-stamp c1" :src="lain1" alt="" />
      <img class="lain-stamp c2" :src="lain2" alt="" />
      <img class="lain-stamp c3" :src="lain3" alt="" />
      <img class="lain-stamp c4" :src="lain4" alt="" />
    </div>

    <div class="chat-topbar">
      <div class="chat-topbar-left">
        <div class="chat-title">{{ title }}</div>
        <div v-if="chatId" class="chat-id">
          当前会话：<span class="chat-id-value">{{ chatId }}</span>
        </div>
      </div>

      <router-link class="back-link" to="/">返回主页</router-link>
    </div>

    <div class="chat-body" ref="bodyRef">
      <div v-if="messages.length === 0" class="empty-tip">
        请输入内容，开始和 AI 对话。
      </div>

      <div v-for="m in messages" :key="m.id" class="row" :class="{ 'row-ai': m.role === 'ai', 'row-user': m.role === 'user' }">
        <div class="bubble" :class="{ 'bubble-ai': m.role === 'ai', 'bubble-user': m.role === 'user' }">
          <div class="bubble-text" v-text="m.content"></div>
        </div>
      </div>
    </div>

    <div class="chat-inputbar">
      <textarea
        v-model="draft"
        class="input"
        :disabled="streaming"
        rows="2"
        placeholder="输入消息，然后回车发送"
        @keydown="handleKeydown"
      />

      <button class="send-btn" :disabled="streaming || !draft.trim()" @click="submit">
        {{ streaming ? '生成中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { nextTick, ref, watch } from 'vue'
import lain1 from '../assets/lain1.png'
import lain2 from '../assets/lain2.png'
import lain3 from '../assets/lain3.png'
import lain4 from '../assets/lain4.png'

const props = defineProps({
  title: { type: String, default: '' },
  chatId: { type: String, default: '' },
  messages: { type: Array, default: () => [] },
  streaming: { type: Boolean, default: false }
})

const emit = defineEmits(['send'])

const draft = ref('')
const bodyRef = ref(null)

function submit() {
  const text = draft.value.trim()
  if (!text || props.streaming) return
  emit('send', text)
  draft.value = ''
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    submit()
  }
}

watch(
  () => props.messages,
  async () => {
    await nextTick()
    if (!bodyRef.value) return
    bodyRef.value.scrollTop = bodyRef.value.scrollHeight
  },
  { deep: true }
)
</script>

<style scoped>
.chat-page {
  min-height: 100svh;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  background: transparent;
  color: var(--text);
  position: relative;
}

.chat-topbar,
.chat-body,
.chat-inputbar {
  position: relative;
  z-index: 1;
}

.lain-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: visible;
}

.lain-stamp {
  position: absolute;
  image-rendering: pixelated;
  opacity: var(--lain-o, 0.20);
  mix-blend-mode: lighten;
  filter: saturate(0.75) contrast(1.85) brightness(1.08) hue-rotate(160deg);
  transform: translateZ(0);
  animation: lainFlicker 4.5s steps(2) infinite;
}

.c1 {
  left: 2%;
  top: 80px;
  width: clamp(220px, 26vw, 330px);
  --lain-o: 0.30;
  animation-delay: -0.3s;
}

.c2 {
  right: 2%;
  top: 150px;
  width: clamp(220px, 24vw, 330px);
  --lain-o: 0.14;
  animation-delay: -1.2s;
}

.c3 {
  left: 50%;
  bottom: 20px;
  width: clamp(280px, 46vw, 640px);
  transform: translateX(-50%);
  --lain-o: 0.22;
  animation-delay: -0.9s;
}

.c4 {
  left: 52%;
  top: 210px;
  right: auto;
  bottom: auto;
  width: clamp(200px, 24vw, 360px);
  transform: translateX(-50%);
  --lain-o: 0.10;
  animation-delay: -2.2s;
}

.chat-topbar {
  padding: 18px 18px 12px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  border-bottom: 1px dashed var(--divider);
  box-shadow: inset 0 -2px 0 rgba(46, 252, 255, 0.18);
}

.chat-topbar-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.chat-title {
  font-size: 20px;
  font-weight: 900;
  letter-spacing: -0.2px;
}

.chat-id {
  color: var(--muted);
  font-size: 13px;
  font-family: var(--mono);
}

.chat-id-value {
  font-family: var(--mono);
  background:
    repeating-linear-gradient(
      0deg,
      rgba(46, 252, 255, 0.06) 0px,
      rgba(46, 252, 255, 0.06) 1px,
      transparent 1px,
      transparent 3px
    ),
    var(--pill-bg);
  padding: 2px 8px;
  border-radius: 0;
  border: 4px solid var(--surface-border);
  box-shadow:
    inset 4px 4px 0 rgba(46, 252, 255, 0.10),
    inset -4px -4px 0 rgba(46, 252, 255, 0.05);
}

.back-link {
  color: var(--text-h);
  text-decoration: none;
  border: 4px solid var(--surface-border);
  border-radius: 0;
  padding: 8px 12px;
  background:
    repeating-linear-gradient(
      90deg,
      rgba(46, 252, 255, 0.06) 0px,
      rgba(46, 252, 255, 0.06) 1px,
      transparent 1px,
      transparent 4px
    ),
    var(--page-surface-a);
  box-shadow:
    inset 4px 4px 0 rgba(46, 252, 255, 0.10),
    inset -4px -4px 0 rgba(46, 252, 255, 0.05);
}

.chat-body {
  flex: 1;
  overflow: auto;
  padding: 10px 16px 18px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.empty-tip {
  margin-top: 30px;
  color: var(--muted-2);
  text-align: center;
}

.row {
  display: flex;
}

.row-user {
  justify-content: flex-end;
}

.row-ai {
  justify-content: flex-start;
}

.bubble {
  max-width: min(720px, 86%);
  border-radius: 0;
  padding: 10px 12px;
  border: 4px solid var(--surface-border);
  box-shadow:
    inset 4px 4px 0 rgba(46, 252, 255, 0.10),
    inset -4px -4px 0 rgba(46, 252, 255, 0.05);
  white-space: pre-wrap;
  word-break: break-word;
}

.bubble-user {
  background:
    repeating-linear-gradient(
      0deg,
      rgba(46, 252, 255, 0.07) 0px,
      rgba(46, 252, 255, 0.07) 1px,
      transparent 1px,
      transparent 4px
    ),
    linear-gradient(135deg, var(--bubble-user-a), var(--bubble-user-b));
  border-color: var(--bubble-user-border);
}

.bubble-ai {
  background:
    repeating-linear-gradient(
      0deg,
      rgba(46, 252, 255, 0.05) 0px,
      rgba(46, 252, 255, 0.05) 1px,
      transparent 1px,
      transparent 4px
    ),
    var(--page-surface-b);
}

.bubble-text {
  line-height: 1.6;
  font-family: var(--mono);
  text-shadow: 0 0 10px rgba(46, 252, 255, 0.05);
}

.chat-inputbar {
  padding: 14px 16px 20px;
  border-top: 1px solid var(--divider);
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 12px;
  background:
    repeating-linear-gradient(
      90deg,
      rgba(46, 252, 255, 0.05) 0px,
      rgba(46, 252, 255, 0.05) 1px,
      transparent 1px,
      transparent 4px
    ),
    var(--page-surface-a);
  backdrop-filter: blur(0px);
}

.input {
  width: 100%;
  resize: none;
  border-radius: 0;
  padding: 10px 12px;
  border: 4px solid var(--surface-border);
  background:
    repeating-linear-gradient(
      0deg,
      rgba(46, 252, 255, 0.04) 0px,
      rgba(46, 252, 255, 0.04) 1px,
      transparent 1px,
      transparent 4px
    ),
    var(--input-bg);
  color: var(--text-h);
  caret-color: var(--accent);
  font-size: 14px;
  outline: none;
  height: 56px;
  box-sizing: border-box;
  font-family: var(--mono);
  text-shadow: 0 0 18px rgba(46, 252, 255, 0.12);
}

.input::placeholder {
  color: var(--muted-2);
}

.input:focus {
  border-color: var(--accent-border);
  box-shadow: 0 0 0 6px rgba(46, 252, 255, 0.16);
}

.send-btn {
  min-width: 110px;
  border-radius: 0;
  border: 4px solid var(--surface-border);
  background:
    repeating-linear-gradient(
      90deg,
      rgba(46, 252, 255, 0.10) 0px,
      rgba(46, 252, 255, 0.10) 1px,
      transparent 1px,
      transparent 4px
    );
  color: var(--text-h);
  font-weight: 900;
  padding: 0 16px;
  height: 56px;
  cursor: pointer;
  box-shadow: none;
  text-shadow: 0 0 18px rgba(46, 252, 255, 0.15);
}

.send-btn:disabled {
  cursor: not-allowed;
  background: rgba(0, 0, 0, 0.25);
  box-shadow: none;
  color: var(--muted-2);
  text-shadow: none;
}

@keyframes lainFlicker {
  0% {
    filter: saturate(0.75) contrast(1.85) brightness(1.08) hue-rotate(160deg);
    transform: translateZ(0) translate(0px, 0px);
  }
  45% {
    filter: saturate(0.98) contrast(2.25) brightness(1.12) hue-rotate(160deg);
    transform: translateZ(0) translate(0.8px, -0.5px);
  }
  55% {
    filter: saturate(0.64) contrast(1.7) brightness(1.02) hue-rotate(160deg);
    transform: translateZ(0) translate(-0.5px, 0.4px);
  }
  100% {
    filter: saturate(0.75) contrast(1.85) brightness(1.08) hue-rotate(160deg);
    transform: translateZ(0) translate(0px, 0px);
  }
}
</style>

