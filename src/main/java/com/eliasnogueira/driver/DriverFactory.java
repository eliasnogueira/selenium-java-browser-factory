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

package com.eliasnogueira.driver;

import com.eliasnogueira.driver.factory.LocalDriverFactory;
import com.eliasnogueira.driver.factory.RemoteDriverFactory;
import com.eliasnogueira.exceptions.TargetNotValidException;
import org.openqa.selenium.WebDriver;

import static com.eliasnogueira.config.ConfigurationManager.configuration;

public class DriverFactory {

    public WebDriver createInstance(String browser) {
        Target target = Target.valueOf(configuration().target().toUpperCase());
        WebDriver webdriver;

        switch (target) {
            case LOCAL:
                webdriver = new LocalDriverFactory().createInstance(browser);
                break;
            case REMOTE:
                webdriver = new RemoteDriverFactory().createInstance(browser);
                break;
            default:
                throw new TargetNotValidException(target.toString());
        }
        return webdriver;
    }

    enum Target {
        LOCAL, REMOTE
    }
}
