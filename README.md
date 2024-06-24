To run the project locally follow the guidelines below:

Install MySQL server for connection with the database - https://www.mysql.com/products/workbench/
    (Optional) During installation add MySQL workbench for easier database management and layout
    Start mySQL server locally and authorize
Install IDE for the project. (Recommended Intellij IDEA - https://www.jetbrains.com/idea/)
    Clone the repo and open it as project with the IDE.
    Navigate to application.properties file and adjust the following properties:
        spring.datasource.url - adjust the port your server is running on, and additional parameters
        spring.datasource.username - your username for MySQL
        spring.datasource.password - your password for MySQL
        server.port - check if the predefined port is available on your machine
