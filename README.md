# Setting up and Running Inventory Manager

## Prerequisites
- Java Development Kit (JDK 11) installed on your machine.
- Git installed to clone the repository (optional).

## Steps

1. **Clone Repository**: Clone the repository containing the Spring Boot application code.
   ```bash
   git clone https://github.com/parastripathi/inventory.git
   ```

2. **Navigate to Project Directory**: Open a terminal/command prompt and change into the project directory.
   ```bash
   cd inventory
   ```

3. **Build Project**: Run the Gradle build command:
   ```bash
   ./gradlew build
   ```

4. **Run Application**: Execute the generated JAR file:
   ```bash
   java -jar build/libs/inventory-0.0.1-SNAPSHOT.jar
   ```

5. **Access Application**: Access the running application at `http://localhost:8080`.

6. **Explore Swagger Documentation**: Access the Swagger UI at `http://localhost:8080/swagger-ui/`.

![screenshot](src/main/resources/static/swagger.png)

**Concurrency Mechanisms**:
Concurrency is managed safely using ConcurrentHashMap for inventory operations, and all Inventory Service methods are self-contained ensuring Thread Safety.





