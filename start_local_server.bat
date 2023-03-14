cd /d %~dp0
wt -d . cmd /c gradlew.bat bootRun
gradlew.bat build --continuous -xtest