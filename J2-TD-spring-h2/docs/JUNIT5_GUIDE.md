# JUnit 5 Testing Guide

## 📚 Introduction to JUnit 5

JUnit 5 is the latest version of the most popular testing framework for Java. It provides a modern and flexible platform for testing Java applications with powerful features and annotations.

### Why JUnit 5?

- **Modern Architecture**: Modular design with separate sub-projects
- **Rich Annotations**: Improved annotations for better test organization
- **Dynamic Tests**: Create tests at runtime
- **Parameterized Tests**: Run the same test with different inputs
- **Better Assertions**: More expressive assertion methods
- **Extension Model**: Powerful extension mechanism for custom behavior

## 🏗️ JUnit 5 Architecture

JUnit 5 consists of three sub-projects:

1. **JUnit Platform**: Foundation for launching testing frameworks on the JVM
2. **JUnit Jupiter**: Programming and extension model for writing tests
3. **JUnit Vintage**: Provides backward compatibility with JUnit 3 and 4

## 🎯 Basic Test Structure

### Test Class Structure

```java
import org.junit.jupiter.api.*;

@DisplayName("Calculator Tests")
class CalculatorTest {

    private Calculator calculator;

    @BeforeAll
    static void setupAll() {
        // Runs once before all tests in this class
        System.out.println("Starting Calculator tests");
    }

    @BeforeEach
    void setup() {
        // Runs before each test method
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Addition should work correctly")
    void testAddition() {
        // Given - setup test data
        int a = 5;
        int b = 3;

        // When - execute the operation
        int result = calculator.add(a, b);

        // Then - verify the result
        assertEquals(8, result, "5 + 3 should equal 8");
    }

    @AfterEach
    void cleanup() {
        // Runs after each test method
        calculator = null;
    }

    @AfterAll
    static void cleanupAll() {
        // Runs once after all tests in this class
        System.out.println("Finished Calculator tests");
    }
}
```

## 📋 Essential JUnit 5 Annotations

### Lifecycle Annotations

| Annotation | Purpose | When it runs |
|------------|---------|--------------|
| `@BeforeAll` | Setup before all tests | Once per test class |
| `@BeforeEach` | Setup before each test | Before every test method |
| `@AfterEach` | Cleanup after each test | After every test method |
| `@AfterAll` | Cleanup after all tests | Once per test class |

### Test Annotations

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Test` | Marks a method as a test | `@Test void testMethod()` |
| `@DisplayName` | Custom test names | `@DisplayName("Should calculate correctly")` |
| `@Disabled` | Disable a test | `@Disabled("Not implemented yet")` |
| `@RepeatedTest` | Repeat test multiple times | `@RepeatedTest(5)` |
| `@Timeout` | Test timeout | `@Timeout(value = 5, unit = TimeUnit.SECONDS)` |

### Conditional Test Execution

```java
@Test
@EnabledOnOs(OS.WINDOWS)
void testOnWindows() {
    // Only runs on Windows
}

@Test
@EnabledOnJre(JRE.JAVA_21)
void testOnJava21() {
    // Only runs on Java 21
}

@Test
@EnabledIf("java.version.startsWith('21')")
void testWithCondition() {
    // Runs if condition is true
}
```

## 🔍 Assertions

### Basic Assertions

```java
import static org.junit.jupiter.api.Assertions.*;

@Test
void basicAssertions() {
    // Equality assertions
    assertEquals(expected, actual);
    assertEquals(expected, actual, "Custom failure message");
    assertNotEquals(unexpected, actual);

    // Boolean assertions
    assertTrue(condition);
    assertFalse(condition);

    // Null assertions
    assertNull(object);
    assertNotNull(object);

    // Reference assertions
    assertSame(expected, actual);  // Same object reference
    assertNotSame(expected, actual);

    // Array assertions
    assertArrayEquals(expectedArray, actualArray);
}
```

### Advanced Assertions

```java
@Test
void advancedAssertions() {
    // Exception assertions
    Exception exception = assertThrows(
        IllegalArgumentException.class, 
        () -> calculator.divide(10, 0)
    );
    assertEquals("Cannot divide by zero", exception.getMessage());

    // Timeout assertions
    assertTimeout(Duration.ofSeconds(2), () -> {
        // Code that should complete within 2 seconds
        Thread.sleep(1000);
    });

    // Grouped assertions
    assertAll("Person properties",
        () -> assertEquals("John", person.getFirstName()),
        () -> assertEquals("Doe", person.getLastName()),
        () -> assertEquals(25, person.getAge())
    );
}
```

### Custom Assertions with AssertJ (Optional)

While JUnit 5 has good assertions, AssertJ provides more readable assertions:

```java
import static org.assertj.core.api.Assertions.*;

