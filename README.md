# Jesse-ai-agent

AI智能体助手

## 简介

**Jesse-ai-agent** 是一个基于 Java 与 Vue 的 AI 智能体助手，旨在为用户提供高效、便捷的人工智能服务。支持自然语言交互、智能任务处理等多项功能，适用于个人效率提升和智能办公等场景。

## 项目特性

- 基于 Java，后端性能可靠
- 前端采用 Vue，界面友好美观
- 多用户支持及权限管理
- 支持智能插件扩展及 API 对接
- 支持自然语言处理与任务分配

## 技术栈

- **后端**：Java（约 73%）
- **前端**：Vue（约 16%）、JavaScript、CSS
- **其他**：可扩展插件

## 安装与使用说明

### 环境要求

- JDK 21 或以上
- Node.js (建议 v18+)
- npm / yarn

### 克隆仓库

```bash
git clone https://github.com/Eisenber/Jesse-ai-agent.git
cd Jesse-ai-agent
```

### 后端启动

```bash
# 进入后端目录（如果有）
cd backend
# 构建并运行
./mvnw clean package
java -jar target/*.jar
```

### 前端启动

```bash
# 进入前端目录
cd ../frontend
# 安装依赖
npm install
# 启动前端服务
npm run serve
```
> 若子目录结构有差异，请据实际目录进行调整。

## 访问方式

- 后端接口默认地址：`http://localhost:8080`
- 前端页面默认地址：`http://localhost:3000`（或终端输出端口）

## 目录结构参考

```
Jesse-ai-agent/
├── backend/      # Java 后端源码
├── frontend/     # Vue 前端源码
├── README.md
└── ...
```

## 贡献与反馈

欢迎提交 Issue 或 PR 参与项目改进！

## 许可证

本项目采用 MIT License，详见 LICENSE 文件。
