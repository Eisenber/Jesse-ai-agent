<template>
  <div class="home">
    <div class="lain-bg" aria-hidden="true">
      <img class="lain-stamp s1" :src="lain1" alt="" />
      <img class="lain-stamp s2" :src="lain2" alt="" />
      <img class="lain-stamp s3" :src="lain3" alt="" />
      <img class="lain-stamp s4" :src="lain4" alt="" />
    </div>

    <div class="home-content">
    <header class="home-header">
      <h1 class="home-title">AI 应用选择</h1>
      <p class="home-subtitle">选择一个应用进入聊天室</p>
    </header>

    <div class="card-grid">
      <router-link class="card" to="/love">
        <div class="card-title">AI 恋爱大师</div>
        <div class="card-desc">SSE 实时聊天（自动生成 chatId）</div>
      </router-link>

      <router-link class="card" to="/manus">
        <div class="card-title">AI 超级智能体</div>
        <div class="card-desc">SSE 实时聊天（调用 doChatWithManus）</div>
      </router-link>
    </div>
    </div>
  </div>
</template>

<script setup>
import lain1 from '../assets/lain1.png'
import lain2 from '../assets/lain2.png'
import lain3 from '../assets/lain3.png'
import lain4 from '../assets/lain4.png'
</script>

<style scoped>
.home {
  min-height: 100svh;
  display: flex;
  flex-direction: column;
  padding: 28px 20px;
  box-sizing: border-box;
  background: transparent;
  color: var(--text);
  position: relative;
}

.home-content {
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
  opacity: var(--lain-o, 0.22);
  mix-blend-mode: lighten;
  filter: saturate(0.75) contrast(1.8) brightness(1.08) hue-rotate(160deg);
  transform: translateZ(0);
  animation: lainFlicker 4.5s steps(2) infinite;
}

.s1 {
  left: 6%;
  top: 110px;
  width: clamp(220px, 26vw, 360px);
  --lain-o: 0.30;
  animation-delay: -0.2s;
}

.s2 {
  right: 6%;
  top: 210px;
  width: clamp(220px, 26vw, 360px);
  --lain-o: 0.16;
  animation-delay: -1.1s;
}

.s3 {
  left: 50%;
  top: auto;
  bottom: 30px;
  transform: translateX(-50%);
  width: clamp(260px, 34vw, 520px);
  --lain-o: 0.22;
  animation-delay: -0.7s;
}

.s4 {
  left: 52%;
  right: auto;
  top: 260px;
  bottom: auto;
  width: clamp(180px, 22vw, 320px);
  --lain-o: 0.10;
  transform: translateX(-50%);
  animation-delay: -2.0s;
}

.home-header {
  max-width: 860px;
  margin: 0 auto;
  width: 100%;
}

.home-title {
  margin: 0;
  font-size: 34px;
  letter-spacing: -0.3px;
  font-weight: 900;
  font-family: var(--mono);
  text-shadow: 0 0 18px rgba(46, 252, 255, 0.14);
}

.home-subtitle {
  margin: 10px 0 0;
  color: var(--muted-2);
}

.card-grid {
  width: 100%;
  max-width: 860px;
  margin: 24px auto 0;
  display: grid;
  gap: 16px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

@media (max-width: 720px) {
  .card-grid {
    grid-template-columns: 1fr;
  }
}

.card {
  text-decoration: none;
  border-radius: 0;
  border: 4px solid var(--surface-border);
  background:
    repeating-linear-gradient(
      0deg,
      rgba(46, 252, 255, 0.08) 0px,
      rgba(46, 252, 255, 0.08) 1px,
      transparent 1px,
      transparent 2px
    ),
    var(--page-surface-a);
  backdrop-filter: none;
  padding: 18px 16px;
  transition: transform 0.12s ease, border-color 0.12s ease;
  box-shadow:
    inset 0 0 0 2px rgba(46, 252, 255, 0.16),
    inset 4px 4px 0 rgba(46, 252, 255, 0.08),
    inset -4px -4px 0 rgba(46, 252, 255, 0.06);
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
}

.card:hover {
  transform: translateY(-2px);
  border-color: var(--accent-border);
}

.card-title {
  font-size: 20px;
  font-weight: 800;
  font-family: var(--mono);
}

.card-desc {
  color: var(--muted);
  line-height: 1.35;
}

@keyframes lainFlicker {
  0% {
    filter: saturate(0.75) contrast(1.8) brightness(1.08) hue-rotate(160deg);
    transform: translateZ(0) translate(0px, 0px);
  }
  45% {
    filter: saturate(0.95) contrast(2.2) brightness(1.12) hue-rotate(160deg);
    transform: translateZ(0) translate(0.6px, -0.4px);
  }
  55% {
    filter: saturate(0.65) contrast(1.7) brightness(1.02) hue-rotate(160deg);
    transform: translateZ(0) translate(-0.4px, 0.3px);
  }
  100% {
    filter: saturate(0.75) contrast(1.8) brightness(1.08) hue-rotate(160deg);
    transform: translateZ(0) translate(0px, 0px);
  }
}
</style>

