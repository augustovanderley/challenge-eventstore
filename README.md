 
# Implementation of EventStore

My implementation of EventStore that stores events in memory.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [TestNg](https://testng.org/doc/) - A testing framework inspired in JUnit. It supports tests for multi-threaded code and helps writing thread-safe programs.
* [Hamcrest](http://hamcrest.org/) - Framework that assists in writing tests. It allows unit tests to be better written, granting more readability.  
* [Jacoco](https://www.eclemma.org/jacoco/) - A code coverage library. Useful for analysing code coverage and identifying untested code. 

## Running tests

All tests can be executed by running:

> mvn test

Unit Tests are in package `src/test/java/net.interlie.model`.  

Tests in package `src/test/java/net.interlie.concurrency` were done in a multithreaded environment, in order to test if this implementation is thread-safe.    

 
## Running Code Coverage Analysis

Jacoco framework can be executed with maven, by running:  

> mvn jacoco:report

To read the analysis report, open the file at target/site/jacoco/index.html. 

 
