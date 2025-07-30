# Marble Quarry Manager

The Marble Quarry Manager is a Java-based application designed to manage marble blocks and orders in a quarry. It provides functionalities to register blocks, manage orders, simulate theoretical cuts, and calculate the required blocks for specific orders.

This project was developed as part of the course **"Java: Fundamentos à Criação de APIs com Base de Dados"** offered by CESAE Digital, which took place from **2025-06-26 to 2025-08-01**, with a total duration of **50 hours**.

## Features

- Register new marble blocks.
- List available and all marble blocks.
- Register new orders and calculate required blocks.
- Simulate theoretical cuts for available blocks.
- Manage block and order data using a PostgreSQL database.

## Technologies Used

- **Java 17**: Core programming language.
- **Maven**: Build and dependency management.
- **JUnit 5**: Unit testing framework.
- **PostgreSQL**: Relational database for storing block and order data.
- **Dotenv**: For managing environment variables.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- Maven
- PostgreSQL
- A `.env` file with the following variables:

  ```plaintext
  DB_URL=jdbc:postgresql://<host>:<port>/<database>
  DB_USER=<your_database_user>
  DB_PASSWORD=<your_database_password>
  ```

## Setup Instructions

1. **Clone the Repository**:

   ```bash
   git clone <repository-url>
   cd marble-quarry-manager
   ```

2. **Configure the Database**:
   - Create a PostgreSQL database.
   - Set up the required tables using the following SQL:

     ```sql
     CREATE TABLE blocks (
         id SERIAL PRIMARY KEY,
         marble_type VARCHAR(50),
         length_m DOUBLE PRECISION,
         height_m DOUBLE PRECISION,
         thickness_m DOUBLE PRECISION,
         status VARCHAR(20)
     );

     CREATE TABLE orders (
         id SERIAL PRIMARY KEY,
         unit_count INT,
         unit_length_m DOUBLE PRECISION,
         unit_height_m DOUBLE PRECISION,
         unit_thickness_m DOUBLE PRECISION
     );

     CREATE TABLE order_blocks (
         order_id INT REFERENCES orders(id),
         block_id INT REFERENCES blocks(id),
         PRIMARY KEY (order_id, block_id)
     );
     ```

3. **Install Dependencies**:

   ```bash
   mvn clean install
   ```

## Running the Application

To run the application, execute the following command:

```bash
mvn exec:java
```

The application will display a menu in the terminal for interacting with the system.

## Running Tests

To execute the unit tests, run:

```bash
mvn test
```

## Project Structure

- **`src/main/java`**: Contains the main application code.
  - `config`: Database connection configuration.
  - `model`: Data models for `Block` and `Order`.
  - `repos`: Repositories for database operations.
  - `service`: Business logic for cutting and calculations.
  - `ui`: User interface for interacting with the application.
- **`src/test/java`**: Contains unit tests for the application.
- **`pom.xml`**: Maven configuration file.

## Example Usage

1. **Register a New Block**:
   - Enter marble type, dimensions, and status.
2. **Register a New Order**:
   - Specify unit dimensions and quantity.
   - The system calculates the required blocks and reserves them.
3. **Simulate Cuts**:
   - Input cut thickness and unit dimensions to see theoretical results.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any questions or suggestions, feel free to contact the project maintainer.
