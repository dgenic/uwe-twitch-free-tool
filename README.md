

java 17 
https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.0.0.2
JAVA_HOME=/home/uwe/graalvm
PATH=PATH;JAVA_HOME/bin


https://dev.twitch.tv/console

Deine Anwendung registrieren

Name
uwes free tool

OAuth Redirect URLs
https://twitchapps.com/tokengen/

Kategorie
Application Integration


https://twitchapps.com/tokengen/

clientId
moderator:manage:banned_users user:read:blocked_users user:manage:blocked_users bits:read

--> token


src/main/java/resources/twitch.properties

O_AUTH=oauth:
CLIENT_ID=

in start.sh

PROJECT_DIR=/home/uwe/java/twitch/uwe-twitch-free-tool
anpassen


./start.sh
 oder

gradlew quarkusBuild
java -jar $PROJECT_DIR/build/quarkus-app/quarkus-run.jar


moderator:manage:banned_users user:read:blocked_users user:manage:blocked_users bits:read
channel:read:subscriptions user:edit:follows user:edit:broadcast chat:edit chat:read whispers:read whispers:edit
channel:moderate moderation:read channel:manage:broadcast
