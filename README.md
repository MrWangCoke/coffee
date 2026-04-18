# ☕ Coffee App：基于 Jetpack Compose 的现代响应式电商实战

![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg) ![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg) ![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg) ![Architecture](https://img.shields.io/badge/Architecture-MVVM-red.svg)

## 📋 项目简介
本项目是一个采用现代 Android 开发技术栈（Kotlin + Compose + MVVM）构建的高性能咖啡电商应用。项目通过从本地静态资源驱动向 **云端数据驱动 (Data-Driven)** 的演进，深度实践了响应式编程、异步网络处理及组件化设计规范。

---

## ✨ 核心特性

### 1. 响应式架构与状态管理
* **MVVM 深度实践**：严格遵循单向数据流 (UDF) 模式，利用 `ViewModel` 隔离 UI 逻辑与业务数据。
* **响应式状态流**：使用 `StateFlow` 实现 UI 状态的精准分发，确保界面实时反映底层数据变化。
* **生命周期感知**：结合 `viewModelScope` 自动管理协程生命周期，彻底杜绝内存泄漏。

### 2. 动态 Banner 系统 (今日核心更新)
* **异步网络轮播**：集成 `HorizontalPager` 实现丝滑的 Banner 切换，数据完全由云端 API 动态配置。
* **智能图片处理**：引入 **Coil** 库处理网络图片，内置多级缓存机制、占位图显示及失败兜底保护。
* **交互优化**：通过 `LaunchedEffect` 与协程 `delay` 实现智能自动轮播，并在用户手动干预时自动重置计时。

### 3. 企业级网络封装
* **Retrofit 接口设计**：将 HTTP 请求抽象为干净的 Kotlin 接口，利用 `suspend` 关键字实现非阻塞式调用。
* **全链路日志监控**：集成 `OkHttp Logging Interceptor`，在调试阶段实时监控请求耗时、状态码及 JSON Payload。
* **现代序列化**：舍弃传统的 Gson，采用 **Kotlinx Serialization**，提供更安全的 Kotlin 类型检查与更好的性能。

---

## 🏗️ 架构设计图示

项目采用分层架构，确保代码的可测试性与可扩展性：



* **Presentation (UI 层)**：负责渲染 `Compose` 组件，不持有任何业务逻辑。
* **ViewModel (引擎层)**：负责数据请求、状态转换与“广播” (StateFlow)。
* **Data (数据层)**：包含 `NetworkManager` (Retrofit) 与 `API` 接口，负责与后端交互。

---

## 🛠️ 技术栈详情

| 维度 | 技术选型 | 说明 |
| :--- | :--- | :--- |
| **语言** | **Kotlin 2.x** | 全面使用协程、扩展函数等现代语法 |
| **UI** | **Jetpack Compose** | 声明式组件化开发，支持 Material 3 设计规范 |
| **异步** | **Coroutines & Flow** | 优雅解决网络请求与耗时任务的线程切换 |
| **网络** | **Retrofit + OkHttp** | 工业级网络访问框架，支持日志拦截与连接池管理 |
| **图片** | **Coil (Kotlin-first)** | 深度集成协程的轻量级图片加载引擎 |
| **架构** | **MVVM + UDF** | 解耦 UI 与数据，提升代码稳定性与可维护性 |

---

## 📂 核心目录结构

```text
com.mrwang.coffeeapp/
├── data/                    # 数据层：网络配置、API 接口定义
│   └── network/             # Retrofit 实例化与拦截器配置
├── domain/                  # 领域层：跨层传输的 POJO/Data Class
│   └── model/               # 核心业务模型 (支持序列化)
└── presentation/            # 展示层：UI 逻辑
    ├── screens/             # 功能页面 (Home, Details, Cart 等)
    │   └── homescreen/      # 首页模块 (View + ViewModel)
    ├── navigation/          # 路由管理 (NavGraph)
    └── ui_components/       # 跨页面复用的原子级 UI 组件
```

---

## 🚀 快速启动

1.  **克隆仓库**：
    ```bash
    git clone https://github.com/mrwangcoke/coffee.git
    ```
2.  **环境配置**：
    * 使用最新版 Android Studio (Ladybug 或更新)。
    * Gradle JDK 设置为 17。
3.  **运行项目**：
    * App 启动后会通过 `fetchBanners()` 自动向云端拉取图片。
    * 若处于无网环境，App 将自动触发 **ErrorBoundary** 机制，展示本地兜底资源。

---

## 🤝 维护与贡献
* **主要负责人**：MrWang
* **版本说明**：项目目前处于 `V1.2-DynamicData` 阶段，后续将推进“底部商品列表”与“购物车本地持久化 (Room)”的开发。
* **提交规范**：遵循语义化提交 (Conventional Commits)，如 `feat: add banner network loading`。

