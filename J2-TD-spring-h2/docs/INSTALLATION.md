# Installation Guide

This guide will help you set up your development environment for the Spring Boot Testing Methodology project.

## 📋 Prerequisites Overview

Before starting, you need to install:
1. **Java Development Kit (JDK) 21** - Programming language runtime
2. **IntelliJ IDEA** - Integrated Development Environment (IDE)
3. **Git** - Version control system (optional but recommended)

## ☕ Java Installation

### What is Java?
Java is a programming language and computing platform. The JDK (Java Development Kit) includes tools for developing Java applications.

### Version Requirement
This project requires **Java 21** (the latest Long-Term Support version as of 2025).

### Installation Instructions by Operating System

#### 🪟 Windows Installation

**Method 1: Using Oracle JDK (Recommended)**

1. **Download JDK 21:**
   - Visit: https://www.oracle.com/java/technologies/downloads/
   - Select "Windows" and download "x64 Installer"

2. **Run the Installer:**
   - Double-click the downloaded `.msi` file
   - Follow the installation wizard
   - Accept the license agreement
   - Choose installation directory (default is recommended)

3. **Set Environment Variables:**
   - Press `Win + X` and select "System"
   - Click "Advanced system settings"
   - Click "Environment Variables"
   - Under "System Variables", click "New":
     - Variable name: `JAVA_HOME`
     - Variable value: `C:\Program Files\Java\jdk-21` (adjust if different)
   - Find "Path" in System Variables, click "Edit"
   - Click "New" and add: `%JAVA_HOME%\bin`
   - Click "OK" to close all windows

4. **Verify Installation:**
   ```cmd
   # Open Command Prompt and run:
   java -version
   javac -version
   ```

**Method 2: Using OpenJDK**

1. **Download OpenJDK 21:**
   - Visit: https://adoptium.net/
   - Select "OpenJDK 21 (LTS)" and "Windows x64"
   - Download the `.msi` installer

2. **Follow the same installation and verification steps as Oracle JDK**

#### 🍎 macOS Installation

**Method 1: Using Homebrew (Recommended)**

1. **Install Homebrew** (if not already installed):
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```

2. **Install OpenJDK 21:**
   ```bash
   # Update Homebrew
   brew update

   # Install OpenJDK 21
   brew install openjdk@21

   # Create symlink for system Java
   sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
   ```

3. **Set JAVA_HOME** (add to `~/.zshrc` or `~/.bash_profile`):
   ```bash
   # Edit your shell profile
   nano ~/.zshrc  # for zsh (default on newer macOS)
   # OR
   nano ~/.bash_profile  # for bash

   # Add these lines:
   export JAVA_HOME=/opt/homebrew/opt/openjdk@21
   export PATH=$JAVA_HOME/bin:$PATH

   # Reload your profile
   source ~/.zshrc
   # OR
   source ~/.bash_profile
   ```

**Method 2: Manual Installation**

1. **Download JDK 21:**
   - Visit: https://www.oracle.com/java/technologies/downloads/
   - Select "macOS" and download "x64 DMG Installer"

2. **Install:**
   - Double-click the downloaded `.dmg` file
   - Follow the installation wizard

3. **Set JAVA_HOME:**
   ```bash
   # Find Java installation path
   /usr/libexec/java_home -v 21

   # Add to shell profile
   export JAVA_HOME=$(/usr/libexec/java_home -v 21)
   export PATH=$JAVA_HOME/bin:$PATH
   ```

4. **Verify Installation:**
   ```bash
   java -version
   javac -version
   ```

#### 🐧 Linux Installation

**Ubuntu/Debian:**

1. **Update package list:**
   ```bash
   sudo apt update
   ```

2. **Install OpenJDK 21:**
   ```bash
   # Install JDK 21
   sudo apt install openjdk-21-jdk

   # Install development tools (optional)
   sudo apt install openjdk-21-jdk-headless
   ```

3. **Set JAVA_HOME:**
   ```bash
   # Edit bash profile
   nano ~/.bashrc

   # Add these lines at the end:
   export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
   export PATH=$JAVA_HOME/bin:$PATH

   # Reload profile
   source ~/.bashrc
   ```

**Red Hat/CentOS/Fedora:**

1. **Install OpenJDK 21:**
   ```bash
   # For Fedora
   sudo dnf install java-21-openjdk-devel

   # For CentOS/RHEL
   sudo yum install java-21-openjdk-devel
   ```

2. **Set JAVA_HOME:**
   ```bash
   # Edit bash profile
   nano ~/.bashrc

   # Add these lines:
   export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
   export PATH=$JAVA_HOME/bin:$PATH

   # Reload profile
   source ~/.bashrc
   ```

**Arch Linux:**

1. **Install OpenJDK 21:**
   ```bash
   sudo pacman -S jdk21-openjdk
   ```

2. **Set default Java version:**
   ```bash
   sudo archlinux-java set java-21-openjdk
   ```

### Java Installation Verification

After installation, verify Java is working correctly:

```bash
# Check Java version (should show version 21.x.x)
java -version

