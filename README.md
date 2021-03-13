# Selenium Java Browser Factory

Don't forget to give this project a ⭐

* [Introduction](#introduction)
* [Technologies and Libraries](#technologies-and-libraries)
* [Technical explanation](#technical-explanation)
  * [DriverFactory](#driverFactory)
  * [BrowserFactory](#browserFactory)
    * [local browser instance creation](#local-browser-instance-creation)
    * [remote browser instance creation](#remote-browser-instance-creation)
* [Explaining the usage scenarios](#explaining-the-usage-scenarios)
  * [Local execution](#local-execution)
  * [Remote execution](#remote-execution)

## Introduction

This project shows how you can use the Factory design pattern to create different browsers using Selenium WebDriver 
for the web test automation.

This branch has the final recommendation.
If you don't know how to use it I would recommend you to take a look at the following branches:
* [basic-example](https://github.com/eliasnogueira/selenium-java-browser-factory/tree/basic-example): shows a basic and local implementation of the Factory design pattern
* [local-remote-example](https://github.com/eliasnogueira/selenium-java-browser-factory/tree/local-remote-example): shows a local and remote implementation of the Factory design pattern

The **local** implementation means that the tests will execute in your local machine.
The **remote** implementation means that the tests will execute in a remote machine (another physical machine, cloud, or grid).

## Technologies and Libraries

* [Java 11](https://openjdk.java.net/projects/jdk/11/) as the programming language
* [JUnit 5](https://junit.org/junit5/) to support the test creation
* [Selenium WebDriver](https://www.selenium.dev/) as the web browser automation framework using the Java binding
* [AssertJ](https://joel-costigliola.github.io/assertj/) as the fluent assertion library
* [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) as the Selenium binaries management
* [Owner](http://owner.aeonbits.org/) to minimize the code to handle the properties file

## Technical explanation

The factory implementation is based on:
* factory class to manage to execute the tests in the local or remote environment
* browser factory to create the browser instance for the local or remote execution
* driver manager that has one class per browser we need to use

### DriverFactory

The [DriverFactory](https://github.com/eliasnogueira/selenium-java-browser-factory/blob/master/src/main/java/com/eliasnogueira/driver/DriverFactory.java) 
class is responsible for creating the browser instance either for local or remote execution.
The target execution is managed by the property `target` on `general.properties` placed on `src/java/resources` folder.

When the property value is `local` it creates a local browser instance using the `createDriver()` method from the 
`BrowserFactory` class having the following code snippet:
```java
webdriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
```

When the property value is `remote` it creates a remote browser instance using the `getOptions()` method from the 
`BrowserFactory` class having the following code snippet:
```java
webdriver = createRemoteInstance(BrowserFactory.valueOf(browser.toUpperCase()).getOptions());
```

Note that the `createRemoteInstance` method is being used to connect to a remote environment.
The remote environment can be configured by changing the `grid.properties` file placed on `src/main/java/resources` folder.
You can create your remote approach or use Selenium Grid. You can find a `docker-compose.yml` file in this project 
root folder to create the Selenium 4 Grid and run the test.

### BrowserFactory

The [BrowserFactory](https://github.com/eliasnogueira/selenium-java-browser-factory/blob/master/src/main/java/com/eliasnogueira/driver/BrowserFactory.java) 
enum is responsible to create either the local browser instance or the capabilities to send to remote execution.

It's an enumeration to simplify the factory creation, but we will get there.
It has two abstract methods to make all the enuns implement the same methods:
```java
public abstract WebDriver createDriver();
public abstract AbstractDriverOptions<?> getOptions();
```

The `createDriver()` is responsible for the local browser instance creation where the `getOptions()` is responsible for 
the remote browser instance creation.

#### local browser instance creation

It uses the WebDriverManager to manage the browser driver and, after that, create a new instance for the browser associated 
by it enum. This is the example for the Google Chrome browser:

```java
@Override
public WebDriver createDriver() {
    WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

    return new ChromeDriver(getOptions());
}
```

Note that the `ChromeDriver()` class has the `getOptions()` method as it parameters.
The `getOptions` has two intents: 
* set the common options to execute the test for the targeting browser
* be used to create the browser instance in the remote execution

#### remote browser instance creation

Remote test executions using Selenium are based on the [RemoteWebDriver](https://www.selenium.dev/documentation/en/remote_webdriver/remote_webdriver_client/) 
instead of the `WebDriver` class. To tell the `RemoteWebDriver` the browser we need to run the tests we must set the [BrowserOptions](https://www.selenium.dev/documentation/en/remote_webdriver/remote_webdriver_client/#browser-options) 
that are usually called Capabilities.

Each browser that supports Selenium has its `Options` class. When we explicitly create an Options instance the `RemoteWebDriver` 
will know the browser to use. In the example below we are using the `ChromeOptions`, so the `RemoteWebDriver` 
will know that the Google Chrome browser must be used in the remote test execution.

```java
@Override
public ChromeOptions getOptions() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments(START_MAXIMIZED);
    chromeOptions.addArguments("--disable-infobars");
    chromeOptions.addArguments("--disable-notifications");
    chromeOptions.setHeadless(configuration().headless());

    return chromeOptions;
}
```
We are also passing some parameters to the browser options.

If you take a look at the `DriverFactory` class you will see that the `createRemoteInstance()` method is creating a new 
instance of the `RemoteWebDriver` class, and it needs the capability (our options class) to determine in which browser 
the test must run.

## Explaining the usage scenarios

### Local execution

* The `general.properties` file has `target=local` and `browser=chrome`
* You ran the `BookRoomWebTest` class  
* The `BaseWeb` class, in its preconditions method, will:
  * read the `browser` property value (`chrome`)
  * call the `DriverFactory.createInstance()`
* The `DriverFactory.createInstance()` method will:
  * read the `target` attribute from the `general.properties` which will be `local` for this scenario
  * determine in the `switch-case` that the local execution must happen
  * create a local browser instance calling `BrowserFactory.createBrowser()`
* The `BrowserFactory` will match with the browser name we are using (`chrome`) and will call `createDriver()` method
  * the `CHROME` enum for the `createDriver()` is using the WebDriverManager to create and initiate the browser driver
  * and after it's creating the browser instance
* The test will run in the Google Chrome browser
  
### Remote execution

* The `general.properties` file has `target=remote` and `browser=firefox`
* You ran the `docker-compose.yml` to initiate the Selenium 4 Grid
* You ran the `BookRoomWebTest` class
* The `BaseWeb` class, in its preconditions method, will:
    * read the `browser` property value (`firefox`)
    * call the `DriverFactory.createInstance()`
* The `DriverFactory.createInstance()` method will:
    * read the `target` attribute from the `general.properties` which will be `remote` for this scenario
    * determine in the `switch-case` that the remote execution must happen
    * create a remote browser instance calling `BrowserFactory.getOptions()`  
    * start the `RemoteWebDriver` session based on the `getOptions` for the browser
* The test will run in the Google Chrome browser    