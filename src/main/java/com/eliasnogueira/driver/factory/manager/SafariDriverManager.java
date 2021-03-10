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

package com.eliasnogueira.driver.factory.manager;

import com.eliasnogueira.driver.IDriverManager;
import com.eliasnogueira.exceptions.BrowserNotSupportedException;
import com.eliasnogueira.exceptions.HeadlessNotSupportedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import static com.eliasnogueira.config.ConfigurationManager.configuration;
import static io.github.bonigarcia.wdm.config.DriverManagerType.SAFARI;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC;

public class SafariDriverManager implements IDriverManager<SafariOptions> {

    @Override
    public WebDriver createDriver() {
        if (!IS_OS_MAC) throw new
                BrowserNotSupportedException("Safari is not supported on" + System.getProperty("os.name"));

        WebDriverManager.getInstance(SAFARI).setup();
        return new SafariDriver(getOptions());
    }

    @Override
    public SafariOptions getOptions() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setAutomaticInspection(false);

        if (TRUE.equals(configuration().headless()))
            throw new HeadlessNotSupportedException();

        return safariOptions;
    }
}