# Check Java compiler version
javac -version

# Check JAVA_HOME environment variable
echo $JAVA_HOME  # On Windows: echo %JAVA_HOME%
```

**Expected Output:**
```
openjdk version "21.0.x" 2024-xx-xx
OpenJDK Runtime Environment (build 21.0.x+xx-xxx)
OpenJDK 64-Bit Server VM (build 21.0.x+xx-xxx, mixed mode, sharing)
```

## 🎯 IntelliJ IDEA Installation

### What is IntelliJ IDEA?
IntelliJ IDEA is a powerful Integrated Development Environment (IDE) for Java development. It provides code completion, debugging, testing tools, and many other features.

### Free Options for Students

**Option 1: IntelliJ IDEA Community Edition (Free)**
- Free and open-source
- Perfect for this course
- Supports Java, Maven, Git

**Option 2: IntelliJ IDEA Ultimate (Free for Students)**
- Professional edition with advanced features
- Free with student license
- Supports additional frameworks and tools

### Getting Student License (Ultimate Edition)

1. **GitHub Student Developer Pack:**
   - Visit: https://education.github.com/pack
   - Sign up with your student email
   - Verify your student status
   - Access JetBrains tools for free

2. **Direct JetBrains Application:**
   - Visit: https://www.jetbrains.com/community/education/#students
   - Apply with your student email
   - Upload student documentation if required

### Installation Instructions

#### 🪟 Windows

1. **Download IntelliJ IDEA:**
   - Visit: https://www.jetbrains.com/idea/download/#section=windows
   - Choose "Community" (free) or "Ultimate" (if you have student license)
   - Download the `.exe` installer

2. **Run Installation:**
   - Double-click the downloaded `.exe` file
   - Follow the installation wizard
   - Choose installation options:
     - ✅ Create desktop shortcut
     - ✅ Update PATH variable
     - ✅ Create associations for .java files
     - ✅ Add "bin" folder to PATH

3. **Complete Installation:**
   - Click "Install"
   - Wait for installation to complete
   - Choose "Run IntelliJ IDEA"

#### 🍎 macOS

1. **Download IntelliJ IDEA:**
   - Visit: https://www.jetbrains.com/idea/download/#section=mac
   - Download the `.dmg` file

2. **Install:**
   - Double-click the downloaded `.dmg` file
   - Drag IntelliJ IDEA to Applications folder
   - Launch from Applications

#### 🐧 Linux

**Method 1: Using Snap (Recommended)**
```bash
# Install Community Edition
sudo snap install intellij-idea-community --classic

# OR install Ultimate Edition (if you have license)
sudo snap install intellij-idea-ultimate --classic
```

**Method 2: Manual Installation**
1. **Download:**
   - Visit: https://www.jetbrains.com/idea/download/#section=linux
   - Download the `.tar.gz` file

2. **Extract and Install:**
   ```bash
   # Extract to /opt directory
   sudo tar -xzf ideaIC-*.tar.gz -C /opt/

   # Create symbolic link
   sudo ln -s /opt/idea-IC-*/bin/idea.sh /usr/local/bin/idea

   # Launch IntelliJ IDEA
   idea
   ```

### First Launch Setup

1. **Welcome Screen:**
   - Select "Do not import settings" (first time)
   - Choose your UI theme (Dark or Light)

2. **Plugin Installation:**
   - IntelliJ will suggest essential plugins
   - Install recommended plugins for Java development

3. **JDK Configuration:**
   - Go to File → Project Structure → SDKs
   - Click "+" and add your JDK 21 installation
   - Set as project SDK

## 🔧 Git Installation (Optional but Recommended)

### Why Git?
Git is a version control system that helps track changes in your code and collaborate with others.

#### 🪟 Windows
1. **Download Git:**
   - Visit: https://git-scm.com/download/win
   - Download and run the installer
   - Use default settings

#### 🍎 macOS
```bash
# Using Homebrew
brew install git

