# Tutorial : A Documented Pull Request
---
# Make the Doodlestudent project works!

## Running docker-compose

The very first thing I have to do is to launch services that the application depends on.
To do so, I have to run the `docker-compose.yaml` 

However, a first error comes when running command `sudo docker-compose up`, the docker-compose version does not accept the `3.8`
```sh
ERROR: Version "3.8" in "./docker-compose.yaml" is invalid.
```

After searching in the docs, the `3.8` version need the docker-compose at version <=1.25.5 which is available for the versions of docker <= 19.03.0 

From this point, the dependencies start with no problem.

## Starting the application

Because I experimented with multiple application startups, I found out that the application starts by creating the database without checking the existence of a previous database. The fact is that it creates errors when starting the application a second time.

![second-startup](https://user-images.githubusercontent.com/68585341/100284384-65291a80-2f6f-11eb-99f7-7437798c5a58.png)

The fix is quite simple: stop the containers after the application close and restart them before the launch.
In other terms : 
```sh
# Start the app
sudo docker-compose up --detach & ./mvnw compile quarkus:dev

# Ctrl+C to stop and then :
sudo docker-compose down
```

## Play with the application

After trying to use the app, I was blocked in the creation of a poll.
The scenario is the following : 

- I start the dependencies and the application
- I fill the title, place and description of the event
- I select some dates
- And ... it's blocked

The error was an authentication error saying that the wrong API key was used.
I started the docker container of etherpad alone and looked at the logs.
The logs indicated that the `APIKEY.txt` file which contains the API key was not found, hence generated randomly.

![random-key](https://user-images.githubusercontent.com/68585341/100284383-64908400-2f6f-11eb-84cc-b9295fd7f3b1.png)
 
To patch the problem, I asked the generated file, set it in the project and updated the `docker-compose.yaml`

## **Review** by gwendal-jouneaux
The following changes were made to fix the APIKEY bug
<p>&nbsp;</p>

---

**`code/docker-compose.yaml`**
```diff
@@ -7,6 +7,8 @@ services:
     environment:
       - MYSQL_ALLOW_EMPTY_PASSWORD=true
   etherpad:
-    image: etherpad/etherpad
+    image: etherpad/etherpad:stable
```
**gwendal-jouneaux** commented :

I put the stable version of the docker image

---
**`code/docker-compose.yaml`**
```diff
@@ -7,6 +7,8 @@ services:
     environment:
       - MYSQL_ALLOW_EMPTY_PASSWORD=true
   etherpad:
-    image: etherpad/etherpad
+    image: etherpad/etherpad:stable
     ports:
       - "9001:9001"
+    volumes:
```
**gwendal-jouneaux** commented :

Create a volume section to create a mapping between local files on my computer and container files

---
**`code/docker-compose.yaml`**
```diff
@@ -7,6 +7,8 @@ services:
     environment:
       - MYSQL_ALLOW_EMPTY_PASSWORD=true
   etherpad:
-    image: etherpad/etherpad
+    image: etherpad/etherpad:stable
     ports:
       - "9001:9001"
+    volumes:
+      - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
```
**gwendal-jouneaux** commented :

Finally, I ask docker to put the APIKEY.txt file at the right place

---
<p>&nbsp;</p>
<p>&nbsp;</p>

After that change, the problem was here again because my change impacted another file: application.yml 

This file contains the API key used to connect to etherpad from the application. We copied the key of the `APIKEY.txt` file in the `application.yml`
<p>&nbsp;</p>
<p>&nbsp;</p>

## **Review** by gwendal-jouneaux

<p>&nbsp;</p>

---

**`code/application.yml`**
```diff
@@ -0,0 +1,28 @@
+doodle:
+  usepad: true
+  internalPadUrl: "http://localhost:9001/"
+  externalPadUrl: "http://localhost:9001/"
+  padApiKey: "19d89ca52bc0fa4f19d6325464d9d7a032649b9fa68c111514627081e2784b4a"
```
**gwendal-jouneaux** commented :

The API key is now correct

---
<p>&nbsp;</p>
<p>&nbsp;</p>

## In Front of a front-end bug



After the last bug fix, the problem was still there. When finalizing the poll creation it stopped working.

This time there was no error on the API logs, I deduced it was a front error.



When looking in the devtools, I found this error



![front-error](https://user-images.githubusercontent.com/68585341/100287032-32cdec00-2f74-11eb-9726-19d473559d76.png)



The error is that the API is requested on the front-end port. After a little investigation, I found that the front should use a proxy to redirect API calls to the good port. After some research, I found out how to use it and also found that the very command that I found was in fact what `npm start` was doing



In other words, use `npm start` in place of `ng serve`
<p>&nbsp;</p>
<p>&nbsp;</p>

## Close the poll



When closing the newly created poll, I found that the back-end is not resilient to the presence (or not) of a local SMTP server.

To avoid such problems, I could install an SMTP Server on the computer. However, this is not the solution I choose.



To create a platform-independent solution for this problem, we updated the `docker-compose.yaml` to add a new container that is an SMTP server. This way, everyone with docker can avoid this error.
<p>&nbsp;</p>
<p>&nbsp;</p>

## **Review** by gwendal-jouneaux

<p>&nbsp;</p>

---

**`code/docker-compose.yaml`**
```diff
@@ -12,3 +12,8 @@ services:
       - "9001:9001"
     volumes:
       - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
+  mail:
```
**gwendal-jouneaux** commented :

Create a new section for the mail server container

---
**`code/docker-compose.yaml`**
```diff
@@ -12,3 +12,8 @@ services:
       - "9001:9001"
     volumes:
       - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
+  mail:
+    image: bytemark/smtp
```
**gwendal-jouneaux** commented :

add the docker image (found on docker hub)

---
**`code/docker-compose.yaml`**
```diff
@@ -12,3 +12,8 @@ services:
       - "9001:9001"
     volumes:
       - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
+  mail:
+    image: bytemark/smtp
+    restart: always
```
**gwendal-jouneaux** commented :

The documentation asked for this parameter

---
**`code/docker-compose.yaml`**
```diff
@@ -12,3 +12,8 @@ services:
       - "9001:9001"
     volumes:
       - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
+  mail:
+    image: bytemark/smtp
+    restart: always
+    ports:
```
**gwendal-jouneaux** commented :

Map the container port 25 (the classic port for SMTP) to 127.0.0.1:2525 used in our `application.yml`

---
**`code/docker-compose.yaml`**
```diff
@@ -12,3 +12,8 @@ services:
       - "9001:9001"
     volumes:
       - ./APIKEY.txt:/opt/etherpad-lite/APIKEY.txt
+  mail:
+    image: bytemark/smtp
+    restart: always
+    ports:
+      - "2525:25"
```
**gwendal-jouneaux** commented :

**HELP** : the syntax is the following -> "LocalhostPort:ContainerPort"

---
<p>&nbsp;</p>
<p>&nbsp;</p>

## Ctrl+C, Ctrl+....



After further testing, everything goes well... And I tried to update a poll.

Of course, nothing happened and the poll was not updated.



As usual, I looked at the log of the API ... OK



And then the log of the front ... not OK



The request returned a 404 error (Not found but you probably know that). 

Then I checked other URLs of the API, they were all OK.

So I looked at the code, and I found it strange that the URL for updates is `/api/poll/update1`.

I found it even more strange when the front requested `/api/poll/update`.


<p>&nbsp;</p>
<p>&nbsp;</p>

## **Review** by gwendal-jouneaux

<p>&nbsp;</p>

---

**`code/poll-service.service.ts`**
```diff
@@ -17,7 +17,7 @@ export class PollService {
 
 
   public updtatePoll(p: Poll): Observable<Poll> {
-    return this.http.put<Poll>('/api/poll/update', p);
+    return this.http.put<Poll>('/api/poll/update1', p);
```
**gwendal-jouneaux** commented :

Probably a bad copy-paste

---
<p>&nbsp;</p>
<p>&nbsp;</p>

## Warning: One bug can hide another



Now the update should work, but in fact, it doesn't.



When updating the poll, the back-end tries to update the info in the pad (title, place and description).

However, the API used in Java needs the pad ID while we saved the pad URL.

In order to fix the problem a `getPadID()` function was created.



The problem in this situation is the fact that since the pad version changed, other things like pad ID length also changed.

The old function that returns the 6 last characters does not work anymore.
<p>&nbsp;</p>
<p>&nbsp;</p>

## **Review** by gwendal-jouneaux
For the new version, I choose to consider the part after the last slash as the ID.

This assumption as the advantage to be independent of ID length
<p>&nbsp;</p>

---

**`code/PollResourceEx.java`**
```diff
@@ -175,6 +175,6 @@ private static void initPad(String pollTitle, String pollLocation, String pollDe
 	}
 
 	private static String getPadId(Poll poll) {
-		return poll.getPadURL().substring(poll.getPadURL().length() - 6);
+		return poll.getPadURL().substring(poll.getPadURL().lastIndexOf('/') + 1);
```
**gwendal-jouneaux** commented :

Using `lastIndexOf('/')` we capture as pad ID the portion of the URL after the last `/` which should be more resilient to ID length changes

---
<p>&nbsp;</p>
<p>&nbsp;</p>

# Conclusion



Now, you should be able to run and use the doodlestudent application with much less trouble.



Of course, some OS-dependent problem can remain, but at least the major issues are mitigated here.





## Authors



Gwendal JOUNEAUX
<p>&nbsp;</p>
<p>&nbsp;</p>