@Test
void assertJExample() {
    List<String> names = Arrays.asList("John", "Jane", "Bob");

    // More readable than JUnit assertions
    assertThat(names)
        .hasSize(3)
        .contains("John", "Jane")
        .doesNotContain("Alice");

    assertThat(person.getAge())
        .isGreaterThan(18)
        .isLessThan(65);
}
```

## 🎯 Parameterized Tests

Run the same test with different inputs:

### Simple Parameterized Tests

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 4, 5})
void testWithValueSource(int number) {
    assertTrue(number > 0 && number <= 5);
}

@ParameterizedTest
@ValueSource(strings = {"racecar", "radar", "level"})
void testPalindromes(String word) {
    assertTrue(isPalindrome(word));
}
```

### CSV Source Tests

```java
@ParameterizedTest
@CsvSource({
    "1, 1, 2",
    "2, 3, 5", 
    "5, 7, 12"
})
void testAddition(int a, int b, int expected) {
    assertEquals(expected, calculator.add(a, b));
}
```

### Method Source Tests

```java
@ParameterizedTest
@MethodSource("provideStringsForIsBlank")
void testIsBlank(String input, boolean expected) {
    assertEquals(expected, Strings.isBlank(input));
}

static Stream<Arguments> provideStringsForIsBlank() {
    return Stream.of(
        Arguments.of(null, true),
        Arguments.of("", true),
        Arguments.of("  ", true),
        Arguments.of("not blank", false)
    );
}
```

## 🧪 Testing Patterns and Best Practices

### 1. Test Method Naming

```java
// Good naming conventions
@Test
void shouldReturnTrueWhenNumberIsEven() { }

@Test
void shouldThrowExceptionWhenDividingByZero() { }

@Test
void findById_WithValidId_ShouldReturnUser() { }

@Test
void findById_WithInvalidId_ShouldReturnEmpty() { }
```

### 2. Given-When-Then Pattern

```java
@Test
@DisplayName("Should calculate discount correctly for premium customers")
void shouldCalculateDiscountForPremiumCustomers() {
    // Given - Arrange test data
    Customer customer = new Customer("John", CustomerType.PREMIUM);
    Order order = new Order(100.0);
    DiscountService discountService = new DiscountService();

    // When - Act on the system under test
    double discount = discountService.calculateDiscount(customer, order);

    // Then - Assert the expected result
    assertEquals(10.0, discount, "Premium customers should get 10% discount");
}
```

### 3. Test Organization

```java
@DisplayName("Player Service Tests")
class PlayerServiceTest {

    @Nested
    @DisplayName("Player Creation Tests")
    class PlayerCreationTests {

        @Test
        @DisplayName("Should create player with valid data")
        void shouldCreatePlayerWithValidData() { }

        @Test
        @DisplayName("Should throw exception with invalid email")
        void shouldThrowExceptionWithInvalidEmail() { }
    }

    @Nested
    @DisplayName("Player Update Tests")
    class PlayerUpdateTests {

        @Test
        @DisplayName("Should update player successfully")
        void shouldUpdatePlayerSuccessfully() { }
    }
}
```

## 🎭 Testing with Mockito

### Basic Mocking

```java
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void shouldCreatePlayer() {
        // Given
        Player player = new Player("John", "Doe", "john@example.com", 25, "Forward");
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // When
        Player result = playerService.createPlayer(player);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(playerRepository).save(player);
    }
}
```

### Argument Matching

```java
@Test
void testWithArgumentMatchers() {
    // Exact argument matching
    when(repository.findById(1L)).thenReturn(Optional.of(player));

    // Any argument of type
    when(repository.save(any(Player.class))).thenReturn(player);

    // Argument capturing
    ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
    playerService.createPlayer(player);
    verify(repository).save(playerCaptor.capture());
    assertEquals("John", playerCaptor.getValue().getFirstName());
}
```

## 🚀 Dynamic Tests

Create tests at runtime:

```java
@TestFactory
Stream<DynamicTest> dynamicTestsFromIntStream() {
    return IntStream.iterate(0, n -> n + 2)
        .limit(10)
        .mapToObj(n -> DynamicTest.dynamicTest(
            "test" + n, 
            () -> assertTrue(n % 2 == 0)
        ));
}
```

## 📊 Spring Boot Testing Integration

### Testing Layers

```java
// Repository Layer Testing
@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;
}

// Service Layer Testing
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;
}

// Web Layer Testing
@WebMvcTest(PlayerController.class)
class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;
}

// Integration Testing
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
}
```

## 🎯 Testing Exercises for This Project

### Exercise 1: Basic Unit Tests

**File**: `PlayerServiceTest.java`