# OR download from: https://git-scm.com/download/mac
```

#### 🐧 Linux
```bash
# Ubuntu/Debian
sudo apt install git

# Red Hat/CentOS/Fedora
sudo dnf install git  # or sudo yum install git

# Arch Linux
sudo pacman -S git
```

### Git Configuration
```bash
# Set your name and email
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## 🧪 Verification Setup

### Create Test Project

1. **Open IntelliJ IDEA**
2. **Create New Project:**
   - Click "New Project"
   - Select "Maven Archetype"
   - Choose GroupId: `com.test`
   - Choose ArtifactId: `hello-java`
   - Select Maven archetype: `maven-archetype-quickstart`

3. **Test Java Installation:**
   - Open `src/main/java/com/test/App.java`
   - Run the main method
   - Should print "Hello World!"

### Import Course Project

1. **Extract Project:**
   - Extract the `spring-boot-testing-project.zip` file
   - Remember the location

2. **Open in IntelliJ:**
   - Click "Open" in IntelliJ welcome screen
   - Navigate to extracted folder
   - Select the `pom.xml` file
   - Click "Open"
   - Choose "Open as Project"

3. **Wait for Setup:**
   - IntelliJ will download Maven dependencies
   - This may take a few minutes
   - Progress will be shown in the bottom status bar

4. **Verify Setup:**
   - Navigate to `src/main/java/com/ynov/testing/TestingMethodologyApplication.java`
   - Right-click and select "Run 'TestingMethodologyApplication'"
   - Application should start successfully
   - Check console for "Started TestingMethodologyApplication"

## 🚨 Troubleshooting

### Common Issues

**Issue: "JAVA_HOME not set"**
```bash
# Solution: Set JAVA_HOME environment variable
# Follow the JAVA_HOME setup steps for your OS above
```

**Issue: "Java version not supported"**
```bash
# Solution: Verify Java version
java -version

# Should show version 21.x.x
# If not, reinstall Java 21 or check PATH variable
```

**Issue: "Maven dependencies not downloading"**
- **Solution 1:** Check internet connection
- **Solution 2:** Go to File → Settings → Build → Build Tools → Maven
  - Check Maven home directory
  - Check settings.xml file location

**Issue: "Port 8080 already in use"**
```bash
# Solution: Kill process using port 8080
# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux:
lsof -ti:8080 | xargs kill -9
```

**Issue: IntelliJ can't find JDK**
- **Solution:** Go to File → Project Structure → Project Settings → Project
  - Set Project SDK to your installed JDK 21
  - Set Project language level to "21"

### Getting Additional Help

1. **Check System Requirements:**
   - Ensure your system meets minimum requirements
   - 8GB RAM recommended for IntelliJ IDEA
   - 2GB free disk space

2. **Update Everything:**
   - Update your operating system
   - Use latest versions of Java and IntelliJ IDEA

3. **Restart After Installation:**
   - Restart your computer after installing Java
   - This ensures environment variables are properly loaded

## ✅ Installation Checklist

- [ ] Java JDK 21 installed and verified
- [ ] JAVA_HOME environment variable set
- [ ] IntelliJ IDEA installed
- [ ] Git installed and configured (optional)
- [ ] Course project opened in IntelliJ IDEA
- [ ] Maven dependencies downloaded
- [ ] Application runs successfully
- [ ] H2 console accessible at http://localhost:8080/h2-console

## 🎉 Next Steps

Once your installation is complete:

1. **Read the main [README.md](../README.md)** for project overview
2. **Review [JUNIT5_GUIDE.md](JUNIT5_GUIDE.md)** for testing fundamentals
3. **Start with the first exercise** in the main documentation
4. **Explore the existing test examples** to understand the codebase

---

**Congratulations! 🎉 Your development environment is now ready for the Testing Methodology course!**
