# Coffee App

![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-red.svg)
![Room](https://img.shields.io/badge/Cache-Room-7952B3.svg)

Coffee App 是一款使用 Kotlin 与 Jetpack Compose 构建的咖啡点单 / 电商示例应用。项目围绕“首页浏览商品、搜索筛选、详情查看、收藏、登录注册、购物车管理、个人设置”等核心流程展开，适合作为现代 Android 声明式 UI、MVVM 架构、网络请求与本地缓存实践项目。

## 项目亮点

- **Compose 声明式 UI**：使用 Material 3、Navigation Compose、HorizontalPager 等组件构建页面与交互。
- **MVVM + 单向数据流**：UI 只负责渲染状态，ViewModel 负责业务逻辑、异步任务与状态分发。
- **云端数据驱动**：商品、Banner、登录注册与购物车接口通过 Retrofit 访问 Supabase / Mock API。
- **Room 本地缓存**：商品与 Banner 会写入本地数据库，页面优先观察本地 Flow，刷新时再同步远端数据。
- **响应式状态管理**：使用 StateFlow 驱动首页、购物车、用户登录状态和语言切换。
- **网络图片加载与缓存**：使用 Coil 加载 Banner 与商品图片，并启用内存、磁盘、网络缓存策略。
- **账号会话持久化**：登录后通过 SharedPreferences 保存用户信息与 access token，重启后可恢复会话。

## 功能清单

| 模块 | 功能 |
| :--- | :--- |
| 欢迎页 | 首次进入引导用户进入登录 / 首页流程 |
| 登录注册 | 基于 Supabase Auth 的邮箱登录、注册、错误提示与会话保存 |
| 首页 | Banner 轮播、商品列表、分类筛选、关键字搜索、手动刷新 |
| 商品详情 | 展示商品图片、描述、价格，支持选择规格与温度 |
| 收藏 | 将商品加入 / 移出收藏列表，查看收藏商品 |
| 购物车 | 登录后加载购物车，支持添加商品、增减数量、删除商品与金额汇总 |
| 个人中心 | 展示登录状态、进入设置页、退出登录 |
| 设置 | 中英文语言切换、账号入口、基础设置项展示 |

## 技术栈

| 分类 | 技术 |
| :--- | :--- |
| 语言 | Kotlin 2.0.21 |
| 构建 | Gradle Kotlin DSL、Android Gradle Plugin 8.13.2 |
| UI | Jetpack Compose、Material 3、Compose BOM |
| 导航 | Navigation Compose |
| 异步 | Kotlin Coroutines、Flow、StateFlow |
| 网络 | Retrofit、OkHttp、OkHttp Logging Interceptor |
| 序列化 | Kotlinx Serialization |
| 图片 | Coil Compose |
| 本地存储 | Room、SharedPreferences |
| 架构 | MVVM、Repository、UDF |

## 架构说明

项目采用轻量分层架构，核心数据流如下：

```text
Compose UI
   |
   | collectAsState()
   v
ViewModel
   |
   | StateFlow / viewModelScope
   v
Repository
   |
   | refresh remote data / observe local data
   v
Room Database <---- Retrofit API ---- Supabase / Mock API
```

- **UI 层**：位于 `presentation`，负责页面布局、事件回调和状态展示。
- **ViewModel 层**：负责请求数据、处理用户操作、维护 UI 状态。
- **Repository 层**：统一协调网络数据与本地缓存，降低页面对数据来源的感知。
- **Data 层**：包含 Room DAO / Entity、Retrofit API、网络配置与会话存储。
- **Domain 层**：定义 Product、OrderItem、AuthResponse 等跨层传输模型。

## 目录结构

```text
Coffee/
├── app/
│   └── src/main/
│       ├── java/com/mrwang/coffeeapp/
│       │   ├── data/
│       │   │   ├── local/              # Room 数据库、DAO、Entity、用户会话存储
│       │   │   ├── network/            # Retrofit API、网络管理、Supabase 配置
│       │   │   └── repository/         # 数据仓库，负责远端同步与本地缓存
│       │   ├── domain/model/           # 业务模型与接口请求 / 响应模型
│       │   └── presentation/
│       │       ├── navigation/         # 路由定义与 NavHost
│       │       ├── screens/            # 首页、详情、购物车、收藏、登录、个人中心等页面
│       │       ├── settings/           # App 级设置状态
│       │       ├── theme/              # Compose 主题、颜色、字体
│       │       └── ui_components/      # 通用 UI 组件
│       └── res/                        # 图片、图标、主题与资源文件
├── gradle/libs.versions.toml           # 版本目录
├── build.gradle.kts                    # 根项目 Gradle 配置
└── settings.gradle.kts                 # 项目模块配置
```

## 快速开始

### 环境要求

- Android Studio Ladybug 或更新版本
- JDK 17
- Android SDK 36
- 设备或模拟器 Android 7.0(API 24) 及以上

### 运行项目

1. 克隆项目：

   ```bash
   git clone https://github.com/mrwangcoke/coffee.git
   cd coffee
   ```

2. 使用 Android Studio 打开项目，并等待 Gradle Sync 完成。

3. 运行 App：

   ```powershell
   .\gradlew.bat :app:assembleDebug
   ```

4. 在 Android Studio 中选择模拟器或真机，点击 Run 即可安装体验。

## 数据与接口说明

- Banner 数据通过 Mock API 获取，失败时首页会展示本地默认 Banner 图片。
- 商品数据通过 Supabase REST API 获取，并缓存到 Room 的 `coffee_app.db`。
- 购物车接口依赖登录后的用户 `access_token`，未登录时购物车页面会提示先登录。
- 登录注册使用 Supabase Auth，注册后如果后端开启邮箱验证，需要先完成邮箱验证再登录。
- 当前演示项目的后端地址与匿名访问配置位于 `data/network`，如需接入自己的 Supabase 项目，可修改 `NetworkManager` 与 `SupabaseConfig`。

## 常用开发命令

```powershell
# 构建 Debug 包
.\gradlew.bat :app:assembleDebug

# 运行单元测试
.\gradlew.bat test

# 运行 Android Instrumented Test
.\gradlew.bat connectedAndroidTest
```

## 当前实现说明

- 商品与 Banner 已接入 Room 缓存，刷新失败时页面仍可展示已有缓存数据。
- 收藏列表目前保存在运行时 ViewModel 状态中，App 进程被系统回收后不会持久保存。
- 语言切换目前由 AppSettingsViewModel 管理，适合演示状态共享；如需长期保存，可接入 DataStore。
- 网络日志拦截器当前为 BODY 级别，便于调试接口；发布版本建议降低日志级别或按 BuildConfig 控制。

## 后续规划

- 使用 DataStore 持久化语言偏好、主题偏好与其他设置。
- 将收藏列表持久化到本地数据库或远端用户表。
- 为购物车和订单增加结算、地址、支付状态等完整链路。
- 补充 Repository、ViewModel 与 UI 组件测试。
- 抽离后端配置，避免环境信息直接写在源码中。

## 维护信息

- 项目负责人：MrWang
- 包名：`com.mrwang.coffeeapp`
- 最低支持版本：Android 7.0(API 24)
- 当前版本：`1.0`
