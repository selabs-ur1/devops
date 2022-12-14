# Canary Testing

## Introduction

Canary testing is a method of software verification whose principle is to deploy a new version of the application to a subset of users. It allows to test the application in a real situation while not affecting all the population using it, and in case of failure, affects less users.

For example, Meta uses the Canary Testing method on its employess applications and then can gather information quickly by gathering data and feedback from the employees without affecting the external users.

Canary testing and A/B testing are similar in deployment, but their objective is different. Canary testing is meant to test resilience of new features, while A/B testing is meant to gather feedback on user experience between multiple already working features.

Like A/B testing, the Canary method fits well with continous software deployment.

## Implementation

Canary and A/B testing being similar, we can then use the same tutorial than for [A/B testing](./ABTesting.md).

However, due to the difference in the objectives of both methods, we have to modify certain parameters. The first parameter is the population. We do not want to have a large part of the user base (for example 50% like in the A/B tutorial), so we can have different methods of user separation : 
  - Employees separation
  - 90/10 ratio (or less depending on the magnitude of the changes or risks, going as low as 99/1), 10 being the percentage of the population getting the new features to test.

### Employees separation

After the feature creation in your Growthbook, instead of selecting A/B Experiment, we will have to go to "Features>Attributes" and then click on "Edit Attributes" on the top right corner.

![image](https://user-images.githubusercontent.com/101655310/204324115-24561660-38ec-40d7-9d89-27a4a8526173.png)

You then need to tick the "Identifier" column corresponding to the "employee" attribute, like shown in the image above. This will allow you to select "employee" as a separation factor later.

To do that, go to "Features>All Features" and select the feature created before. Like in the A/B testing tutorial, select **A/B Experiment** in the "Override Rules" section.

Instead of keeping the id in "Assign value based on attribute", you are now able to select "employee". Tick "Customize Split" in the "Exposure, Variations and Weight" section and bring the slider bar to 100% for the "On" option. This activates the option "On" for every employee, but no other user.

![image](https://user-images.githubusercontent.com/101655310/204326712-d9952354-b4dd-4a55-aea0-d648ed98629c.png)


### 99/1

If you want to test your application with a certain percentage of the user population, you then need to use "id" instead of "employee" in your override rule, and then bring the slider to 1% for the "On" option instead of 100%. Only 1% of users will then have the new features.

![image](https://user-images.githubusercontent.com/101655310/204328006-da186631-397a-4324-9320-121c191f15c5.png)

You are now able to change different separation rules based on your testing strategy !

### Logging

For the Canary testing method to work fully, you have to be able to detect errors within the population using the new features. For that, you need to be sure that your logging process is functionning well and easy for the users to use.
If the test users are the employees, error reporting can even be easier because they will be able to quickly report and describe the errors.
