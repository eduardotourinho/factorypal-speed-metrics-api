# FactoryPal Code Chellenge

## Challege requirements

@See [README](docs/README.adoc)

## How to run the challenge

**API**: `docker-compose up api`

**Tests**: `docker-compose run --rm api-test` - This will run all tests (Unit + Karate tests)

## OpenAPI docs

http://localhost:8080/api-doc

## Tech stack

- Java 11
- SpringBoot Webflux
- Karate (API testing)

## Decisions Rationale

WebFlux was selected mainly for learning. It is my first implementation using reactive API.
Certainly there are some improvements (specially regarding handling Exceptions)