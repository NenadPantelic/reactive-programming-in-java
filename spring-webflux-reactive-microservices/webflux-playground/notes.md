## R2DBC

### Database initialization scripts

```
spring.sql.init.data-location=classpath:sql/data.sql
# To show SQL
logging.level.org.springframework.r2dbc=TRACE
```

### Connection string

```
# H2
r2dbc:h2:mem///userdb

# Postgres
r2dbc:postgresql://localhost:5432/userdb

# MySQL
r2dbc:mysql://localhost:3306/userdb
```

### Logging SQL

```
# in JPA
spring.jpa.show-sql=True

# Logging change
logging.level.org.springframework.r2dbc=DEBUG
```

### Verifier

- `StepVerifier.create(...)`
- Next
    - `expectNext(...)`
    - `expectNextCount()`
    - `thenConsumeWhile(...)`
    - `assertNext(...)`
- Complete/Error
    - `expectComplete()`
    - `expectError()`
- Verify (this is what subscribes and runs the test)
    - `verify()`

```
var flux = Flux.just(1, 2)

StepVerifier.create(flux)
    .expectNext(1)
    .expectNext(2)
    .expectComplete()
    .verify();
    
Flux.just(1, 2)
    .as(StepVerifier::create)
    .expectNext(1)
    .expectNext(2)
    .expectComplete()
    .verify();
```

- Reactive programming prefers pure functions with no side effects
    - prefer pure functions, but not blindly everywhere
    - it is ok to mutate objects!

```
// doOnNext is for mutations; operators for an item are not invoked concurrently;
// they are invoked sequentially
customerRepository.findById(1)
        .doOnNext(c -> c.setName("sam"));


// traditional style equivalent
var customer = getCustomer(1);
customer.setName("sam")
```

- Complext queries/Join
    - Prefer SQL: it is efficient, no N+1 problem
    - With Repository:
        - `@Query`
        - database client

```sql
SELECT 
  p.*
FROM
  customer c
INNER JOIN customer_order co ON c.id = co.customer_id
INNER JOIN product p ON p.id = co.product_id
WHERE
  c.name = :name
```

```sql
SELECT
  co.order_id,
  c.name as customer_name,
  p.description AS product_name,
  co.amount,
  co.order_state
FROM
  customer c
INNER JOIN customer_order co ON c.id = co.customer_id
INNER JOIN product p ON p.id = co.product_id
WHERE
  p.description = :description
ORDER BY co.amount DESC
```

- Advantages of DTO

1. model can have some fields, while DTO can omit them - e.g. the user entity contains password field,
   but we do not want to expose it via API
2. DTO can convert field values - e.g. time in database is stored in UTC, while we represent time in the API response in
   local time
3. DTO can be versioned
4. Validation

- reminder: Mono/Flux are publisher types
- To return an error:
    - return Mono<ResponseEntity>
    - returning a Flux<ResponseEntity> does not make sense, Flux should be used to stream data; you cannot say
      chunk A (200), chunk B (400), chunk C (500) etc.
    - status code should be set only once
- `ResponseEntity<Mono<T>>` and `ResponseEntity<Flux<T>>` make the response status and headers known immediately while
  the body is provided asynchronously at a later point
- `Mono<ResponseEntity<T>>` provides all three - response status, headers, and body, asynchronously at a later point
- `Flux` should return 200 status code

- `WebClient` - to send non-blocking HTTP requests
- `WebTestClient` - to write unit/integration tests (`exchange` method - sends the request and gets the result; it's a
  blocking, it is a test, so it's fine)
- `jsonpath` is useful in `WebTestClient`