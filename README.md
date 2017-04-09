# Multi-platform PAC-MAN developed with Java 

[![Build Status](https://travis-ci.org/alejandrocq/pacman-JAVA.svg)](https://travis-ci.org/alejandrocq/pacman-JAVA)
[![Join the chat at https://gitter.im/alejandrocq/pacman-JAVA](https://badges.gitter.im/alejandrocq/pacman-JAVA.svg)](https://gitter.im/alejandrocq/pacman-JAVA?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Native using JavaFX and web using GWT.

* ``mvn -am -pl :pacman-jre install && mvn -pl :pacman-jre exec:java`` to run the native app
* ``mvn -am -pl :pacman-gwt gwt:devmode`` to run the web app (open http://localhost:8888/pacman/)
* ``mvn package`` to generate 
  * ``jre/target/pacman-java-{version}-jar-with-dependencies.jar`` executable jar
  * ``gwt/target/pacman-gwt-{version}.war`` webapp

[![PacMan](https://github.com/alejandrocq/pacman-JAVA/raw/master/screenshot.png)](https://ibaca.github.io/pacman)
