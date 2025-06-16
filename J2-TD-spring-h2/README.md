# J2—TD

Ce projet a été générer via Perplexity Lab. Il peut contenir des incohérences, mais l'essentiel pour ce TD marche

Vous pouvez utilise rJava JDK 24 à la place du 21


# Spring Boot Testing Methodology Project

## 📚 Course: Testing Methodology - Ynov Campus

This is a complete Spring Boot educational project designed for learning and practicing testing methodologies with JUnit 5. The project implements a RESTful API for managing players using the Controller-Service-Repository pattern with H2 database.

## 🎯 Learning Objectives

- **Write Unit Tests** with JUnit 5 and Mockito
- **Understand Testing Techniques** for different layers (Controller, Service, Repository)
- **Implement Integration Tests** with Spring Boot Test
- **Practice TDD (Test-Driven Development)** methodology
- **Learn Testing Best Practices** and patterns
- **Execute and Generate Test Reports** with code coverage

## 🏗️ Project Architecture

The project follows the **Controller-Service-Repository** pattern:

```
📦 src/main/java/com/ynov/testing/
├── 🚀 TestingMethodologyApplication.java    # Main Spring Boot application
├── 📋 controller/
│   └── PlayerController.java               # REST API endpoints
├── 🔧 service/
│   └── PlayerService.java                  # Business logic layer
├── 💾 repository/
│   └── PlayerRepository.java               # Data access layer
└── 📊 model/
    └── Player.java                         # Entity/Model class

📦 src/test/java/com/ynov/testing/
├── 🧪 controller/
│   └── PlayerControllerTest.java           # Controller unit tests
├── 🔬 service/
│   └── PlayerServiceTest.java              # Service unit tests
└── 🔍 repository/
    └── PlayerRepositoryTest.java           # Repository integration tests
```

## 🛠️ Technologies Used

- **Java 21** (Latest LTS version)
- **Spring Boot 3.4.5** (Latest stable version)
- **Spring Data JPA** (Database operations)
- **H2 Database** (In-memory database for development and testing)
- **JUnit 5** (Testing framework)
- **Mockito** (Mocking framework)
- **Maven** (Build tool and dependency management)
- **Jackson** (JSON serialization/deserialization)

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 21 or higher**
2. **IntelliJ IDEA** (Community or Ultimate Edition)
3. **Git** (for version control)

### Installation Guides

📖 **Detailed installation instructions available in:**
- [`docs/INSTALLATION.md`](docs/INSTALLATION.md) - Complete setup guide for Windows, macOS, and Linux
- [`docs/INTELLIJ_SETUP.md`](docs/INTELLIJ_SETUP.md) - IntelliJ IDEA installation and configuration
- [`docs/JAVA_INSTALLATION.md`](docs/JAVA_INSTALLATION.md) - Java JDK installation guide

### Quick Start

1. **Extract the project** to your desired location
2. **Open IntelliJ IDEA** and select "Open Project"
3. **Navigate** to the extracted folder and select `pom.xml`
4. **Wait** for Maven to download dependencies
5. **Run** the application using the green play button next to `TestingMethodologyApplication.java`

## 🏃‍♂️ Running the Application

### Method 1: Using IntelliJ IDEA
1. Open `src/main/java/com/ynov/testing/TestingMethodologyApplication.java`
2. Click the green play button (▶️) next to the class name
3. The application will start on `http://localhost:8080`

### Method 2: Using Maven Command Line
```bash
# Navigate to project directory
cd spring-boot-testing-project

# Run the application
./mvnw spring-boot:run

# On Windows:
mvnw.cmd spring-boot:run
```

### Method 3: Using Java Command
```bash
# Compile the project
./mvnw clean package

# Run the JAR file
java -jar target/testing-methodology-1.0.0.jar
```

## 🔍 Testing the Application

### Running All Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw clean test jacoco:report
```

### Running Specific Test Categories

```bash
# Run only unit tests
./mvnw test -Dtest="*Test"

# Run only integration tests
./mvnw test -Dtest="*IT"

# Run specific test class
./mvnw test -Dtest="PlayerServiceTest"
```

### Test Coverage Report

After running tests with JaCoCo, view the coverage report:
- Open `target/site/jacoco/index.html` in your browser

## 🌐 API Endpoints

### Player Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/players` | Get all players |
| `GET` | `/api/players/{id}` | Get player by ID |
| `POST` | `/api/players` | Create new player |
| `PUT` | `/api/players/{id}` | Update existing player |
| `DELETE` | `/api/players/{id}` | Delete player |

### Search Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/players/search/team/{teamName}` | Get players by team |
| `GET` | `/api/players/search/position/{position}` | Get players by position |
| `GET` | `/api/players/search/age?minAge=20&maxAge=30` | Get players by age range |
| `GET` | `/api/players/search/name?fullName=John` | Search players by name |
| `GET` | `/api/players/search/salary?minSalary=50000` | Get players by minimum salary |

### Status Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/players/active` | Get active players |
| `GET` | `/api/players/inactive` | Get inactive players |
| `PATCH` | `/api/players/{id}/activate` | Activate player |
| `PATCH` | `/api/players/{id}/deactivate` | Deactivate player |

