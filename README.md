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
