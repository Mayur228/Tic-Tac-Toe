# 🎮 TicTacToe Multiplayer – Android

A modern TicTacToe game built with Jetpack Compose featuring:

- 🤖 Single Player (AI Opponent)
- 📡 Offline Multiplayer (Bluetooth)
- 🧠 Clean Architecture
- ⚡ Real-time move synchronization

This project demonstrates real-world integration of a custom Bluetooth SDK for offline peer-to-peer multiplayer gaming.

---

## 🚀 Features

### 🧍 Single Player Mode
- Play against AI
- Smart move decision logic
- Instant response gameplay
- Win / Draw detection

### 📡 Multiplayer Mode (Offline)

Powered by **ClassicBluetoothSdk**

- Player 1 can Host a game
- Player 2 can Scan & Join
- Real-time move synchronization
- No internet required
- No backend required
- Works completely offline

---

## 🏗 Architecture

The app follows Clean Architecture principles:

```
Presentation (Compose UI)
        ↓
ViewModel (Game Logic)
        ↓
Domain Layer (Game Rules)
        ↓
Bluetooth SDK (Multiplayer Transport)
```

Separation of concerns ensures:

- Reusable business logic
- Testable game engine
- Scalable multiplayer integration

---

## 📡 Multiplayer Flow

### Host Flow

1. Player selects "Host Game"
2. Device starts Bluetooth server
3. Opponent scans devices
4. Connection established
5. Game session begins

---

### Join Flow

1. Player selects "Join Game"
2. App scans nearby Bluetooth devices
3. Select host from list
4. Connect to host
5. Game starts instantly

---

## 🔐 Multiplayer Communication Design

- Move data transmitted as framed binary messages
- Each move contains:
  - Player identifier
  - Cell position (0–8)
  - Game state validation

Framed transport ensures:

- No message corruption
- No partial reads
- Real-time synchronized board state

---

## 🤖 AI Implementation

The AI opponent:

- Detects winning opportunities
- Blocks opponent winning moves
- Fallbacks to best available cell
- Guarantees valid board state

Game engine handles:

- Win detection (rows, columns, diagonals)
- Draw condition
- Game reset
- Turn switching logic

---

## 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Coroutines
- Flow
- Clean Architecture
- Classic Bluetooth (RFCOMM)

---

## 🎮 Game Modes

| Mode | Description |
|------|-------------|
| Single Player | Play against AI |
| Multiplayer Host | Create Bluetooth game session |
| Multiplayer Join | Join host session |

---

## 📷 Screens Overview

- Home Screen (Mode Selection)
- Game Board UI
- Bluetooth Device Scanner
- Connection Status Indicator
- Result Dialog (Win / Draw)

---

## 💡 Engineering Highlights

- Lifecycle-aware Bluetooth handling
- Structured concurrency
- No GlobalScope usage
- State-driven UI
- Real-time board synchronization
- Offline-first architecture

---

## 🔥 Why This Project Matters

This project demonstrates:

- Real-time multiplayer without internet
- Clean SDK integration
- Scalable architecture design
- Separation between transport layer and game engine
- Production-ready Android engineering practices

It showcases how offline multiplayer games can be built using structured Bluetooth communication.

---

## 📌 Future Improvements

- BLE multiplayer support
- Difficulty levels for AI
- Score tracking
- Match history
- Sound effects & animations
- Cross-platform multiplayer

---

## 📄 License

This project is built for demonstration and educational purposes.

Multiplayer functionality is powered by a proprietary Bluetooth SDK.
