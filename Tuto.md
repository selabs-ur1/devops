# Installing the DoodleStudent Project

You should use windows to set up the installation. You can use Mac or Linux as well but you'll probably need to get trough minor troubles. 
The whole project run in some container and we'll need to install few things before building them all.

Because it's still in progress, they might be some steps that you can struggle with. We are doing our best to make this tutorial as clear as possible and make it as far as possible.

You can see the video tutorial here :
https://youtu.be/h4Eh9l0kAzY

## Prepare your environment

### Installing docker

To install docker, you should download the desktop version here : 
https://www.docker.com/get-started

If you encounter any problem to install docker, you can follow this tutorial :
https://docs.docker.com/docker-for-windows/install/
(use the left panel to choose a tutorial that correspond to your environment)

### That's it

Because the whole project run on container, the required softwares are supposed to be included and automatically installed in the containers. 

###  (Temporary) While the back isn't well dockerise

We had some troubles with the back part of the project (see below), so you need to install mvn :
https://www.codeflow.site/fr/article/maven__how-to-install-maven-in-windows
(In case of other environment you can easily find a tutorial on maven website https://maven.apache.org/install.html)

## Build the project

### Docker

In order to launch the project, you just should use docker-compose up and it should set up, build, and install the entire project by it's own.

Unfortunately, we had some troubles here :
1. The front container is running but seems to be not reachable from localhost:4200 see details on the following issue : https://github.com/barais/doodlestudent/issues/2/. To Temporary avoid this trouble, you can manually launch the server using npm install and npm start from the front directory (you might need to disable the front container to free the port 4200)
2. The back container is not implemented yet and don't work for now. To temporary fix the problem a run.bat can be used instead of the compose-up command to build the project (to automatise the work as a container).

## Open Tracing

To be able to monitor and trace the project we chose here to talk about a reknown tool : open tracing.

### What is open-tracing ?

Open tracing is that simple way to trace all the requests that your programs does, draw chart about it, analyse and much more... 

It can run in many different systems and is really easy to install !

### Installation 

The way to install open-tracing is different depending on what is running on :

#### Back : 

We had to add the corresponding dependency on the pom.xml file :
```
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-opentracing</artifactId>
</dependency>
```
And finaly, we had to add the following configuration on \src\main\resources\ical4j.properties (you can custom parameters)
``` 
quarkus.jaeger.service-name=myservice 
quarkus.jaeger.sampler-type=const 
quarkus.jaeger.sampler-param=1 
quarkus.log.console.format=%d{HH:mm:ss} %-5p traceId=%X{traceId}, parentId=%X{parentId}, spanId=%X{spanId}, sampled=%X{sampled} [%c{2.}] (%t) %s%e%n  
```

#### Front : 

We didn't manage to finish that part so to be able to track the front part, you'll need to install it own your own by following this tutorial https://github.com/jaegertracing/jaeger-client-node

#### OpenTracing Interface : 

Once all your app are started, you can launch the open-tracing container using the command :
``` 
docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
``` 

## Usage

Once the installation is done you just have to go to http://localhost:16686 and you can trace as u want your application service by service.

# What's next ?

What can be improved here ? Well, there is few/many things to do to improve this tutorial :

First, we have to fix all the container troubles to make it work (we try hard, but the bug stand strong ! ).

Then it would be really cool to add a trigger on the git commit on main and automatically generate some new images to update the containers that could be deployed EVERYWHERE in the world (using pipelines trough gitlab/atlessian/jenkins...).

There is always a cool feature you could add in devops, so the best we can do is study and find the best way to do what we want to do and stay open-minded !
