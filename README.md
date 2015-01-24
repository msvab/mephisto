[![Build Status](https://travis-ci.org/msvab/mephisto.svg?branch=master)](https://travis-ci.org/msvab/mephisto)

# Mephisto

## Documentation-Driven HTTP API testing in Java

This framework allows you to verify that HTTP calls to another service are not breaking its contract, as defined by its API documentation.

### Motivation
Many services exposing HTTP API already publish documentation as an artifact of the build process. If the documentation
is done using RAML, Swagger or any similar framework that defines request and response formats, then there is no reason
why we couldn't use that as a representation of the service in our integration tests.

### Use cases
#### 1. Verify that our API documentation is updated together with API changes
In integration tests for our own service, we can validate that all the HTTP calls we make to our service conform with documentation.

At the moment only HTTP calls done through Jersey Client are supported.

````java
JerseyClient client = JerseyClientBuilder.createClient();
client.register(new ContractVerifierFilter(contractFromRaml("api.raml")));

// If the endpoint GET /basic is defined in documentation, the call will work as expected.
// However, if it doesn't exist, 404 Bad Request response will be returned instead with description of the problem.
client.target("http://localhost:8080/basic").request().get();
````

For full example check the `example-jersey-client` module.

#### 2. Verify that all HTTP calls to a 3rd party service conform with its API documentation

TODO