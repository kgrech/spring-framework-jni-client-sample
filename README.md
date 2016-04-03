# spring-framework-jni-client-sample

Demo of the features of spring framework + client implementation with JNI

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
	
	


