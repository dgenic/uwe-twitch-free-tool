Bester Stream! https://twitch.tv/einfachuwe42

# Uwe Twitch Tool

## Java 17 installieren

### Download
https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.0.0.2

### Umgebung Variabeln und Path
```
JAVA_HOME=/home/uwe/graalvm
PATH=PATH;JAVA_HOME/bin
```
## Anwendung anmelden
Auf der Seite https://dev.twitch.tv/console die Anwendung registrieren.

__OAuth Redirect URLs:__ https://twitchapps.com/tokengen/

__Kategorie:__ Application Integration

## OAuth Token erzeugen

Auf der Seite https://twitchapps.com/tokengen/ den OAuth Token mit der ClientId und folgenden Scopes erzeugen.
```
bits:read user:edit:follows
```

## Git Repo Clonen

`git clone git@github.com:einfachuwe42/uwe-twitch-free-tool.git`

## Konfiguration der Anwendung

In die Datei `src/main/java/resources/twitch.properties` OAuth Token und ClientId setzen.

```
O_AUTH=oauth:xxxxxxxxxxxxxxxxxx
CLIENT_ID=xxxxxxxxxxxxxxxxxxxxx
```

## Anwendung bauen

Im Projektordner `gradlew quarkusBuild` ausführen.

## Anwendung starten

`java -jar $PROJECT_DIR/build/quarkus-app/quarkus-run.jar`

