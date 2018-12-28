## Scala Set Game

![](docs/app.png)

## Setup

Application built with [Scala 2.12.4](https://www.scala-lang.org/download/2.12.4.html) and

sbt required (Install globally and the project will setup the expected version locally):
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

Requires JavaFX which is missing from OpenJDK
```
sudo apt install openjfx
```

## Testing

```
sbt test
```

## Style

```
sbt scalastyle
sbt test:scalastyle
```

## Packaging

### Debian

JavaFX installation required.

```
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 sbt debian:packageBin
```

### Windows

.NET and Wix installation required.

```
sbt windows:binPackage
```

### CircleCI

To test locally:
```
curl -fLSs https://circle.ci/cli | bash
```

Setup key and then run build:
```
circleci local execute --job build
```
