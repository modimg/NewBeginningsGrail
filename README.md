# Grail New Beginnings - Participant Microservice Project

This is a REST API based on Java (1.8) Spring Boot (2.6.1) and Maven that can be used as a submission as a part of interview process with Grail.

### REST API

The NewBeginnings REST application provides CRUD services for participant's personal information (as mentioned in question pdf). It uses an in-memory database (H2) to store the data.

Assumptions:
- Do not need to implement authentication and authorization.
- Do not need to worry about persistence once the service shuts down.
- Implemented ParticipantUtils class to generate participant unique reference number.
  In actual - the application should call separate microservice to allocate unique reference number.

```
  public static String getParticipantId(){
  String id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
  id = id.substring(0,3) + "-" + id.substring(3,6);
  return id;
  }
```

- It stores up to 2 address lines in database.
- It stores CELL, WORK and HOME phone number (for SMS, CALL). If phone number is provided, then it will always have 10 digits. 
- Standardizing the phone number with format ###-###-####.

### Data Model:
     - Implemented 2 tables to store participants personal information.
     
     - TABLE: PARTICIPANT
       COLUMNS:
          PARTICIPANT_ID (VARCHAR2)
          FIRST_NAME (VARCHAR2)
          MIDDLE_NAME  (VARCHAR2)
          LAST_NAME (VARCHAR2)
          DOB (DATE)
          HOME_PHONE_NO (VARCHAR2)
          WORK_PHONE_NO (VARCHAR2)
          CELL_PHONE_NO (VARCHAR2)
          ADDRESS_ID (LONG)
  
     - TABLE: ADDRESS
       COLUMNS:
          ADDRESS_ID (LONG)
          ADDR_LINE1 (VARCHAR2)
          ADDR_LINE2 (VARCHAR2)
          CITY (VARCHAR2)
          STATE (VARCHAR2)
          COUNTRY (VARCHAR2)
          ZIP (VARCHAR2)


### Trade Offs:
  - The data model is consistent with requirements, but it is not as normalized as one can use in production application.
    <br>
  - Participant/Address join
      - Currently, address id is part of participant table. It means one can generate duplicate records in Address table (as it has one to one relationship with participant).
      - Ideally there should be separate table consisting of ADDRESSID and PARTICIPANTID. This will reduce the duplication creation and better data management/quality.
   <br>
        <br>
  - Communication Table:
      - There should be separate COMMUNICATION table which contains all communicated related values for the given participant reference number - PARTICIPANT_ID.
      - This will help us to include all other communication like  EMAIL, SOCIAL PLATFORM URLs etc.
      - This will also give us opportunity to create primary and secondary communications.
   
        <br>
  - LOV (List of Values):
      - Values for COUNTRY and STATE columns in ADDRESS table should be store in LOV tables.
      - This will help us to store only valid values for these columns in database.
      

### Missing Validations:
PARTICIPANT:
- Not checking if phone number is less than 10 digits or not.
- Not considering the country code for the phone number.
  <br>

ADDRESS:
- Values for State, Country are not validated with LOV tables.
- Missing standardization for the zip code.
- Does not check if the address have mandatory values - address line1, zip code, country, state.
  <br>

### Application Highlights:

* Implemented REST using lates Spring Framework: inversion of control, dependency injection, etc.
* Packaging as a single war with embedded container (tomcat 8): No need to install a container separately on the host just run using the ``java -jar`` command.
* Writing a RESTful service using annotation: supports JSON request / response.
* Spring Data Integration with JPA/Hibernate - @Entity.
* Automatic CRUD functionality against the data source using Spring Repository.
* Demonstrates MockMVC test framework with associated libraries.
* All APIs are documented by Swagger2 using annotations.

### How to Run

As this REST API application is built using spring boot, it is packaged as a war which has Tomcat 8 embedded. One should not need to install separate application server.

* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar -Dspring.profiles.active=test target/NewBeginningsGrail-1.0-SNAPSHOT.war
or
        mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
