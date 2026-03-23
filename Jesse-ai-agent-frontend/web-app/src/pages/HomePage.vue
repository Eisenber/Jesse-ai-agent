<template>
  <div class="home">
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
</template>

<style scoped>
.home {
  min-height: 100svh;
  display: flex;
  flex-direction: column;
  padding: 28px 20px;
  box-sizing: border-box;
  background: transparent;
  color: var(--text);
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
</style>

