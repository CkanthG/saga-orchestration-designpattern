# SAGA ORCHESTRATION DESIGN PATTERN

### Tech Stack
```
Java 21
SpringBoot 3 WebFlux
MySQL
PostgreSQL
Flyway
Kafka
Zookeeper
```

### MicroServices
```
Inventory MS :- 
    To reduce items from inventory If order successfully complated otherwise revert items back to inventory.
Payment MS :- 
    Do payment successfully when order completed successfully otherwise revert amount.
Order MS :- 
    To place orders.
Orchestrator MS :- 
    To manage above microservices and complete the order transaction.
```
