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

* [Springboot](https://spring.io/) - Java framework to create a micro service
* [Gradle](https://gradle.org/) - Automated building and management tool
* [H2](https://www.h2database.com/html/main.html) - Database
* [React](https://reactjs.org/docs/getting-started.html) - Javascript library for the whole frontend
* [MUI](https://mui.com/) - CSS Component library
* [Github Projects](https://github.com/explore) - Project Management
* [Figma](https://figma.com/) - Mockups
* [Google Cloud](https://cloud.google.com/) - Deployment
* [SonarCloud](https://sonarcloud.io/ ) - Testing & Feedback of code quality

## High-level components

### User
The [User](https://github.com/sopra-fs23-group-20/server/blob/827958492eda6547c71077f69acf2fd5a0bd2537/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/User.java) entity is a important...

### Game
The [Game](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/Game.java) entity...

### GameUser
[GameUser](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/GameUser.java) is...

### Country
[Country](https://github.com/sopra-fs23-group-20/server/blob/628d671fff0967c6fd185b5291b4fb43f3db6068/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/Country.java) entity contains...

### Category
The [Category](https://github.com/sopra-fs23-group-20/server/blob/718d5acd1c51855319aa0172937644438750d051/src/main/java/ch/uzh/ifi/hase/soprafs23/entityDB/Category.java) instance...





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
* Our web application is mostly mobile-friendly, but some pages may still require improvements in scaling for optimal viewing on different devices.
* An extension of the amount and type of hints available to guess the country. 

## Authors and acknowledgment

### Contributors

* **Jonas Blum** - [Github](https://github.com/robonder)
* **Jonathan Contreras Urzua** - [Github](https://github.com/JonathanContrerasM)
* **Louis Huber** - [Github](https://github.com/L-Huber)
* **Dario Monopoli** - [Github](https://github.com/dariomonopoli-dev)
* **Jamo Sharif** - [Github](https://github.com/JSha91)


### Supervision

* **Mete Polat** - [Github](https://github.com/polatmete)



## License
[Apache license 2.0](https://github.com/sopra-fs23-group-20/server/blob/6dc8281b0a876fa1d310626a704e0e4bfa08b86d/LICENSE)

