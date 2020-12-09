# PROBOSCIS :  Continuous integration, static analysis and test automation

*Authors* : **Leslie AMANI - Dackory DAYORO - Konan TOURE**

Today, companies are moving towards a dynamic, customer-centric approach to the development and delivery of their applications. In an environment where customers are turning to digital transactions in the mobile age, the role of application developers is becoming essential in the customer experience. At the same time, the trend towards agility has been a source of inspiration for DevOps, one of the agile key points of which favors professionals and their interactions rather than processes and tools.
Over the past few years, the development and operations teams have significantly improved the way they work. But today, the need to realign these two teams is growing. The DevOps movement arises from this need for realignment. DevOps is behind a philosophy that completely transforms the way IT professionals view the stability and functioning of the system they manage, as well as their own role in the flow of value from start to finish. end. Cloud Computing and Software-Defined Network (SDN) are two elements that have accelerated the destruction of the silos that separated development and operations teams.

## Installation of Doodlestudent project
As part of our work, we will be using the Doodlestudent app. Configuration required for Windows :
-   Docker ;
-   Git ;
-   Java ;
-   Maven ;

The installation steps are shown below :
**Step 1 : clone the projet**
> git clone https://github.com/barais/doodlestudent.git

**Step 2 : Run the back**
> docker-compose up --detach & ./mvnw compile quarkus:dev

**Step 2 : Run the front**
> npm start

Navigate to `http://localhost:4200/`.

There may be some problems when starting docker-compose. We must run the following commands:
> docker-compose down 
> docker-compose up --detach & ./mvnw compile quarkus:dev


## **Presentation of some tools Continuous integration, static analysis and test automation**
In this section, we will see the essential tools for a DevOps in its tasks, namely: test automation and continuous integration. Indeed, before deploying an application, it is essential to perform unit and functional tests. However, doing it by hand quickly becomes tedious. This is why, through the Doodlestudent project, we will use some tools to organize tests and continuous integration.

****
**Continuous integration** 

Continuous Integration is the process of integrating changes to the computer code of a software project on an ongoing basis, in order to immediately detect and correct any errors. Among the most used tools, we can mention :
 - **Jenkins :**
Jenkins is a continuous integration tool that allows testing an application with all of its modifications and continuous deployment. It allows you to test and report changes made on a large code base in real time.
**Advantages** : open-sources ; Continuous integration is ensured through plugins ; easy to install ;  convenient for all platforms ;

 - **GitLab CI**
GitLab is an open source collaborative development platform published by the American company of the same name. It covers all the stages of DevOps. Based on the features of Git software, it allows you to control source code repositories and manage their different versions.
GitLab CI/CD is a GitLab tool designed to manage continuous application integration, delivery and deployment. GitLab CI/CD involves the configuration of a file called .gitlab-ci.yml placed in the root directory. A file that generates a pipeline running code changes within the code repository.
***
**Test automatisation** 
Test automation makes it possible to perform tests at will following the delivery of a new version of an application. There are many tools that allow you to automate your tests. Among the most famous, we can mention :
 - **Selenium** 
Selenium is one such automation tool, concerning the interface testing of web applications. It consists of two parts: 
**Selenium IDE:** it is an extension of Firefox, which allows you to record a series of actions, which you can replay at will;
**Selenium WebDriver :** this time it is an API, available for several languages, allowing to program actions on the interface, and to check the responses. The actions to be performed can be exported from Selenium IDE.
**Advantages :** Ability to test all components of the application (backend, front) from the user's point of view ; less expensive ; can be implemented with several browsers (chrome, firefox , safari, opera …) ; uses several programming languages ​​(python, java, scala, PHP, C # ...)
**Disadvantages :** Selenium only allows you to test applications accessible from a browser, without taking into account rich clients (Ajax, ExtJS , GWT, etc.) .

 - **Testcontainers**
