
**Voting system for deciding where to have lunch**

**Stack**:
Spring Boot 2.6, Lombok, H2, Spring-Data-JPA, Swagger/OpenAPI 3.0

**Description:**
There are 2 types of users: admin and regular users.
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price).
Menu changes each day (admins do the updates).
Users can vote on which restaurant they want to have lunch at Only one vote counted per user If user votes again the
same day:
If it is before 11:00 we assume that he changed his mind. If it is after 11:00 then it is too late, vote can't be
changed.

**Credentials:**
{user:admin@gmail.com, password:admin} {user:user@gmail.com, password:user}

**Launch**:
RestaurantVotingApplication.class

* [Swagger UI](http://localhost:8080/swagger-ui.html)
* [Swagger Api](http://localhost:8080/v3/api-docs)
