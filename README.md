### Goals with a Spring Boot refactor

* Having a more clean and concise code base, using less custom solutions when possible
* Rewrite Gatekeeper module to rely on RabbitMQ brokers to do inter-cloud communications
* Easier library version updates with Spring Boot starters
* Using the new features in JDK9-11 (for example module system), as JDK 11 is a Long Term Support version
* A new project structure that would support a lightweight implementation of Arrowhead in the same code base as the main implementation
* Better REST API documenting
* Meaningful unit and integration tests