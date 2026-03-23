import api from './api'

function parseSseBlock(block) {
  // SSE block is separated by blank line (\n\n).
  const normalized = block.replace(/\r\n/g, '\n').trim()
  if (!normalized) return null

  const lines = normalized
    .split('\n')
    .map((l) => l.trimEnd())
    .filter((l) => l.trim() !== '')

  const hasDataPrefix = lines.some((l) => l.startsWith('data:'))
  if (!hasDataPrefix) {
    // If there is no `data:` prefix, treat it as plain text only when it
    // looks like actual content (not SSE meta fields like `id:` / `event:`).
    if (lines.some((l) => /^((id)|(event)|(retry)):/.test(l))) return null
    return normalized
  }

  const dataLines = []
  for (const line of lines) {
    if (!line.startsWith('data:')) continue
    dataLines.push(line.slice(5).replace(/^ /, ''))
  }

  if (dataLines.length === 0) return null
  return dataLines.join('\n')
}

export async function requestSseWithAxios({
  url,
  params,
  signal,
  onEvent
}) {
  let buffer = ''
  let lastLen = 0

  const req = api.get(url, {
    params,
    headers: { Accept: 'text/event-stream' },
    signal,
    responseType: 'text',
    onDownloadProgress: (e) => {
      const xhr = e?.currentTarget
      if (!xhr) return

      const responseText = xhr.responseText || ''
      if (responseText.length <= lastLen) return

      const chunk = responseText.substring(lastLen)
      lastLen = responseText.length

      if (!chunk) return
      buffer += chunk.replace(/\r\n/g, '\n')

      const parts = buffer.split('\n\n')
      buffer = parts.pop() ?? ''

      for (const part of parts) {
        const evtText = parseSseBlock(part)
        if (evtText !== null) onEvent(evtText)
      }
    }
  })

  try {
    await req
  } finally {
    const remaining = buffer.trim()
    if (remaining) {
      const evtText = parseSseBlock(remaining)
      if (evtText !== null) onEvent(evtText)
    }
  }
}

