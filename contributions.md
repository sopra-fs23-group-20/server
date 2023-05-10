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

---

### Dario
https://github.com/sopra-fs23-group-20/client/issues/33 </br>
https://github.com/sopra-fs23-group-20/server/issues/36 </br>

#### Description:
I added a message that signifies the game's conclusion, implemented the logic for the settings page, and enabled the game to end after a number of rounds chosen by the lobby creator. Additionally, I developed tests for the server repository.


---

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

#### Description:.
I mainly worked on the scoreboard and its corresponding features. The scoreboard will now appear after each round showing the points and ranks of each player. At the end the final Scoreboard with the corresponding points will be displayed. 
(I have done a lot in this week or worked ahead, because I will have less time in May due to my job --> will be compensated in May!)

---

### Jonathan
https://github.com/sopra-fs23-group-20/client/issues/59 </br>
https://github.com/sopra-fs23-group-20/server/issues/103 </br>
https://github.com/sopra-fs23-group-20/server/issues/109 </br>
https://github.com/sopra-fs23-group-20/server/issues/111 </br>

---

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

### Jonas

https://github.com/sopra-fs23-group-20/server/issues/58 </br>
https://github.com/sopra-fs23-group-20/client/issues/49 </br>
https://github.com/sopra-fs23-group-20/client/issues/50 </br>
https://github.com/sopra-fs23-group-20/server/issues/48 </br>

#### Description:
I refactored the backend using the State pattern for the different game states (SETUP, GUESSING, SCOREBOARD, ENDED) and implemented websocket connections on /topic/games/{gameId} and /topic/games/{gameId}/{userId} to send game updates to the players.

#### Tasks for next week:
https://github.com/sopra-fs23-group-20/client/issues/63 (Bug fix: The current round in the game allways says "Nan") </br>
https://github.com/sopra-fs23-group-20/client/issues/64 (When joining a lobby via link without being logged in the user should be redirected to the register page and then after registering automatically be redirected to the game he/she tried to join) </br>
https://github.com/sopra-fs23-group-20/client/issues/51  (There should be a websocket connection on /topic/gameLobbies where newly created lobbies should be sent to the client so the gameLobbyOverview page is always up-to-date without needing to manually refresh the page) </br>


---
## Week 17.4 - 23.4

---

### Dario
https://github.com/sopra-fs23-group-20/client/issues/70 (work ahead for next week) <br />
https://github.com/sopra-fs23-group-20/client/issues/71 (work ahead for next week) <br />
https://github.com/sopra-fs23-group-20/server/issues/115 <br />
https://github.com/sopra-fs23-group-20/server/issues/116 <br />
https://github.com/sopra-fs23-group-20/client/issues/61 (work ahead for next week) <br />
https://github.com/sopra-fs23-group-20/server/issues/110 <br />
https://github.com/sopra-fs23-group-20/client/issues/80  <br />
https://github.com/sopra-fs23-group-20/server/issues/121 (work ahead for next week) <br />

#### Description:
I created a Rules page that displays the rules of the game, then I added the possibility to select regions and hints as a lobby creator, and also the feature that only the lobby creator sees the button to start the game. I also added that the game can only be started if at least one region and one hint are selected, and added the possibility to randomize the order acccording to which the hints are displayed (button is disabled when only one hint is selected because randomizing one hint has no effect). I also wrote tests for the CountryCountroller and CountryService classes.

### Louis
https://github.com/sopra-fs23-group-20/client/issues/62 <br />
https://github.com/sopra-fs23-group-20/client/issues/68 <br />
https://github.com/sopra-fs23-group-20/client/issues/58 <br />
https://github.com/sopra-fs23-group-20/client/issues/65 <br />
https://github.com/sopra-fs23-group-20/server/issues/114
#### Description:
added a guide as a tutorial for the users, added avatars to the profile page, added QR code to share a game session, added accordion to the gameSetup page, tried to fix the layout for mobile

---

### Jonas
https://github.com/sopra-fs23-group-20/client/issues/84 <br />
https://github.com/sopra-fs23-group-20/client/issues/66 <br />
https://github.com/sopra-fs23-group-20/client/issues/64 <br />
https://github.com/sopra-fs23-group-20/client/issues/63 <br />
https://github.com/sopra-fs23-group-20/client/issues/65 <br />