```

* Once the application runs you should see something like this

```
2022-03-21 00:14:42.938  INFO 38996 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-03-21 00:14:43.266  INFO 38996 --- [           main] com.grail.nb.NewBeginnings               : Started NewBeginnings in 5.772 seconds (JVM running for 6.371)
```

### Log
* Check newbeginnings.log file for application log
* Below log levels are set:<br>
  logging.group.nb=com.grail.nb.controller, com.grail.nb.service, com.grail.nb.util<br>
  logging.level.nb=WARN<br>
  logging.level.org.springframework.web=DEBUG<br>
  logging.level.org.hibernate=ERROR<br>



### To view API docs

Run the server and browse to http://localhost:8080/swagger-ui.html

### To view your H2 in-memory datbase

The 'test' profile runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8080/h2-ui. Default username is 'nb' with a blank password.


### Attaching to the app remotely from your IDE

Run the service with these command line options:

```
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=4000"
or
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=4000 -Dspring.profiles.active=test -Ddebug -jar target/NewBeginningsGrail-1.0-SNAPSHOT.war
```
and then you can connect to it remotely using your IDE. For example, from IntelliJ You have to add remote debug configuration: Edit configuration -> Remote.


### CRUD endpoints:

### Create a participant

```
POST /api/v1/participant
Accept: application/json
Content-Type: application/json

{
  "fname": "Mitesh",
  "mname": "G",
  "lname": "Modi",
  "dob": "1983-12-24",
  "cellphoneno": "9372319655",
  "address": {
    "addrLine1": "1200 Flip Trail",
    "addrLine2": "",
    "city": "Cary",
    "state": "NC",
    "zipCode": "27513",
    "country": "INDIA"
  }
}

RESPONSE: HTTP 201 (Created)
{
  "participantId": "OTA-NRQ",
  "fname": "Mitesh",
  "mname": "G",
  "lname": "Modi",
  "dob": "1983-12-24",
  "cellphoneno": "937-231-9655",
  "homephoneno": "",
  "workphoneno": "",
  "address": {
	"addressId": 2,
	"addrLine1": "1200 Flip Trail",
	"addrLine2": "",
	"city": "Cary",
	"state": "NC",
	"zipCode": "27513",
	"country": "INDIA"
  }
}
```

### Update existing participant's information

```
PUT /api/v1/participant/{id}
	e.g.: PUT /api/v1/participant/OTA-NRQ

Accept: application/json
Content-Type: application/json

{
  "participantId": "OTA-NRQ",
  "fname": "Mitesh",
  "mname": "Girishchandra",
  "lname": "Modi",
  "dob": "1983-12-24",
  "cellphoneno": "937-231-9655",
  "homephoneno": "",
  "workphoneno": "",
  "address": {
	"addressId": 2,
	"addrLine1": "2013 Mavin Place",
	"addrLine2": "",
	"city": "Durham",
	"state": "NC",
	"zipCode": "27703",
	"country": "USA"
  }
}


RESPONSE: HTTP 200 (No Content)

{
  "participantId": "OTA-NRQ",
  "fname": "Mitesh",
  "mname": "Girishchandra",
  "lname": "Modi",
  "dob": "1983-12-24",
  "cellphoneno": "937-231-9655",
  "homephoneno": "",
  "workphoneno": "",
  "address": {
	"addressId": 2,
	"addrLine1": "2013 Mavin Place",
	"addrLine2": "",
	"city": "Durham",
	"state": "NC",
	"zipCode": "27703",
	"country": "USA"
  }
}

```
### Get existing participant's information

```
GET /api/v1/participant/{id}
	e.g.: GET /api/v1/participant/OTA-NRQ

Accept: application/json
Content-Type: application/json

RESPONSE: HTTP 200 (No Content)

{
  "participantId": "OTA-NRQ",
  "fname": "Mitesh",
  "mname": "Girishchandra",
  "lname": "Modi",
  "dob": "1983-12-24",
  "cellphoneno": "937-231-9655",
  "homephoneno": "",
  "workphoneno": "",
  "address": {
	"addressId": 2,
	"addrLine1": "2013 Mavin Place",
	"addrLine2": "",
	"city": "Durham",
	"state": "NC",
	"zipCode": "27703",
	"country": "USA"
  }
}

```
### Delete existing participant

```
DELETE /api/v1/participant/{id}
	e.g.: DELETE /api/v1/participant/OTA-NRQ

Accept: application/json
Content-Type: application/json

RESPONSE: HTTP 200 (No Content)

{
	"deleted":true
}
```

### Retrieve a full list of participants (paginated)

```
GET /api/v1/participant/findAll?page=0&size=10

Response: HTTP 200
Content: paginated list 
```
### Project Dependencies
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot</artifactId>
            <version>2.6.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>2.6.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.0.204</version>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.5.0</version>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.5.0</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.6.0</version>
        </dependency>

### Questions and Comments: mitesh1224@gmail.com


