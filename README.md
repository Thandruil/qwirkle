# qwirkle

[![Build Status](https://travis-ci.org/Thandruil/qwirkle.svg?branch=master)](https://travis-ci.org/Thandruil/qwirkle)

## Building

First, acquire the source code by cloning the git repository:

`git clone https://github.com/Thandruil/qwirkle.git`

Now run gradle to collect the dependencies and build the artifacts.

`./gradlew build` (Unix) or `gradle.bat build` (Windows)

Gradle will place a compiled jar archive in the `build/libs` folder.
Now you should be able to run Qwirkle like this:

`java -jar qwrikle.jar --client` or `java -jar qwirkle.jar --server`