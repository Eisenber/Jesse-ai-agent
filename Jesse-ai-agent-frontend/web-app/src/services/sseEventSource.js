export function requestSseWithEventSource({
  url,
  onMessage,
  onError,
  onEventSource
}) {
  return new Promise((resolve, reject) => {
    const es = new EventSource(url)
    onEventSource?.(es)

    es.onmessage = (evt) => {
      onMessage?.(evt.data)
    }

    es.onerror = (err) => {
      // SSE 连接结束（或网络错误）时会触发。EventSource 没有明确的 onclose，
      // 因此在这里关闭并结束 Promise。
      try {
        es.close()
      } catch {
        // ignore
      }
      onError?.(err)
      resolve()
    }

    // 极端情况下：浏览器可能直接拒绝连接，这时 reject。
    es.onopen = () => {
      // no-op
    }

    // 安全兜底：让 Promise 不会永远挂起（可按需调整）
    // 10 分钟后强制关闭并 resolve。
    setTimeout(() => {
      try {
        es.close()
      } catch {
        // ignore
      }
      resolve()
    }, 10 * 60 * 1000)

    // 由于 EventSource 不提供 abort 机制，这里不处理 reject（交给 onerror 收尾）。
  })
}

