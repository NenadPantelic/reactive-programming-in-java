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

#### Error handling

- Spring framework kind of subscriber to the publisher type
- when the controller throws an exception, it is passed to a controller advice which converts the exception
  to response
- RFC 7807/RFC 9457
  - structured error response
  - machine/human-readable

##### Problem Details

| Properties | Description                                                                                                                 |
| ---------- | --------------------------------------------------------------------------------------------------------------------------- |
| type       | A link to the documentation for the callers to read more about the problem. If it is not provided, "about:blank" is assumed |
| title      | Human readable summary of the problem                                                                                       |
| status     | HTTP status code                                                                                                            |
| detail     | Detailed message specific to the problem                                                                                    |
| instance   | The URI which caused the problem                                                                                            |

### R2DBC vs JPA/JDBC

- `Schedulers.boundedElastic()` - to offload synchronous calls
- R2DBC fetches rows lazily, only as requested by the subscriber. In that way, it handles the backpressure in reactive
  streams
- concat vs merge in reactor (concat sequentially merge streams one-by-one as they are complete)
  ![](images/concat.png)
  ![](images/merge.png)
- what happens if a downstream subscriber cancels a flux while rows are still being streamed from R2DBC?
  A: The driver stops fetching further rows and cancels the underlying query

### WebFilter

- an intermediary component between the server and the controller. It can manipulate the incoming request and outgoing
  response.
- useful to handle the cross-cutting concerns: authentication, authorization, logging, monitoring, rate limiting
- we can access path, header, parameters, cookies...etc
- do not do request body validation! - the request body is deserialized in controller layer
- we can chain multiple `WebFilter` to do multiple validations before the request reaches the controller

- Assignment:

  - we have two types of callers: `STANDARD` and `PRIME`
  - Authn requirements - 401:
    - all the callers are expected to provide a security header as part of requests - "auth-token"
    - value
      - "secret123" - `STANDARD`
        - "secret456" - `PRIME`
  - AuthZ requirements - 403:
    - `STANDARD` - allowed to make only `GET` requests
    - `PRIME` - allowed to make any types of calls

- one filter can pass some attributes to another filter: `exchange.getAttributes().put(key, value)`
- accessing attributes in controller: `@RequestAttribute(<attribute-name>) <attribute-type> attribute`

##### Functional endpoints

```java
route()
        .GET("/customers",handler::getAllCustomers)
        .GET("/customers/{id}",handler::getCustomer)
        .POST("/customers",handler::saveCustomer)
        ...
        .onError(CustomerNotFoundException.class,this::badRequestHandler)
        .build();
```

### WebClient

- Reactor based fluent API for making HTTP requests
  - wrapper around reactor-netty
- Non-blocking
- Immutable
- Thread-safe!
- retrieve method - sends the request and receives a response in a non-blocking manner

- Wrapper around reactor-netty

  - it uses 1 thread/CPU
  - it is non-blocking

- Asynchronous event loop

  - requests are sent without through an outbound queue
  - the async code client does not block the current thread until the response comes back, it simply moves forward
  - once the response comes through an inbound queue thread accepts it and processes it

- URI variables

```java
this.client.get()
        .uri("/lec01/product/{id}",i)
        .retrieve()
        .bodyToMono(Product.class);

        this.client.get()
        .uri("/{lec}/product/{id}","lec01",i)
        .retrieve()
        .bodyToMono(Product.class);

        var map=Map.of(
        "lec","lec01",
        "id",1);
        this.client.get()
        .uri("/lec01/product/{id}",map)
        .retrieve()
        .bodyToMono(Product.class);
```

#### retrieve vs exchange

- exchange will allow you to get headers, cookies and status code (through `ClientResponse`)

##### Exchange filter function

- it is applied on the client side as a client filter
- handling the cross-cutting concerns (logging, monitoring, setting, authentication, token...) the same way the filter
  in server does

```java
// ExchangeFilterFunction
Mono<ClientResponse> filter(ClientRequest request,ExchangeFunction next);
```

- mutate the WebClient

```java
import org.springframework.context.annotation.Bean;

@Bean
public WebClient orderClient(){
        return WebClient.builder()
        .baseUrl("http://order-service.com")
        .build();
        }

        var newClient=oldClient.mutate()
        .baseUrl("http://new-base-url")
        .build();
```

- `bodyValue` - for things that is already in memory
- `body` - for the publisher who should emit data

## Reactive data streaming: high-volume uploads & downloads

