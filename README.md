# ddd-applied-demo

Eventsourced DDD example implementation using Spring Boot 2 and Kotlin.

## usage

### get all accounts

```bash
curl http://localhost:8080/accounts
```

### create account

```bash
curl http://localhost:8080/accounts -X POST -d '{"holder": {"name": "test"}}' -H 'Content-Type: application/json'
```

