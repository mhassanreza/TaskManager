# Task Manager App
A simple task management app built with Jetpack Compose, Clean Architecture, and MVI.

<body>
   <p>This repository showcases an Android application built with modern development tools and libraries. It demonstrates key concepts such as Jetpack Compose, Hilt Dependency Injection, version catalog, type-safe navigation arguments, and more.</p>

   <h2>Features</h2>
   <ul>
      <li><strong>CRUD tasks with Room persistence</strong>: Create, read, update, and delete tasks with local storage.</li>
      <li><strong>Sort and filter tasks</strong>: Sort by priority, due date, or title; filter by all, completed, or pending tasks.</li>
      <li><strong>Material 3 UI with custom theme</strong>: Light/Dark mode toggle with a modern UI design.</li>
      <li><strong>Animations</strong>: Task slide-in/out, FAB bounce for a smooth user experience.</li>
      <li><strong>Swipe-to-delete with undo</strong>: Easily remove tasks with a swipe gesture and undo option.</li>
      <li><strong>Settings screen</strong>: Customize theme and default sort/filter preferences.</li>
   </ul>
   <h2>Tech Stack</h2>
   <ul>
      <li><strong>Kotlin, Jetpack Compose, Hilt, Room, Coroutines, Navigation</strong></li>
      <li><strong>Clean Architecture</strong>: Organized into data, domain, and presentation layers.</li>
      <li><strong>MVI pattern</strong>: Manages UI state and events efficiently.</li>
   </ul>
   <h2>Project Structure</h2>
   <ul>
      <li><strong>app</strong>: Main application module, contains shared resources and app-level configurations.</li>
      <li><strong>storage</strong>: Manages data storage with SharedPreferences and Room for persistent data.</li>
   </ul>
   <h2>Libraries Used</h2>
   <ul>
      <li><a href="https://developer.android.com/jetpack/compose" target="_blank">Jetpack Compose</a> - Modern UI toolkit for building native UI.</li>
      <li><a href="https://developer.android.com/training/dependency-injection/hilt-android" target="_blank">Hilt</a> - Dependency Injection library for Android.</li>
      <li><a href="https://developer.android.com/guide/navigation" target="_blank">Navigation Component</a> - Handling in-app navigation with type-safe args.</li>
      <li><a href="https://github.com/Kotlin/kotlinx.serialization" target="_blank">Kotlinx Serialization</a> - Kotlin serialization for data handling.</li>
   </ul>
   <h2>Installation</h2>
   <ol>
      <li>Clone the repository:</li>
      <li>Open the project in Android Studio.</li>
      <li>Build and run the project on your Android emulator or device.</li>
   </ol>
</body>
