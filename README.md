# Load Balancer

A simple yet effective load balancer implementation in Java that demonstrates various load balancing strategies and socket handling.

## Features

- Multiple load balancing strategies:
  - Round Robin
  - Least Connections
- Health check mechanism for backend servers
- Thread-safe implementation
- Configurable server list
- Exception handling for network operations
- Unit tests with Mockito

## Architecture

The load balancer follows a modular design with the following components:

1. **LoadBalancer**: Main class that manages the load balancing strategies
2. **LoadBalancingStrategy**: Interface defining the contract for load balancing algorithms
3. **RoundRobinStrategy**: Implementation of round-robin load balancing
4. **LeastConnectionsStrategy**: Implementation of least connections load balancing
5. **ClientSocketHandler**: Handles client connections and data transfer

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

```bash
mvn clean install
```

## Running the Load Balancer

```bash
mvn exec:java -Dexec.mainClass="org.example.LoadBalancer"
```

## Configuration

The load balancer can be configured by modifying the server list in the code. Currently, it uses a simple list of backend servers:

```java
List<String> servers = Arrays.asList("IP1", "IP2");
```

## Load Balancing Strategies

### Round Robin
- Distributes requests evenly across all available servers
- Simple and effective for servers with similar capacity
- Thread-safe implementation using AtomicInteger

### Least Connections
- Routes requests to the server with the least number of active connections
- Better for servers with varying capacities
- Maintains connection count for each server

## Testing

The project includes comprehensive unit tests:

```bash
mvn test
```

Test coverage includes:
- Load balancing strategies
- Socket handling
- Error scenarios
- Health checks

## Error Handling

The load balancer handles various error scenarios:
- Server unavailability
- Connection failures
- Socket closures
- Network timeouts

## Thread Safety

The implementation is thread-safe using:
- AtomicInteger for counters
- ConcurrentHashMap for server health tracking
- Thread-safe collections

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Akshat Gupta

## Future Improvements

Potential areas for enhancement:
1. Add more load balancing strategies (Weighted Round Robin, IP Hash)
2. Implement server health monitoring
3. Add metrics collection
4. Add configuration file support
5. Implement SSL/TLS support
6. Add logging and monitoring
7. Implement circuit breaker pattern
8. Add support for dynamic server registration 