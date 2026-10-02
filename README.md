# jsonplaceholder-auto

A REST API test automation project built with **Java 17**, **REST Assured** and **TestNG**.

It tests the public [JSONPlaceholder](https://jsonplaceholder.typicode.com) API. The main test finds a user by username, fetches every post that user wrote, fetches every comment on those posts, and checks that each comment's email address is correctly formatted.

This is the original version of the task. A refactored version with a custom HTML report lives in [api-automation](https://github.com/niranjankagri/api-automation).

## Tech stack

| Tool | Purpose |
|------|---------|
| Java 17 | Language |
| Maven | Build and dependency management |
| TestNG | Test runner, assertions and soft assertions |
| REST Assured | Sending HTTP requests |
| Hamcrest | Matchers for validating response bodies |
| Gson | Mapping JSON responses onto Java objects |
| Log4j2 | Logging to the console and `log4j-application.log` |
| CircleCI | Continuous integration |

## Project structure

```
jsonplaceholder-auto
├── .circleci/config.yml               CircleCI pipeline: runs the smoke suite
├── application-data.properties        Base URL of the API under test
├── pom.xml                            Maven dependencies and plugins
├── smoke-testng.xml                   TestNG suite run by `mvn test`
└── src
    ├── main/java/com/typicode
    │   ├── AssertUtil                 Hard and soft assertions
    │   ├── Util                       Logger and application-data.properties reader
    │   ├── api
    │   │   ├── APIUser                Fluent entry point: request, latest response, resource APIs
    │   │   ├── APIUserChecker         Checks on the latest response (status code, body)
    │   │   ├── StatusCode             Expected HTTP status codes
    │   │   ├── app                    One class per API resource
    │   │   │   ├── UserApi            /users
    │   │   │   └── post
    │   │   │       ├── PostApi        /posts
    │   │   │       └── comment
    │   │   │           └── CommentApi /comments and the email format check
    │   │   └── lib                    HTTP layer behind interfaces
    │   │       ├── IAPI, IResponse
    │   │       └── restassuredimpl    REST Assured implementation (APIImpl, ResponseImpl)
    │   └── pojo                       User (+ Address, Company), Post, Comment
    ├── main/resources
    │   └── log4j2.properties          Logging configuration
    └── test/java/com/typicode
        ├── AbstractBaseTest           Parent class of all tests
        └── apitests
            ├── Task                   User → posts → comments → email format
            └── APITest                Fetch all users (not in the smoke suite)
```

## How it works

The project has three layers:

1. **Tests** (`apitests`) describe the scenario and contain no HTTP code.
2. **Resource APIs** (`api/app`) wrap one endpoint each. They send the request, check the status code and body, and return POJOs.
3. **HTTP layer** (`api/lib`) sends the requests. `IAPI` and `IResponse` are interfaces, and `restassuredimpl` implements them with REST Assured, so the client library could be swapped without touching the tests.

Tests extend `AbstractBaseTest` and use `api()`, which returns an `APIUser`. Calls chain fluently:

```java
api().users()
     .fetchAllUsers()
     .verify().responseCode(StatusCode.OK, "Fetching User Details")
     .verify().responseBodySafely("id", everyItem(not(emptyOrNullString())), "User ID should not be null or empty");
throwAssertionOnFailure();
```

`responseBodySafely` uses a soft assertion, so a test keeps running and collects every failure. `throwAssertionOnFailure()` at the end of a test reports them all and resets the soft assertions.

### Test scenario: `Task.validateEmailFormatInCommentsOfSearchedUser`

1. Search for the user Samantha (`GET /users?username=Samantha`) and check the status code, id and username.
2. Get all posts written by that user (`GET /posts?userId={id}`) and check each post's id and userId.
3. Get all comments on each post (`GET /comments?postId={id}`) and check each comment's id and postId.
4. Check every comment's email against `^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$`. Soft assertions collect all failures, so one run reports every badly formatted email.

## Prerequisites

- JDK 17 or newer
- Maven 3.6 or newer
- Internet access (the tests call the live JSONPlaceholder API)

## Running the tests

From the project root:

```bash
mvn clean test
```

Maven runs the suite in `smoke-testng.xml` (set by the `suiteXmlFiles` property in `pom.xml`). To run a different suite file:

```bash
mvn clean test -DsuiteXmlFiles=other-testng.xml
```

Results are written to:

- `target/surefire-reports/`: TestNG and Surefire reports (`index.html`, `emailable-report.html`)
- `log4j-application.log`: the full request and assertion log

To run from an IDE, right-click `smoke-testng.xml` or a test class and choose **Run**. Use the project root as the working directory, because `application-data.properties` is read from there.

## Configuration

| What | Where |
|------|-------|
| Base URL | `application-data.properties` → `baseUrl` |
| Username under test | `Task.java` |
| Endpoint paths | the `URL` constant in each resource API class |
| Email format rule | `CommentApi.verifyEmailFormatInComments` |
| Log format and output | `src/main/resources/log4j2.properties` |

## Adding a new test

1. If the response is a new resource, add a POJO under `pojo`.
2. Add a resource API class under `api/app` with its `URL` constant and methods that set the relative URL, call `requestGet()` and verify the response.
3. Add an accessor for it in `APIUser`, like `users()`, `posts()` and `comments()`.
4. Write a test class under `apitests` that extends `AbstractBaseTest` and ends with `throwAssertionOnFailure()`.
5. Add the test class to `smoke-testng.xml` (or a new suite file).

## Continuous integration

`.circleci/config.yml` runs the smoke suite with the `cimg/openjdk:17.0` image, caches Maven dependencies between builds, and shows the TestNG results in the CircleCI **Tests** tab.

## Known limitations

- `APIImpl` sets the global `RestAssured.baseURI`, so tests are not safe to run in parallel.
- The email regex only allows top-level domains of 2 to 4 letters.
- `application-data.properties` is read from the working directory, not the classpath.

Most of these are addressed in [api-automation](https://github.com/niranjankagri/api-automation).
