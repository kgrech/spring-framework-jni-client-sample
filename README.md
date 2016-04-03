# spring-framework-jni-client-sample

Sample web application based on spring framework with client implementation using JNI. 

Server side collects information about all client machine states (memory usage, cpu loading, number of processes). Clients are configured using XML file. In case of one of the values increases the given limit, e-mail notification is sent.

Following features are demonstrated:

1. Server Side:
  * Spring Boot
  * Spring MVC
  * Spring Data MongoDB
  * Event sending and handling with Spring Framework
  * Extensible XML authoring with Spring
  * Spring fox and Swagger Rest API documentation
  * Spring Test
  * Mocking with mockito library
  * Intergation tests with json path and MockMvc
2. Client Side (Linux only):
  * Usage of Java Native Interface (JNI) to call C++ code from java
  * Reading /proc/loadavg and /proc/meminfo files (linux only)
  * Buildng JNI lib with CMake
  * Rest API call with org.apache.httpcomponents
  * Logging

##Running
1. Install mongo db:
```yum install mongodb*```
2. Install open jdk 8:
```yum install java-1.8.0-openjdk-devel.x86_64```
3. Install maven:
```yum install maven```
4. Start database:
```sudo service mongod start```
5. Build and run server side:
  * ```cd java/server/```
  * ```mvn clean install```
  * ```mvn spring-boot:run```
  * Visit http://localhost:8080/swagger-ui.html. Login: client_1 Password: client_1
6. Build native client library:
  * ```cd ../../native/```
  * ```cmake CMakeLists.txt```
  * ```make```
7. Build and run client:
  * ```cd ../java/client```
  * ```mvn assembly:assembly```
  * ```java -jar target/stat-collector-client-1.0-jar-with-dependencies.jar localhost 8080 client_1```
	
	


