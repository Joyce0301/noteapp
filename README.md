# 📝 NoteApp - 简洁的 Android 备忘录应用

一个基于 Android + Room 数据库的轻量级备忘录应用，支持快速记录、编辑和管理日常备忘。

![Android](https://img.shields.io/badge/Android-24+-green.svg)
![SDK](https://img.shields.io/badge/SDK-36-blue.svg)
![Room](https://img.shields.io/badge/Room-2.6.1-orange.svg)
![Java](https://img.shields.io/badge/Java-11-red.svg)

## ✨ 功能特性

- 📝 **快速记录** - 简洁的输入界面，一键添加备忘
- 🔄 **实时编辑** - 点击任意条目即可编辑内容
- 🗑️ **长按删除** - 长按条目弹出删除确认
- 📅 **时间戳** - 自动记录创建和更新时间
- 📱 **Material Design** - 简洁现代的 UI 设计
- 💾 **本地存储** - 使用 Room 数据库，数据持久化安全

## 🏗️ 技术架构

```
┌─────────────────────────────────────────┐
│           MainActivity                  │
│  (UI 层 - 列表展示 + 用户交互)           │
├─────────────────────────────────────────┤
│           NoteAdapter                   │
│  (适配器 - 自定义列表项显示)             │
├─────────────────────────────────────────┤
│           NoteDao                       │
│  (数据访问层 - 增删改查接口)             │
├─────────────────────────────────────────┤
│        NoteDatabase (Room)              │
│  (数据库层 - SQLite 持久化)              │
├─────────────────────────────────────────┤
│           Note Entity                   │
│  (数据模型 - 备忘数据结构)               │
└─────────────────────────────────────────┘
```

### 核心依赖

```kotlin
// AndroidX
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.activity:activity:1.8.2")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
annotationProcessor("androidx.room:room-compiler:2.6.1")
```

## 📦 项目结构

```
noteapp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/noteapp/
│   │   │   ├── MainActivity.java      # 主界面 Activity
│   │   │   ├── AboutActivity.java     # 关于页面
│   │   │   ├── Note.java              # 数据模型（Entity）
│   │   │   ├── NoteDao.java           # 数据访问接口
│   │   │   ├── NoteDatabase.java      # Room 数据库
│   │   │   └── NoteAdapter.java       # 列表适配器
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml  # 主界面布局
│   │   │   │   └── activity_about.xml # 关于页面布局
│   │   │   └── values/
│   │   │       └── strings.xml        # 字符串资源
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── README.md
```

## 🚀 快速开始

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11+
- Android SDK 24+

### 构建步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/Joyce0301/noteapp.git
   cd noteapp
   ```

2. **用 Android Studio 打开**
   - 选择 `File → Open`
   - 选择项目根目录
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 `Run` 按钮 (▶️)
   - 或使用快捷键 `Shift + F10`

### 命令行构建

```bash
# 调试版本
./gradlew assembleDebug

# 发布版本
./gradlew assembleRelease

# 运行测试
./gradlew test
```

## 📸 应用截图

> TODO: 添加应用截图

## 🎯 使用指南

### 添加备忘
1. 在顶部输入框输入内容
2. 点击「添加」按钮
3. 备忘将自动保存到数据库并显示在列表中

### 编辑备忘
1. 点击列表中任意条目
2. 在弹出对话框中修改内容
3. 点击「保存」完成编辑

### 删除备忘
1. 长按列表中任意条目
2. 在确认对话框中点击「删除」

## 🛠️ 开发计划

- [ ] 添加搜索功能
- [ ] 支持分类/标签
- [ ] 云同步备份
- [ ] 深色模式支持
- [ ] 小部件 (Widget)
- [ ] 导出/导入功能

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👤 作者

- **Joyce0301** - [GitHub](https://github.com/Joyce0301)

## 🙏 致谢

感谢以下开源项目：
- [AndroidX](https://developer.android.com/jetpack/androidx)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Material Design](https://material.io/)

---

<div align="center">

**Made with ❤️ for Android**

如果这个项目对你有帮助，请给一个 ⭐️ Star！

</div>