Add these test methods to practice JUnit 5 features:

```java
@Test
@DisplayName("Should throw exception when creating player with null data")
void shouldThrowExceptionWhenCreatingPlayerWithNullData() {
    // Test exception handling
    assertThrows(IllegalArgumentException.class, () -> {
        playerService.createPlayer(null);
    });
}

@ParameterizedTest
@ValueSource(ints = {-1, 0, 151, 200})
@DisplayName("Should throw exception for invalid age values")
void shouldThrowExceptionForInvalidAge(int invalidAge) {
    // Test with multiple invalid ages
    Player player = new Player("John", "Doe", "john@example.com", invalidAge, "Forward");

    assertThrows(IllegalArgumentException.class, () -> {
        playerService.createPlayer(player);
    });
}

@RepeatedTest(5)
@DisplayName("Should generate unique IDs for multiple players")
void shouldGenerateUniqueIds() {
    // Test repeated execution
    Player player = new Player("Test", "Player", "test@example.com", 25, "Forward");
    when(playerRepository.save(any(Player.class))).thenReturn(player);

    Player result = playerService.createPlayer(player);
    assertNotNull(result);
}
```

### Exercise 2: Nested Test Organization

Create organized test structure:

```java
@DisplayName("Player Service Comprehensive Tests")
class PlayerServiceTest {

    @Nested
    @DisplayName("Player Creation")
    class PlayerCreation {
        // Group related creation tests
    }

    @Nested
    @DisplayName("Player Retrieval")
    class PlayerRetrieval {
        // Group related retrieval tests
    }

    @Nested
    @DisplayName("Player Updates")
    class PlayerUpdates {
        // Group related update tests
    }
}
```

### Exercise 3: Custom Test Extensions

Create reusable test components:

```java
@ExtendWith(TimingExtension.class)
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    // Custom extensions for logging, timing, etc.
}
```

## 🔧 Test Configuration

### Test Properties

Create `application-test.properties`:

```properties
# Test-specific configuration
spring.datasource.url=jdbc:h2:mem:testdb-test
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.org.springframework.web=DEBUG
```

### Test Profiles

Use `@ActiveProfiles("test")` to activate test configuration.

## 📈 Code Coverage

### Running with JaCoCo

```bash
# Run tests with coverage
./mvnw clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Coverage Goals

Aim for:
- **Line Coverage**: > 80%
- **Branch Coverage**: > 70%
- **Method Coverage**: > 90%

## 🚨 Common Testing Mistakes

### 1. Testing Implementation Instead of Behavior

❌ **Bad:**
```java
@Test
void shouldCallRepositorySaveMethod() {
    playerService.createPlayer(player);
    verify(repository).save(any());  // Testing implementation detail
}
```

✅ **Good:**
```java
@Test
void shouldCreatePlayerWithCorrectData() {
    Player result = playerService.createPlayer(player);
    assertEquals("John", result.getFirstName());  // Testing behavior
}
```

### 2. Large Test Methods

❌ **Bad:**
```java
@Test
void testEverything() {
    // Tests creation, update, deletion, search all in one method
}
```

✅ **Good:**
```java
@Test
void shouldCreatePlayer() { /* Test only creation */ }

@Test
void shouldUpdatePlayer() { /* Test only update */ }

@Test
void shouldDeletePlayer() { /* Test only deletion */ }
```

### 3. No Assertions

❌ **Bad:**
```java
@Test
void testMethod() {
    playerService.createPlayer(player);
    // No assertions!
}
```

✅ **Good:**
```java
@Test
void shouldCreatePlayer() {
    Player result = playerService.createPlayer(player);
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
}
```

## 📚 Additional Resources

### Official Documentation
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

### Best Practices
- **Test Naming**: Use descriptive names that explain what is being tested
- **Test Independence**: Each test should be independent and repeatable
- **Test Organization**: Group related tests using `@Nested` classes
- **Mock Sparingly**: Only mock external dependencies, not the system under test
- **Assertions**: Always include meaningful assertions

## ✅ JUnit 5 Checklist

- [ ] Understand basic test structure (Given-When-Then)
- [ ] Know essential annotations (`@Test`, `@BeforeEach`, etc.)
- [ ] Practice different assertion methods
- [ ] Use parameterized tests for multiple inputs
- [ ] Organize tests with `@Nested` classes
- [ ] Mock dependencies appropriately
- [ ] Write descriptive test names
- [ ] Achieve good test coverage
- [ ] Test both happy path and edge cases
- [ ] Handle exceptions properly in tests

---

**Happy Testing with JUnit 5! 🧪✨**

Remember: Good tests are your safety net. They give you confidence to refactor, add features, and maintain your code over time.
