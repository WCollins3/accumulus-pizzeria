# Accumulus Pizzeria

## Cast your vote for toppings
Send a POST request to the `/topping` endpoint with your email address and a list of toppings to cast your vote

## Read the results
Sent a GET request to the `/topping/votes` to see the votes that people have cast

# Start-up method (Requires Docker)
To start up the service, [docker](https://docs.docker.com/desktop/) must be installed on your system.
Run the following command with docker installed (in command prompt on windows, or in a unix terminal on mac and linux)

```docker-compose up --build```

This will create a container which has a postgres database and the pizzeria app running. The database
volume will be stored to be re-used after shutting the container down.

## Alternative start-up method (requires maven, java 17, and an already-running database)
If you already have an empty database running, you can run the service without docker by setting the
following environment variables:

SPRING_DATASOURCE_URL: A JDBC URL for your database. Example: `jdbc:postgresql://localhost:5432/pizzeria`

SPRING_DATASOURCE_USERNAME: A username registered in your database with read and write access

SPRING_DATASOURCE_PASSWORD: The password for the username mentioned above

SPRING_DATASOURCE_DRIVER: The driver class name that spring needs to use for database connections. For
example, the postgres driver class name is `org.postgresql.Driver`

You can build the jar with the following command: `mvn clean package -DskipTests`