# ATM
WorkingATM with Docker

This is a working ATM that contains a balance Check and withDraw functionality.

The available endpoints are as follows:

/withdraw 

/balanceCheck

and are used with the below request body.

Sample Requests Body

{
    "userId": "987654321",
    "userPin":"4321",
    "amount" : "74"
}

To run the application you must first run 

mvn install in the main app directory.

You can then start the application using the docker compose file and the command

 docker-compose up --build

Enjoy.




