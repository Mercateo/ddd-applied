[![Build Status](https://travis-ci.org/Mercateo/ddd-applied.svg?branch=master)](https://travis-ci.org/Mercateo/ddd-applied) [![Coverage Status](https://coveralls.io/repos/github/Mercateo/ddd-applied/badge.svg?branch=master)](https://coveralls.io/github/Mercateo/ddd-applied?branch=master)

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

