# Group 20 - Weekly Contributions - Milestone 3

## Week 27.3 - 2.4

---

### Louis
https://github.com/sopra-fs23-group-20/client/issues/3 <br />
https://github.com/sopra-fs23-group-20/server/issues/2 <br />
https://github.com/sopra-fs23-group-20/server/issues/1 <br />
https://github.com/sopra-fs23-group-20/client/issues/17 <br />
https://github.com/sopra-fs23-group-20/server/issues/66 <br />
https://github.com/sopra-fs23-group-20/client/issues/18 <br />
https://github.com/sopra-fs23-group-20/server/issues/63 <br />
#### Description:
I finished the login and register pages, the dashboard (which had no issue) has now the needed navbar + cards to navigate and I created the "lobbybrowser".
<br />

---

### Jamo

https://github.com/sopra-fs23-group-20/server/issues/72 <br />
https://github.com/sopra-fs23-group-20/server/issues/81 <br />
https://github.com/sopra-fs23-group-20/server/issues/100 <br />
https://github.com/sopra-fs23-group-20/client/issues/24 <br />

#### Description: 
Guess correctly within 5s --> 100 points. Guess not correct --> 0 points. Points decrease linearly with the remaining time. Players current roundPoint displayed on the screen.

---

### Jonas
https://github.com/sopra-fs23-group-20/client/issues/47 <br />
https://github.com/sopra-fs23-group-20/client/issues/5 <br />
https://github.com/sopra-fs23-group-20/client/issues/8 <br />
https://github.com/sopra-fs23-group-20/client/issues/23 <br />
https://github.com/sopra-fs23-group-20/server/issues/101 <br />
https://github.com/sopra-fs23-group-20/client/issues/48 <br />
https://github.com/sopra-fs23-group-20/client/issues/28 <br />
https://github.com/sopra-fs23-group-20/client/issues/26 <br />
https://github.com/sopra-fs23-group-20/client/issues/13 <br />
https://github.com/sopra-fs23-group-20/server/issues/84 <br />
https://github.com/sopra-fs23-group-20/server/issues/57 <br />

#### Description: 
I completed the project setup, which involved converting the client template to TypeScript and transitioning all components to Material UI components. <br />
I started a first attempt of a websocket connection to link the server with the client on /topic/{userToken}, allowing Hints and TimeUpdates to be transmitted from the server directly to the client without requiring periodic GET requests.<br />
I integrated my personal Raspberry Pi computer into the project's CI/CD workflow, enabling automated deployment to the Raspberry Pi, just like with Google Cloud, after each main branch update. <br />
The Raspberry Pi serves as a backup to the Google Cloud server, because currently, websocket connections don't work on Google Cloud and we also don't have a solution yet for a persistent Database (Jonathan and I set-up a PostgreSQL database on Google Cloud, but it would cost 10$ a day).<br />
Both of these issues are solved by the Raspberry Pi, which is why we are currently exploring both options. 
Access to the project hosted on the Raspberry Pi is available via these URLs:<br />
Client: https://sopra-fs23-group20-client.pktriot.net<br />
Server: https://sopra-fs23-group20-server.pktriot.net

#### Tasks for next Week: 
https://github.com/sopra-fs23-group-20/client/issues/51 <br />
https://github.com/sopra-fs23-group-20/client/issues/50 <br />
https://github.com/sopra-fs23-group-20/client/issues/49 <br /> <br />
For next week, I want to establish an additional websocket connection on /topic/games/{gameId} and improve its stability by checking the websocket's connection's status, attempting to reconnect, and switching to periodic GET requests if necessary (currently necessary on Google Cloud) to keep the game up-to-date for the player. <br />
I also want to create another websocket connection on /topic/gameLobbies to automatically update clients with new lobbies, ensuring the gameLobbyOverview page remains up-to-date without requiring the player to consistently refresh the page manually.

---

### Dario
https://github.com/sopra-fs23-group-20/server/issues/12 <br />
https://github.com/sopra-fs23-group-20/client/issues/35 <br />
https://github.com/sopra-fs23-group-20/client/issues/34 <br />
https://github.com/sopra-fs23-group-20/server/issues/35 <br />
https://github.com/sopra-fs23-group-20/server/issues/69 <br />
https://github.com/sopra-fs23-group-20/client/issues/10 <br />
https://github.com/sopra-fs23-group-20/client/issues/38 <br />
https://github.com/sopra-fs23-group-20/client/issues/39 <br />
https://github.com/sopra-fs23-group-20/client/issues/44 <br />
https://github.com/sopra-fs23-group-20/client/issues/16 <br />

#### Description:
I finished the edit profile page, so that now it is possible to change password, add/change profile picture and nationality. I also added a settings so that lobby creator can choose and view ending criteria (client side).

---

### Jonathan
https://github.com/sopra-fs23-group-20/server/issues/95 <br />
https://github.com/sopra-fs23-group-20/server/issues/96 <br />
https://github.com/sopra-fs23-group-20/server/issues/97 <br />
https://github.com/sopra-fs23-group-20/server/issues/98 <br />
https://github.com/sopra-fs23-group-20/server/issues/99 <br />

#### Description:
I created tests for all functions of the User entity meaning all function of the User Controller, User Service, and User Repository. I also created automated tests for all mappings of all entity that we have so far.
Jonas and I also connected a Postgres Database to Google Cloud.

---
## Week 3.4 - 9.4 (Week 10.4 - 16.4 was OPTIONAL!) 

### Dario
https://github.com/sopra-fs23-group-20/client/issues/33 </br>
https://github.com/sopra-fs23-group-20/server/issues/36 </br>

#### Description:
I added a message that signifies the game's conclusion, implemented the logic for the settings page, and enabled the game to end after a number of rounds chosen by the lobby creator. Additionally, I developed tests for the server repository.

### Jamo
https://github.com/sopra-fs23-group-20/client/issues/22 </br>
https://github.com/sopra-fs23-group-20/client/issues/29 </br>
https://github.com/sopra-fs23-group-20/client/issues/30 </br>
https://github.com/sopra-fs23-group-20/server/issues/40 </br>
https://github.com/sopra-fs23-group-20/client/issues/31 </br>
https://github.com/sopra-fs23-group-20/client/issues/32 </br>
https://github.com/sopra-fs23-group-20/client/issues/54 </br>
https://github.com/sopra-fs23-group-20/server/issues/105 </br>
https://github.com/sopra-fs23-group-20/server/issues/106 </br>
https://github.com/sopra-fs23-group-20/server/issues/108 </br>


### Jonathan
https://github.com/sopra-fs23-group-20/client/issues/59 </br>
https://github.com/sopra-fs23-group-20/server/issues/103 </br>
https://github.com/sopra-fs23-group-20/server/issues/109 </br>
https://github.com/sopra-fs23-group-20/server/issues/111 </br>


### Louis
https://github.com/sopra-fs23-group-20/client/issues/4
</br>
https://github.com/sopra-fs23-group-20/server/issues/91
#### Description:
added funcion that a user can enter a lobby by using the GameID as a code, created code = GameId
#### Additional Tasks:
https://github.com/sopra-fs23-group-20/client/issues/15
</br>
https://github.com/sopra-fs23-group-20/client/issues/53
like described in the email

---
## Week 17.4 - 23.4
## Week 24.4 - 30.4
## Week 1.5 - 7.5
## Week 8.5 - 14.5
## Week 15.5 - 21.5

