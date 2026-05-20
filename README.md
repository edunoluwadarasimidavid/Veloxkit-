# Veloxkit 🌌

[![Build Veloxkit APK](https://github.com/davidedun2010/veloxkit/actions/workflows/build.yml/badge.svg)](https://github.com/davidedun2010/veloxkit/actions/workflows/build.yml)

## Introduction
Developed by **Smart Tech Programming**, **Veloxkit** is a gorgeous, hyper-modern, high-contrast **Android Developer Companion** designed for terminal power-users and developers on the go. Optimized for environments such as Termux, local Python nodes, and node environments, it serves as a command resource, code vault, and AI developer workspace.

Veloxkit is built entirely in **Kotlin** and **Jetpack Compose**, fully styled with a unique high-performance **Bento Grid Design System**, persistent Room DB vaults, custom real-time canvas backdrop animations, and Google Gemini AI integrations.

---

## 🎨 Bento Grid Design System & Aesthetic Pairings

Veloxkit uses a customized, screen-fitting **Bento Grid** that optimizes visual density and layouts across compact, medium, and expanded device containers:
* **Slate Cyberpunk Scheme**: Implemented using deep black absolute canvases (`#0A0A0A`), matrix elements, neon green outlines (`#00FF41`), and dim green details (`#00CC33`).
* **Grid Density**: Modular feature sizes dynamically group actions into 1x1 widgets, 2x1 wide highlighted cards, and adaptive lists to reduce visual clutter.
* **Animated Backdrop**: Features standard custom Matrix Rain Canvas flows rendering green cascading datastreams securely below interactive UI layers.
* **Cyber Borders & Ripples**: Interactivity is enhanced with custom border glows and tactile haptic indicators.

---

## 🚀 Key Features

### 1. 📋 Command Library
A categorized reference module containing vital diagnostic/installation operations:
* Pre-loaded segments for **Termux setup**, **Git controls**, **Python installations**, and **NodeJS runtimes**.
* Infinite scroll lists, filterable categories, and instant regex-supported keywords query filters.
* Quick-action execution simulation.

### 2. 🔐 Snippet Vault
A secure, persistent vault to save reusable codebase fragments locally:
* Add, edit, or delete snippets.
* Built-in code syntax editor blocks.
* Persisted dynamically via **Room Database**.

### 3. 📂 Project Launcher
Micro-environment config engines that generate script scaffolds for rapid start-ups:
* Bootstraps **Kotlin Compose standard templates**, **Termux Python Rest APIs**, **Node Express API Engines**, and custom scratchpad repos in an instant.
* Immediate clipboard copy action.

### 4. ⚡ Setup Wizard
A progressive step-by-step setup procedure that organizes environment setup sequentially:
* Numerical progress tracks, detailed subsystem descriptions, and one-tap terminal commands.

### 5. 🧠 AI Helper (Gemini API Integration)
An intelligent interactive terminal providing responses configured directly for platform administration:
* Specialized context handling Termux packages, Git structures, and scripting engines.
* Chat history securely saved locally inside encrypted repositories.
* Elegant bubble rendering distinguishing user prompts from executable command-lines.

---

## 🛠️ Automated CI/CD: How to Download APK from GitHub Actions

A **GitHub Actions Workflow** has been pre-configured to build, sign, and build the APK automatically on every push or pull request in the main branch.

### To Download the Compiled APK:
1. Navigate to your fork/repository on **GitHub**.
2. Click on the **Actions** tab in the top navigation bar.
3. Select the latest run of the **Build Veloxkit APK** workflow.
4. Scroll down to the **Artifacts** section at the bottom of the page.
5. Click on the **Veloxkit-Debug-APK** link to download the zip file containing your ready-to-test APK!

---

## 🏗️ Manual Local Build Instructions

To build Veloxkit locally on your development machine, ensure you have JDK 17 installed.

### Pre-requisites:
* Android Studio (Koala or later recommended)
* JDK 17
* Android SDK Platform 34

### Execution:
1. Clone the repository:
   ```bash
   git clone https://github.com/davidedun2010/veloxkit.git
   cd veloxkit
   ```
2. Set execute permissions for the Gradle Wrapper:
   ```bash
   chmod +x gradlew
   ```
3. Compile and build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
4. The generated APK will be available in:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 🧰 Tech Stack Specs

* **Language**: [Kotlin](https://kotlinlang.org/) (100% Type-safe Compose layout architectures)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/compose) with Material Design 3 (M3)
* **Local Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) & SQLite
* **Asynchronous Flow**: Kotlin Coroutines & StateFlow pipelines
* **Network & AI**: RESTful API nodes connecting with Google Gemini API
* **Security**: Encrypted DataStore for local credentials storage
* **CI/CD**: GitHub Actions Build CI pipelines

---

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for parameters.

---
*Created with 💚 by **Smart Tech Programming** — Your Trusted partner in software utilities.*
