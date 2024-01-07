# Selenium Java Browser Factory

Don't forget to give this project a ⭐

* [Introduction](#introduction)
* [Technologies and Libraries](#technologies-and-libraries)
* [Technical explanation](#technical-explanation)
    * [DriverFactory](#driverFactory)
    * [Driver Manager classes](#driver-manager-classes)
* [Explaining the usage scenarios](#explaining-the-usage-scenarios)

## Introduction

This project shows how you can use the Factory design pattern to create different browsers using Selenium WebDriver
for the web test automation.

**This branch has the first basic approach.**
The main focus is to give you an example to implement the Factory design pattern to execute web tests in your local machine.
You can see the other examples in the following branches
* [local-remote-example](https://github.com/eliasnogueira/selenium-java-browser-factory/tree/local-remote-example): shows a local and remote implementation of the Factory design pattern
* [master](https://github.com/eliasnogueira/selenium-java-browser-factory): shows a possible optimized way to create a browser factory with less code possible through the use of enums.

## Technologies and Libraries

* [Java 21](https://openjdk.java.net/projects/jdk/21/) as the programming language
* [JUnit 5](https://junit.org/junit5/) to support the test creation
* [Selenium WebDriver](https://www.selenium.dev/) as the web browser automation framework using the Java binding
* [AssertJ](https://joel-costigliola.github.io/assertj/) as the fluent assertion library
* [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) as the Selenium binaries management
* [Owner](http://owner.aeonbits.org/) to minimize the code to handle the properties file

## Technical explanation

The factory implementation is based on:
* factory class to create the browser instance for the local or remote execution
* driver manager that has one class per browser we need to use (instantiate)

### DriverFactory

The [DriverFactory](https://github.com/eliasnogueira/selenium-java-browser-factory/blob/basic-example/src/main/java/com/eliasnogueira/driver/factory/DriverFactory.java)
class is responsible for creating the browser instance.

The browser will be created based on the `browser` property value on `general.properties` file located at `src/test/java`.
The value provided is converted by the enum `BrowserList` and used into a `switch-case` do instantiate the matching 
browser returning the `WebDriver` instance of it.

If the browser is not valid/recognized/know it throws a `BrowserNotSupportedException` exception.

Instead of placing the browser instance class inside the `DriverFactory` a good approach is to create a class that can do it.
For each implemented browser we have a class with `DriverManager` suffix.

### Driver Manager classes
You can see on the `DriverFactory` class that the browser instantiation will be done by a custom class.
You could, for example, directly return the `ChromeDriver()` instead of using a custom class.
It will give you more control during any code change.

We have a Driver Manager class for each browser implemented placed on the `factory.manager` package.
Each one implements the `Factory` interface to have the same method `createDriver()`.

The `createDriver()` method created the browser driver using the WebDriverManager library and then create an instance 
of its browser.

## Explaining the usage scenarios

* The `general.properties` file has `browser=chrome`
* You ran the `BookRoomWebTest` class
* The `BaseWeb` class, in its preconditions method, will:
  * read the `browser` property value (`chrome`)
  * call the `DriverFactory.createInstance()` passing the browser as parameter
* The `DriverFactory.createInstance()` method will:
  * determine in the `switch-case` which browser must be instantiated (chrome)
  * create the browser instance using the driver manager class
* The driver manager class will:
  * create the driver (chrome) using the WebDriverManager
  * create and return the browser instance (chrome)
* The test will run in the Google Chrome browser