Testcontainers is a Java library that allows you to write and especially manage docker containers easily from Junit tests . In fact, traditionally, to correctly run our tests, we need a local environment. And it becomes even more difficult if we use docker containers because our tests must be able to launch the containers. Thus, with this tool, container management becomes, through our tests, simplified.
**Advantages :** Ease of integration for tests requiring docker containers.

 - **Macaca test** 
Macaca is a solution for automation of open-source test for native web, mobile, hybrid, and mobile web. Macaca provides automation drivers, environmental support, peripheral tools, and integration solutions designed to address issues such as test automation and customer end performance.
**Advantages :** Can be used with java, nodeJs , python ;

 - **Online tools**
There are many online tools that allow you to test web applications from the url. It is about : **Monkey test it**, **assertible**, etc.
**Advantages** : Easy to use ; does not require technical skills ; use of plugins ;
**Disadvantages** : Insufficient testing ; applications must deploy

## Use of this tools in doodlestudent

As part of our project, we will use the tools presented upstream. It will be about using selenium and gitlab-ci in order to face the daily difficulties of a DevOps.

###  Selenium 

- **Install java JDK and add it in the environment variables**
- **Install Eclipse** 

- **Download Selenium var Jar for chrome according to your version** 
> https://chromedriver.storage.googleapis.com/index.html
- **Download Selenium server**  
> https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar
- **Download Selenium librairies for Java** 
> https://selenium-release.storage.googleapis.com/3.141/selenium-java-3.141.59.zip
- **Launch the Doodle project with eclipse ;**

- **Add the Selenium libraries in the build path ;**

- **Create a test class ;**
Example of test : 
```html
public  class  TestSelenium {
	public  static  void  main (String[] args) throws  InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver.get("http://localhost:4200/");
		driver.manage().window().maximize();
		driver.findElement(By.className("Home_CreateLink")).click();
		Thread.sleep(2000);
		String  url = driver.getCurrentUrl() ;
		String  resultat = "http://localhost:4200/create";
		if (url.equalsIgnoreCase(resultat)) {System.out.println("Test reussi, page suivante");}
		else {System.out.println("Pas de chance");}
		driver.findElement(By.id("titre")).sendKeys("TP-DLC");
		driver.findElement(By.id("lieu")).sendKeys("Teams");
		driver.findElement(By.id("desc")).sendKeys("Date pour la prochaine reunion");
		Thread.sleep(2000);
		driver.findElement(By.className("p-button-label")).click();
		String  url2 = driver.getCurrentUrl() ;
		String  resultat2 = "http://localhost:4200/create";
		driver.close();
		if (url2.equalsIgnoreCase(resultat2)) {System.out.println("Test reussi avec succès");}
		else {System.out.println("Pas de chance");}
	}
}
```

### Gitlab-CI 

 - Fork the DOODLE project on Gitlab
 - Add a simple Selenium test to your GitLab project, modify the **.gitlab-ci.yml** file in your repository to indicate you want to run a Selenium test.
 - Example : 
```html
build-job:
stage: build
script:
	- echo "Hello, $GITLAB_USER_LOGIN!"
test-job1:
	stage: test
	script:
		- echo "This job tests something"
test-job2:
	stage: test
	script:
		- echo "This job tests something, but takes more time than test-job1."
		- echo "After the echo commands complete, it runs the sleep command for 20 seconds"
		- echo "which simulates a test that runs 20 seconds longer than test-job1"
		- sleep 20

deploy-prod:
	stage: deploy
	script:
		- echo "This job deploys something from the $CI_COMMIT_BRANCH branch."
```


## **Problems encountered**
We are unable to run our selenium tests on GitLab-ci. Maybe this is due to a bad configuration of our .gitlab-ci.yml file. 

# Conclusion

In short, we note that there are many tools to facilitate the daily life of a DevOps. These tools are easy to integrate and are simple to use. However, we were faced with some difficulties in installing the application. This therefore had an impact on the use of the tools.