#### Description:
I added a selection for the difficulty level between Easy, Medium and Hard based on the country's total population. <br />
I also added the option to register as a "Guest" user and let the user automatically after being redirected to the register screen be redirected back to the game he/she tried to join.

#### Tasks for next week:
Prepare the presentation for M3 <br />
https://github.com/sopra-fs23-group-20/client/issues/51  (There should be a websocket connection on /topic/gameLobbies where newly created lobbies should be sent to the client so the gameLobbyOverview page is always up-to-date without needing to manually refresh the page) </br>
https://github.com/sopra-fs23-group-20/client/issues/85 (The time Left in a round should be displayed as a horizontal bar which decreases every second.) </br>
https://github.com/sopra-fs23-group-20/server/issues/70 (No new players can join the game once it has started)  </br>


---

### Jamo
https://github.com/sopra-fs23-group-20/client/issues/89 <br />
https://github.com/sopra-fs23-group-20/client/issues/90 <br />
https://github.com/sopra-fs23-group-20/client/issues/91 <br />
https://github.com/sopra-fs23-group-20/client/issues/92 <br />
https://github.com/sopra-fs23-group-20/client/issues/93 <br />

#### Description:
I created the Leaderboard from scratch. In the leaderboard you can see, how many games the players have won! For the implementation I chose the approach with generic functional components which made it a bit more difficult. Furthermore, I designed the leaderboard and fixed the ranking order in the Score- and Leaderboard.

---


### Jonathan
https://github.com/sopra-fs23-group-20/client/issues/76 <br />
https://github.com/sopra-fs23-group-20/client/issues/72 <br />
https://github.com/sopra-fs23-group-20/client/issues/73 <br />
https://github.com/sopra-fs23-group-20/client/issues/75 <br />
https://github.com/sopra-fs23-group-20/client/issues/77 <br />
https://github.com/sopra-fs23-group-20/client/issues/78 <br />
https://github.com/sopra-fs23-group-20/client/issues/81 <br />

https://github.com/sopra-fs23-group-20/server/issues/117

## Week 24.4 - 30.4

---

### Dario
https://github.com/sopra-fs23-group-20/client/issues/70 <br />
https://github.com/sopra-fs23-group-20/client/issues/71 <br />
https://github.com/sopra-fs23-group-20/client/issues/61 <br />
https://github.com/sopra-fs23-group-20/server/issues/121 <br />
https://github.com/sopra-fs23-group-20/client/issues/99 <br />

#### Description: 
As I was away from wednesday until the end of the week, most of the tasks are the ones I did last week as work ahead for this week, but I was still able to manage to find time to fix a bug that occured when registering and did also write unit tests for the entities and also tests for the state pattern classes.

### Jamo
https://github.com/sopra-fs23-group-20/client/issues/100 <br />
https://github.com/sopra-fs23-group-20/client/issues/98  <br />
https://github.com/sopra-fs23-group-20/client/issues/95  <br />
https://github.com/sopra-fs23-group-20/client/issues/94  <br />

#### Description: 
I modified the Leaderboard, such that it shows three trophies as it is the case in the Scoreboard (uniformity). Furthermore, I worked on the Scoreboard (e.g. added that User can see which country they guessed).

### Jonathan
https://github.com/sopra-fs23-group-20/client/issues/101 <br />
https://github.com/sopra-fs23-group-20/client/issues/60 <br />
https://github.com/sopra-fs23-group-20/client/issues/103 <br />


### Louis
https://github.com/sopra-fs23-group-20/server/issues/122 <br />
https://github.com/sopra-fs23-group-20/client/issues/88 <br />
https://github.com/sopra-fs23-group-20/client/issues/86 <br />


#### Description: 
The game lobby server only returns now joinable game lobbies, so I had to create a new end point. I also added a tutorial button on the main page, which shows a small guide via a popover.

---

### Jonas
https://github.com/sopra-fs23-group-20/server/issues/124 <br />
https://github.com/sopra-fs23-group-20/client/issues/97 <br />
https://github.com/sopra-fs23-group-20/server/issues/112 <br />

