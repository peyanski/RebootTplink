## Synopsis

This program is using Selenium and Chrome Driver to restart a TP-Link router (from the latest GUI) if there is no Internet connection

## Build & Run

1. Clone the project from the repository
2. rename config.properties.example to config.properties and add your router credentials
3. Use maven to build it:
    mvn clean && mvn package
4. run it with:
    java -cp target\rebootTplink-1.3-SNAPSHOT-jar-with-dependencies.jar org.a3.RebootTplink

## Tests

// TODO

## License