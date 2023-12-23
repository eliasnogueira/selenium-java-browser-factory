/*
 * MIT License
 *
 * Copyright (c) 2021 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.eliasnogueira.driver.factory;

import com.eliasnogueira.driver.IDriverFactory;
import com.eliasnogueira.driver.factory.manager.ChromeDriverManager;
import com.eliasnogueira.driver.factory.manager.FirefoxDriverManager;
import com.eliasnogueira.driver.factory.manager.EdgeDriverManager;
import com.eliasnogueira.driver.factory.manager.SafariDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.eliasnogueira.config.ConfigurationManager.configuration;

public class RemoteDriverFactory implements IDriverFactory {

    private static final Logger logger = Logger.getLogger("com.eliasnogueira");

    @Override
    public WebDriver createInstance(String browser) {
        MutableCapabilities capability;
        BrowserList browserToCreate = BrowserList.valueOf(browser.toUpperCase());

        capability = switch (browserToCreate) {
            case CHROME -> new ChromeDriverManager().getOptions();
            case FIREFOX -> new FirefoxDriverManager().getOptions();
            case EDGE -> new EdgeDriverManager().getOptions();
            case SAFARI -> new SafariDriverManager().getOptions();
        };

        return createRemoteInstance(capability);
    }

    private RemoteWebDriver createRemoteInstance(MutableCapabilities capability) {
        RemoteWebDriver remoteWebDriver = null;
        try {
            String gridURL = String.format("http://%s:%s", configuration().gridUrl(), configuration().gridPort());

            remoteWebDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), capability);
        } catch (java.net.MalformedURLException e) {
            logger.log(Level.SEVERE, "Grid URL is invalid or Grid is not available");
            logger.log(Level.SEVERE, String.format("Browser: %s", capability.getBrowserName()), e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, String.format("Browser %s is not valid or recognized", capability.getBrowserName()), e);
        }

        return remoteWebDriver;
    }
}
