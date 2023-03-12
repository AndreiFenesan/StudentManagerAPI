# Student Manager API

Student Manager API is a **Rest API** that helps you to manage
student grades.

## Technologies used:
1. Spring
2. MongoDb

## Key features:
* Easily manage CRUD operation on students, courses and grades. 
* Token based authentication for professors and students.
* Automatic sending of emails to students who receive a new grade.

## How it works:
This application uses the Spring framework and MongoDb for building a Rest API.

The user makes requests to the endpoints, and if he is authorized (the user is logged in as a student or as a professor), then his request will be processed and a response will be given. For a user to be logged in, he must send his username and password to the **/api/login/student** or **/api/login/professor** for validation and receive authentication and refresh tokens. Every token is available only for 5 minutes. Before the token expires, the client has to send the token and refresh token to  **/api/login/renewtoken** for generating another one.
### Endpoints:
1. /api/students
    * available methods:
      * Get for getting all students from a specific group.
      * Post for adding a new student
2. /api/subject    
    * available methods:
      * Post for adding a new subject
3. /api/grade/studentGrades
    * available methods:
        * Get for receiving all grades of a student.
4. /api/grade/professorGrades
   * available methods:
       * Post for adding a new grade.
       * Delete for deleting a grade.
5. /api/token/login/student
    * available methods:
      * Post for getting tokens for students.
6. /api/token/login/professor
    * available methods:
        * Post for getting tokens for professors.
7. /api/token/renewtoken
    * available methods:
        * Post for refreshing your tokens before expiration.
  
    


