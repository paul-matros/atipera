# GitHub Repository Browser API

A Quarkus-based REST API that fetches and serves non-fork GitHub repositories and their branches for a given username.

## Features

- Fetch GitHub repositories for a specific user
- Filter out forked repositories
- Retrieve branches and their latest commit information for each repository
- API versioning for maintainability
- Return appropriate error responses when a user is not found

## Technical Stack

- [Quarkus](https://quarkus.io/) - A Kubernetes Native Java framework
- [Mutiny](https://smallrye.io/smallrye-mutiny/) - Reactive programming model
- [RESTEasy Reactive](https://quarkus.io/guides/resteasy-reactive) - RESTful web services
- [MicroProfile REST Client](https://quarkus.io/guides/rest-client-reactive) - Type-safe REST client
- [Lombok](https://projectlombok.org/) - Java annotation library to reduce boilerplate code
- [Jackson](https://github.com/FasterXML/jackson) - JSON processing

## API Endpoints

### Get User's Repositories

GET /api/v1/repositories/{username}
Copy
Returns a list of non-fork repositories for the specified GitHub user, including branch information.

#### Response Format

```json
[
  {
    "name": "repository-name",
    "ownerLogin": "username",
    "branches": [
      {
        "name": "branch-name",
        "lastCommitSha": "commit-sha-hash"
      }
    ]
  }
]
```

#### Error Responses

User Not Found (404)

```json
{
  "status": 404,
  "message": "User 'username' not found"
}
```

## Building the Application

### Prerequisites

* JDK 17 or later
* Maven 3.8.1 or later

## Build Commands

### Package the application

```bash
./mvnw package
```

### Run the application in development mode

```json
./mvnw quarkus:dev
```

This enables live coding and hot deployment for development purposes.

### Build a native executable (optional, not tested)

```json
./mvnw package -Pnative
```

## Configuration

The application is configured to use GitHub's public API without authentication, which is sufficient for development
purposes and respects GitHub's rate limits. No GitHub token is required to run the application.

## GitHub API Client Configuration

The GitHub API client is configured in application.properties:

```properties
quarkus.rest-client.github-api.url=https://api.github.com
quarkus.rest-client.github-api.scope=jakarta.inject.Singleton
```

## Testing

The project includes an integration test that verifies the application's behavior using the public GitHub API with the "
octocat" test user:

```bash
./mvnw test
```

## Note on API Rate Limits

When running tests or using the application extensively, be aware of GitHub's API rate limits for unauthenticated
requests (60 requests per hour). If you need to increase this limit for development purposes, you can configure a GitHub
personal access token.

## API Versioning

The API uses versioning (currently v1) to ensure backward compatibility as the API evolves. All endpoints are prefixed
with /api/v1/.

## Development Notes

* No GitHub token was added as it wasn't necessary for development within the rate limit constraints. No unnecessary
  features were added as per task constraints.
* The application follows a reactive programming model using Mutiny for asynchronous operations.
* The model classes should ideally be records as well, similar to the DTOs, but this conversion was not implemented due
  to
  time constraints.