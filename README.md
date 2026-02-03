# quarkus-hornetq-client

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Configuring JBoss EAP 6.4.0

You will need to first configure this shell to run a supported Java version for EAP 6.4.0.

```shell script
sdk install java 8.0.482.fx-librca
sdk use java 8.0.482.fx-librca
java -version
```

This should return Java 1.8

Now install EAP, setup a user for quarkus and start the server

```shell script
cp ~/Downloads/jboss-eap-6.4.0.zip .
unzip jboss-eap-6.4.0.zip
./jboss-eap-6.4/bin/add-user.sh -a -u 'quarkus' -p 'Password123!' -g 'guest'
./jboss-eap-6.4/bin/standalone.sh -c standalone-full.xml
```
Now in yet another shell window, again switch to Java 1.8.x, and setup a jms-queue for us to use

```shell script
sdk use java 8.0.482.fx-librca
java -version
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue add --queue-address=ExampleQueue --entries=java:/jboss/exported/jms/queue/ExampleQueue"
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw  quarkus:dev -Dquarkus.http.port=8180 -Ddebug=5105
```

## Testing Quarkus

Now use curl to test the Quarkus endpoint

```shell script
curl "http://localhost:8180/jms/send?msg=HelloHornetQ"
```

You should see something like this returned `Successfully sent: HelloHornetQ` and `<<< Received: HelloHornetQ` in the Quarkus logs

## Testing HornetQ

```shell script
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue pause --queue-address=ExampleQueue"
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue count-messages --queue-address=ExampleQueue"
```

Send a few messages via the prior `curl` command

```shell script
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue count-messages --queue-address=ExampleQueue"
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue resume --queue-address=ExampleQueue"
./jboss-eap-6.4/bin/jboss-cli.sh --connect --command="jms-queue count-messages --queue-address=ExampleQueue"
```