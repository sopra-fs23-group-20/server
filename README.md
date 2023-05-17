<p align="center">
  <img src="https://readme-typing-svg.demolab.com/?lines=Sopra+Group+20!;Guess+The+Country!&font=Fira%20Code&center=true&width=600&height=80&duration=4000&pause=500" alt="Example Usage - README Typing SVG">
</p>

<div align="center">
  
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs23-group-20_server&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=sopra-fs23-group-20_server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs23-group-20_server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sopra-fs23-group-20_server)
  
</div>

## Introduction

Our project aims to create an interactive platform that makes learning geography fun, engaging, and accessible. By challenging users to identify countries based on various clues, we hope to enhance their geographic knowledge across multiple domains. With this goal in mind, we designed the project to be user-friendly, visually appealing, and easy to navigate. Hope you enjoy...

## Technologies

-   [Springboot](https://spring.io/) - Java framework to create a micro service
-   [Gradle](https://gradle.org/) - Automated building and management tool
-   [H2](https://www.h2database.com/html/main.html) - Database
-   [React](https://reactjs.org/docs/getting-started.html) - Javascript library for the whole frontend
-   [MUI](https://mui.com/) - CSS Component library
-   [Github Projects](https://github.com/explore) - Project Management
-   [Figma](https://figma.com/) - Mockups
-   [Google Cloud](https://cloud.google.com/) - Deployment
-   [SonarCloud](https://sonarcloud.io/) - Testing & Feedback of code quality

## High-level components

### User

The [User](https://github.com/sopra-fs23-group-20/server/blob/827958492eda6547c71077f69acf2fd5a0bd2537/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/User.java) entity is essential for the geography game application, as it stores important user information, such as username, creation date, and games won, and enables a personalized experience. Additionally, it ensures the security of the application by storing sensitive data, such as passwords and tokens, which are used for authentication and authorization purposes.

### Game

The [Game](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/Game.java) as it represents the structure and state of the game being played. It stores key information such as the game's current state, duration, and number of rounds. Additionally, the entity stores data related to the game's participants, including the game creator, lobby status, and current participants. It also stores information about the game's difficulty, selected categories, and available hints, and enables the selection of countries and regions to be played.

### GameUser

The [GameUser](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/GameUser.java) has dependencies with the User and Game entities. It contains a reference to the User entity via the userId attribute and a reference to the Game entity via the game attribute, which stores information about the specific game instance in which the user is participating. The Game entity also has a reference to GameUser entity via the participants attribute, which stores a collection of GameUser objects representing the users participating in the game.

### Country

[Country](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/Country.java) represents the various countries available for users to learn and guess throughout the game. It stores important data, including the country's unique identifier, name, population, flag, location, capital, and region. Additionally, it relates to the Outline entity, which provides the game map's outline for each country.

## Launch & Deployment

### Setup this Template with your IDE of choice

Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

#### IntelliJ

1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

#### VS Code

The following extensions can help you get started more easily:

-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs23` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.

-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Roadmap

### Mobile Compatibility

Our web application is mostly mobile-friendly, but some pages may still require improvements in scaling for optimal viewing on different devices. Another idea in this area would be to implement the project as a dedicated mobile app.

### Add additional Hints

So far we have the following 5 hints based on which the users can try to guess a country: Population, Outline, Location, Flag and Capital. Ideas for additional hints: Most famous landmark, Currency, GDP, National Anthem, Neighboring Countries, ...

### Friends System

The option to add friends, compare yourself with them and invite them to a lobby

### Achievements

To encourage user engagement and acknowledge accomplishments, it would make sense to implement a series of achievements, including: first correct country guess, first game played, first game won, first friend added, 10 correct country guesses, 10 games played, 10 games won, 10 friends added, and more.

## Authors and acknowledgment

### Contributors

-   **Jonas Blum** - [Github](https://github.com/robonder)
-   **Jonathan Contreras Urzua** - [Github](https://github.com/JonathanContrerasM)
-   **Louis Huber** - [Github](https://github.com/L-Huber)
-   **Dario Monopoli** - [Github](https://github.com/dariomonopoli-dev)
-   **Jamo Sharif** - [Github](https://github.com/JSha91)

### Supervision

-   **Mete Polat** - [Github](https://github.com/polatmete)

## License

[Apache license 2.0](https://github.com/sopra-fs23-group-20/server/blob/6dc8281b0a876fa1d310626a704e0e4bfa08b86d/LICENSE)
