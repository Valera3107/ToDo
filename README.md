GET

http://localhost:8080/task/  - get all tasks

http://localhost:8080/task/{id}  - get task by id

http://localhost:8080/task/filter/{name}  - get task by name

http://localhost:8080/task/statistic/{date}/{num}  - get statistic about finished task by date(DAY, WEEK, MONTH, YEAR) and num (amount of the days, weeks, months or years)


POST

http://localhost:8080/task/  - create new task


PUT

http://localhost:8080/task/  - update task

http://localhost:8080/task/{id}/{status}  - change status of the task by id and select one of the status value(NEW, RUNNING, ABORTED, FINISHED)


DELETE

http://localhost:8080/task/{id}  - delete task by id  


Running the application locally: 

There are several ways to run a Spring Boot application on your local machine.
One way is to execute the main method in the ua.com.todo.Application class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:

mvn spring-boot:run


kubectl create -f deployment/secrets.yaml
kubectl create -f deployment/mysql-deployment.yaml
kubectl create -f deployment/spring-deployment.yaml


kubectl delete -f deployment/spring-deployment.yaml
kubectl delete -f deployment/mysql-deployment.yaml
kubectl delete -f deployment/secrets.yaml