# A/B Testing on doodle quarkus

## Introduction
A/B testing is a process in which we provide different versions of an application to the end user to see how it performs by retrieving different metrics.
For this tutorial, we will show how to use GrowthBook to perform an A/B testing on the front-end of the doodle application.

## Setting up growthbook
The first thing to do is to set up the growthBook application, the easiest way is to self-host it by using a docker-compose file provided by growthbook.
```yaml
# docker-compose.yaml
version: '3'
services:
  mongo:
    image: "mongo:latest"
    environment:
      - MONGO_INITDB_ROOT_USERNAME=root
      - MONGO_INITDB_ROOT_PASSWORD=password
  growthbook:
    image: "growthbook/growthbook:latest"
    ports:
      - "3000:3000"
      - "3100:3100"
    depends_on:
      - mongo
    environment:
      - MONGODB_URI=mongodb://root:password@mongo:27017/
    volumes:
      - uploads:/usr/local/src/app/packages/back-end/uploads
volumes:
  uploads:
```
And then to lauch it with the command 
```bash
docker-compose up
```
This will download the mongodb and growthbook images and launch them.<br>
You can then reach the self-hosted growthbook website by accessing [http://localhost:3000]()<br>

You will first be prompted with the first page in which you will be able to create a account.<br>
<img src='./Images/Account.png' width=650>


## Setting up the doodle front-end
### Lauch the app
To simplify this tutorial we will only be running the front-end of the application, which can be launched by using these commands:<br>
`npm install` and `npm start`<br>
And then accessing the front-end on [http://localhost:4200]()
### Integrate GrowthBook in the angular application
Now that doodle and GrowthBook are set up, we can update our doodle app to use GrowthBook<br>
To do so, first acces "Feature>All Features" on the left side of the GrowthBook website.<br>
<img src='./Images/allFeatures.png' width=180><br>
And then click on `View instructions`<br>
<img src='./Images/installSDK.png' width=500><br>
What you are seeing now is some explanation on how to use growthbook with your application, and it is what I used to implement it in the doodle app<br>
Growthbook then provides starter code for a few different languages, but we are going to use the javascript code by putting it in an angular service.<br>
The service that I have created is available [here](./front/src/app/abtesting.service.ts). Essentially, the service has two methods:<br>
- An init method which initializes the growthbook service by creating a growthbook instance, then reaching the api endpoint and finally setting the attributes of the current user (We will talk more precisely about those later).
- A getState method that we will use to retrieve wether a user has a feature activated.

To use this service, the only thing to change is the line 5:
```javascript
const FEATURES_ENDPOINT = "_YOUR_SDK_ENDPOINT_";
```
And replacing "_YOUR_SDK_ENDPOINT_" by the endpoint in "Features>Environments>SDK endpoint".


## Setting up a feature in growthbook

As mentionned earlier, the purpose of A/B testing is to provide different version of the application to the end user. In order to do so, we use what is called "features". After setting up these features, we will get those check with angular wether those features are up or not.<br>
We will now need to create our first feature by accessing "Features>All Features" and then clicking on "Add first feature"<br>
<img src='./Images/firstFeature.png' width=500><br>
You will then be prompted with a "create feature" tab in which you can define different setting about the feature you want to publish. For our app, we will only provide the users with a centered title. Note that the default value is set to "On", so without changing anything every user will have this feature enabled<br>
<img src='./Images/FFeature.png' width=500><br>

## Using the feature in angular

As said earlier, we will center a title of the website by using the features of growthbook, to do so, we need to modify the code of the create-poll component a little<br>
First we have to inject the abtesting service in the component by adding it in the providers and in the constructor:
```javascript
...
    providers: [MessageService, PollService, FullCalendarComponent, ABTestingService]
...
    centerValue = false
...
    await this.ABTesting.init();

    this.centerValue = this.ABTesting.getState("center-title")
...
```
And then we can modify the component in the html to change the position of the title.
```html
    <img src="assets/flat_logo.png" alt="Logo Simba" height="50px" style="display: block" [ngStyle]="{ 'marginBottom': '1rem','margin-left':(centerValue)? 'auto':'','margin-right':(centerValue)? 'auto':'' }"/>

```
Since the center-title feature is available by default, the title is now centered when we are in the create-poll page.<br>
<img src='./Images/Centered.png' width=700><br>
We have created a feature available by default but It is not A/B testing yet, to archive this we will have to modify the feature.

## Modifying the feature for A/B testing
You can now access the feature you have created in "Features>All Features" and click on your feature to access its settings.<br>
You can now scroll down to the "Override Rules" section, different rules can be added to this feature but we will select **A/B experiment**.
<img src='./Images/Overide.png' width=700>

### Targetting Attributes 
The first setting that matters is the "assign value based on attribute". Every user has a set of attribute that is associated to them, I kept those provided by default but they can be changed in "Features>Attributes".<br>
As you can see below, In the program I also kept those by default but in a real production environnement those attributes could be retrieved after a user identification.
```javascript
  public setattribute() {
    this.growthbook.setAttributes({
      "id": "foo",
      "deviceId": "foo",
      "company": "foo",
      "loggedIn": true,
      "employee": true,
      "country": "foo",
      "browser": "foo",
      "url": "foo"
    });
  }

```
So for our A/B testing experiment, we will keep the id as the targetting attribute.

### Exposure, Variation and Weights
In the section just below the attribute selection, we have the section where we can select the percents of users who will be part of the experiment as well as the different splits and values. we will keep the default values of 50% for each split and then click save. Once its done you can now click "Review and publish" on the top of the page to release the test".

## Testing the experiment
Now, we have a 50% chance of getting either, a value of true or false for the center-title value. For example if we change the value of our attribute "id" from foo to something random like "test", if we get lucky, we get this page with the title on the left:<br>
Note that growthbook uses deterministic hashing, so each id has a static value associated with it.<br>
<img src='./Images/notCenter.png' width=700>

## Analysis
Growthbook also allows for analysis about the different experiments, to do that you can connect a datasource to it and define metrics that helps you compare the versions of your apps. For example for our centered title we could mesure the time on site or the bounce rate which would give us an insight on what the users think about the feature.

