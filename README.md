
<h1>Koel Test Automation Framework</h1><br>
<img  src="assets/logo.f9bf992.svg" alt="extent1" width="100">

<h3>Test Subject</h3>
koel: a personal music streaming server that works<br>

https://qa.koel.app<br>

<h3>Programming Language</h3>
Java <br>

<h3>Libraries/Tools Used</h3> 
* Selenium - Web automation<br>
* TestNG - Test execution<br>
* Cucumber - BDD testing <br>
* REST Assured - Validation of REST web services<br>
* MariaDB Connector/J - Database connector for Koel database<br>
* Apache POI - Reading and writing data in .xlsx, .xls format<br>
* LambdaTest - Cross-browser testing<br>
* log4j - Capturing logs<br>
* Extent Reports - reporting<br>
* Gradle - Build and package management<br>
* Jenkins - CI/CD<br>

<h3>Overview</h3>
This project provides a testing framework based on page object model to automate tests for the Koel web application. There are multiple branches for different types of testing:<br>
* db - Database testing
* api - REST api testing
* TestNG - TDD testing
* Cucumber - BDD testing

<h3>Getting Started</h3>
Follow these steps to get started with the Koel Test Automation Framework:<br><br>
1.) Clone this repository to your local machine. <br>
2.) cd Koel-Test-Automation <br>
3.) Rename "sample.env" to ".env" in src/test/resources <br>
4.) Fill in all the appropriate property values in the .env file<br>
5.) Choose a branch<br>
6.) gradle clean test<br>
NOTE: If using Jenkins be sure to add a variable in the build.gradle file<br>
<img  src="assets/jenkins.png" alt="jenkins" width="500"><br>

<h3>Extent Reports</h3>
Extent reports will be generated in the reports/extent-reports folder.<br>
<img src="./assets/extent1.png" alt="extent1" width="550" height="530"> 
<img src="./assets/extent2.png" alt="extent2" width="550" height="530">

<h3>Store Results in Excel Files, or Use as a Dataprovider<h3>
<img src="./assets/ExcelFile.png" alt="extent1" width="550" height="530"> 
