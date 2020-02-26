# Read Me First
To use application use one of a following options:

### Run locally

Building:
```
mvm clean install
```
Running:
```
mvn spring-boot:run
```

### Build local container and run it
Building:
```
docker build -t remote-xml-analyser .
```
Running:
```
docker run -p 8080:8080 -t remote-xml-analyser .
```
### Pull remote container and run it
Pulling:
```
docker pull michalpacholczykit/remote-xml-analyser:analyser
```
Running:
```
docker run -p 8080:8080 -t michalpacholczykit/remote-xml-analyser .
```