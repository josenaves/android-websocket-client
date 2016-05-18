# android-websocket-client

This is PoC (Proof Of Concept) that uses Google's Protocol Buffer payloads
encoded in binary websocket messages to exchange data with a backend written in Node.

## Requirements:
- Java 7
- Android SDK API 23
- Gradle
- Square's Wire
- AndroidAsync

## How to generate Java model

You should download the latest version of [Wire Compiler](http://search.maven.org/remotecontent?filepath=com/squareup/wire/wire-compiler/2.1.2/wire-compiler-2.1.2-jar-with-dependencies.jar)
and run the following command:

```
$ java -jar wire-compiler-2.1.2-jar-with-dependencies.jar \
    --proto_path=app/src/main/proto \
    --java_out=app/src/main/java \
    image.proto

Writing com.josenaves.android.websocket.client.Image to app/src/main/java

```