- 4 types of communication
  - request -> response
  - request -> streaming response
  - streaming request -> response
  - streaming request -> streaming response
- Use case: Create a million products
- Traditional approach:
  - POST
    - Increased network traffic/latency
    - unnecessary wait time
    - redundant validation
  - CSV file upload
    - file could be corrupt
    - "," escape
    - complex nested data structure
- Streaming approach
  - set up a connection once and keep sending the messages in a streaming fashion
  - no need to wait for previous request to complete
  - reduced network traffic/latency
  - use JSON to create a product

#### JSON lines

- JSON array - good for smaller data sets, but you have millions of products, then it will have to keep the whole JSON
  array in memory
  (it has to store the closing bracket in memory to make a valid JSON object)
- JSON line - ND-JSON

  - new line delimited; each line is 1 JSON
    - self-contained
    - easy to parse
    - great for streaming!
    - massive datasets!

- @RequestBody - non-blocking construct

- @RequestBody will wait for the whole body content to be received before deserializing into a native API

```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostMapping
public Mono<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customer){
        }
```

```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostMapping
public Mono<CustomerDTO> saveCustomer(@RequestBody Mono<CustomerDTO> customer){
        }
```

## Server Sent Events (SSE/EventSource)

- if you have the election results platform or a site showing the result of a very popular game we will have so many
  requests (frequent requests). A big part of that would be waste requests, no update of results
- SSE
  - Stream events from backend to frontend
  - one-way communication
  - for SSE - this is a MediaType that browsers do understand - `MediaType.TEXT_EVENT_STREAM_VALUE`

## High performance

### gzip

- if the response is big (in KBs), the client might observe increased response time. The apps might appear to be
  performing poorly will respond slowly, but it could be just network latency
- gzip: technique to compress the response before sending over the network; the size is reduced, it will reach the
  client sooner
- it works well in a congested network + response size is large
- Note:

  - server requires additional processing to compress
  - it might have negative effect when the response size is small!
  - do NOT use local machine to test! You will NOT see any improvement! (it is for network latency)

- gzip properties
  - server side
  ```java
  server.compression.enabled=true
  # the response size must be at least 2048 bytes
  server.compression.min-response-size=2048
  # apply it to these MIME types only
  server.compression.mime-types=application/json,application/xml
  ```
  - client side
  ```
  Accept-Encoding: gzip
  ```
- performance has to be measured!

### Connection Pooling

- Connection setup takes time!
- no of connections / avg response time = expected throughput
- Keep-alive - to reuse connections!
- HTTP/1.1

  - 1 connection per request; if you send a request, you cannot send another request until the response comes back
  - 3-step TCP handshake
  - when the connection is set up, the OS exposes an outbound port, e.g. 53123, it hits the remote machine port
  - the server will process the request and flush it, so the response will be sent via that connection
  - if the server needs too much time to respond, that connection is occupied, and we cannot hit another request to
    that remote server until the original request is handled, i.e. that response is received.
  - to fire another request, the OS will have to set up a new outbound port to fire another request

- Handy commands
  - `netstat -an | grep -w 127.0.0.1.7070`
  - to watch `watch 'netstat -an | grep -w 127.0.0.1.7070'`

### HTTP/2

- HTTP/1.1 - 1997
  - needs one connection per request
- HTTP/2 - 2015
  - Google introduced; they used Spidey internally and then they proposed HTTP/2
  - Pros
    - Multiplexing - it needs just one connection, it can send multiple concurrent requests just by using one single
      connection
    - Binary protocol; whereas HTTP/1.1 is textual
    - Header compression (smaller requests and responses)

```
# with this the server will support both HTTP/1.1 and HTTP/2
server.http2.enabled=true
```

- H2 (SSL/TLS enabled), H2C (clear text)

- when you create reactive system, try to use reactive libraries for your application!
  - R2DBC, Mongo, Redis, Kafka, Pulsar, ElasticSearch...
  - what if I do not have? How to make it reactive?
  ```java
    Mono.fromSupplier(() -> yourlib.invokeMethod())
    ...
    .subscribeOn(Scheduler.boundedElastic()) // very important!
  ```

##### Summary:

- use connection pooling to reuse TCP connections between requests
- to reduce the response size, use gzip compression (for larger requests, since gzip will increase the CPU usage)
- to reduce the connection setup/overhead use the connection pool with keep-alive
- HTTP/2 brings multiplexing - requests reuse the same TCP connection
- the client has to send `Accept-Encoding: gzip` header to accept responses compressed with gzip