#### Description:
I created a context provider component "AlertProvider" which displays an error message using Material UI's Snackbar component. I then replaced all our current error messages with this component. <br />
I fixed a bug which displayed the scores wrong in the Scoreboard. <br />
I created a "Complex Unit Test" and described it for the M3 report. <br />

#### Tasks for next week:
https://github.com/sopra-fs23-group-20/client/issues/96 Let the game Creator choose the duration of how long the scoreboard should be shown (by default 7 seconds). <br />
https://github.com/sopra-fs23-group-20/server/issues/70 No new players can join the game once it has started. <br />
https://github.com/sopra-fs23-group-20/client/issues/85 The time Left in a round should be displayed as a horizontal bar which decreases every second. <br />
https://github.com/sopra-fs23-group-20/client/issues/51 There should be a websocket connection on /topic/gameLobbies where newly created lobbies should be sent to the client so the gameLobbyOverview page is always up-to-date without needing to manually refresh the page. <br />


---

## Week 1.5 - 7.5

---

### Jamo
I have already submitted my tasks for this week in week 3.4 - 9.4/easter break.

---

### Louis
I have already submitted my tasks for this week in week 3.4 - 9.4/easter break.

---

### Jonathan
https://github.com/sopra-fs23-group-20/client/issues/59 <br />
https://github.com/sopra-fs23-group-20/client/issues/118 <br />
https://github.com/sopra-fs23-group-20/server/issues/134 <br />
https://github.com/sopra-fs23-group-20/server/issues/138 <br />


#### Description:
I updated the rules and guideline page formating and styling textual and picture vise. I also did the Readme file and changed overall styling of the application. Also added more tests

---

### Dario
https://github.com/sopra-fs23-group-20/client/issues/107 <br />
https://github.com/sopra-fs23-group-20/server/issues/4 <br />
https://github.com/sopra-fs23-group-20/client/issues/20 <br />

#### Description:
I added the feature that a game cannot be joined when it has already started, completing user story 4. Then I also added that the number of players can be seen when joining a lobby.

---

### Jonas
https://github.com/sopra-fs23-group-20/client/issues/96 <br />
https://github.com/sopra-fs23-group-20/client/issues/85 <br />
https://github.com/sopra-fs23-group-20/client/issues/113 <br />

#### Description:
I implemented the option to choose the duration of how long the scoreboard should be shown (by default 7 seconds). <br />
I implemented the time Left in a round as a horizontal bar which decreases every second. <br />
I implemented the round counter as a circle progress bar so the user knows what round he/she is currently at. <br />


#### Tasks for next week:
https://github.com/sopra-fs23-group-20/server/issues/127 Add possibility to restart game or leave it after it is ended (already started this week) <br />
https://github.com/sopra-fs23-group-20/server/issues/128 US17: As a user I want to be able to play a second game mode called "Blitz" <br />
https://github.com/sopra-fs23-group-20/client/issues/112 Show 6 buttons with the 5 other selected countries as "guessing options" -> instead of the Input Field <br />
https://github.com/sopra-fs23-group-20/server/issues/131 Select the 5 closest (distance wise) countries to the country to guess as guessing options <br />

---

## Week 8.5 - 14.5

---

### Louis
https://github.com/sopra-fs23-group-20/client/issues/105

#### Description:
implemented quickjoin, might have to work on the alert (already started at the 6 May)

### Jamo
https://github.com/sopra-fs23-group-20/server/issues/125
https://github.com/sopra-fs23-group-20/server/issues/126
https://github.com/sopra-fs23-group-20/client/issues/109

#### Description:


### Dario
https://github.com/sopra-fs23-group-20/client/issues/111 <br />
https://github.com/sopra-fs23-group-20/server/issues/137 <br />
https://github.com/sopra-fs23-group-20/client/issues/117 <br />
https://github.com/sopra-fs23-group-20/client/issues/120 <br />
https://github.com/sopra-fs23-group-20/client/issues/122 <br />

#### Description:
I added a first draft of the new game mode "Blitz", I fixed a bug where users could submit guess without entering a country, I fixed a visual bug that occured when only selecting antarctica in the regions section, and I added flashcards with animations to learn countries.

---

## Week 15.5 - 21.5