### Statistics

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/players/count/team/{teamName}` | Count players in team |
| `GET` | `/api/players/count/active` | Count active players |
| `GET` | `/api/players/stats/team/{teamName}/average-age` | Get average age by team |
| `GET` | `/api/players/health` | Service health check |

## 📊 H2 Database Console

Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `password`

## 🧪 Testing Exercises

### 📝 Exercise 1: Basic Unit Testing
**File:** `src/test/java/com/ynov/testing/service/PlayerServiceTest.java`

**Objectives:**
- Understand test structure (Given-When-Then)
- Practice mocking with Mockito
- Learn assertion techniques
- Test exception scenarios

**Tasks:**
1. Run existing tests and analyze the results
2. Add a new test method for `activatePlayer()`
3. Create a test for edge case: activating already active player

### 📝 Exercise 2: Controller Testing
**File:** `src/test/java/com/ynov/testing/controller/PlayerControllerTest.java`

**Objectives:**
- Test REST endpoints with MockMvc
- Validate JSON responses
- Test HTTP status codes
- Handle request/response mapping

**Tasks:**
1. Add test for pagination parameters
2. Test invalid JSON input scenarios
3. Create test for CORS headers

### 📝 Exercise 3: Repository Integration Testing
**File:** `src/test/java/com/ynov/testing/repository/PlayerRepositoryTest.java`

**Objectives:**
- Test database operations
- Validate JPA queries
- Test constraints and relationships
- Use TestEntityManager

**Tasks:**
1. Add test for custom JPQL queries
2. Test database constraints (unique email)
3. Create test for bulk operations

### 📝 Exercise 4: End-to-End Integration Testing
**Create:** `src/test/java/com/ynov/testing/integration/PlayerIntegrationTest.java`

**Objectives:**
- Test complete application flow
- Use TestRestTemplate
- Test with real HTTP requests
- Validate complete scenarios

**Tasks:**
1. Create full CRUD operation test
2. Test business logic workflows
3. Validate error handling

## 📚 Learning Resources

### Testing Documentation
- [`docs/JUNIT5_GUIDE.md`](docs/JUNIT5_GUIDE.md) - Comprehensive JUnit 5 tutorial
- [`docs/MOCKITO_GUIDE.md`](docs/MOCKITO_GUIDE.md) - Mockito framework guide
- [`docs/TESTING_BEST_PRACTICES.md`](docs/TESTING_BEST_PRACTICES.md) - Testing patterns and practices
- [`docs/SPRING_BOOT_TESTING.md`](docs/SPRING_BOOT_TESTING.md) - Spring Boot specific testing techniques

### API Documentation
- [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md) - Complete API reference with examples

## 🔧 Development Setup

### IDE Configuration
1. **Import Code Style:** Import `docs/idea-code-style.xml` in IntelliJ
2. **Configure Maven:** Ensure Maven home is properly set
3. **Set JDK:** Configure Project SDK to Java 21
4. **Enable Annotations:** Enable annotation processing

### Useful IDE Shortcuts
- `Ctrl+Shift+T` (Windows/Linux) or `Cmd+Shift+T` (Mac): Create/Navigate to test
- `Ctrl+Shift+F10` (Windows/Linux) or `Cmd+Shift+R` (Mac): Run test method
- `Ctrl+Alt+Shift+T` (Windows/Linux) or `Cmd+Alt+Shift+T` (Mac): Run all tests in class

## 🐛 Troubleshooting

### Common Issues

**Issue: Port 8080 already in use**
```bash
# Solution: Change port in application.properties
server.port=8081
```

**Issue: Tests failing due to database locks**
```bash
# Solution: Use @DirtiesContext annotation or ensure proper cleanup
```

**Issue: Maven dependencies not downloading**
```bash
# Solution: Refresh Maven project
./mvnw dependency:resolve
```

### Getting Help
1. Check the troubleshooting section in [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md)
2. Review console logs for error messages
3. Verify Java and Maven versions
4. Ensure proper IDE configuration

## 📈 Project Metrics

After running tests, you can view:
- **Test Results:** `target/surefire-reports/`
- **Coverage Report:** `target/site/jacoco/index.html`
- **Build Reports:** `target/`

## 🎓 Assessment Criteria

Your testing implementation will be evaluated on:

1. **Test Coverage** (>80% line coverage)
2. **Test Quality** (meaningful assertions, edge cases)
3. **Test Organization** (clear naming, proper structure)
4. **Mock Usage** (appropriate mocking strategies)
5. **Integration Testing** (database operations, API endpoints)
6. **Documentation** (clear test descriptions and comments)

## 🤝 Contributing

This is an educational project. Students are encouraged to:
1. Add new test cases
2. Improve existing tests
3. Add documentation
4. Report issues or suggestions

## 📄 License

This project is created for educational purposes as part of the Testing Methodology course at Ynov Campus.

## 📞 Support

For technical support or questions:
1. Review the documentation in the `docs/` folder
2. Check existing test examples
3. Consult with your instructor
4. Use the troubleshooting guide

---

**Happy Testing! 🧪✨**

*Remember: Good tests are the foundation of reliable software. Practice writing clear, maintainable, and comprehensive tests!*
