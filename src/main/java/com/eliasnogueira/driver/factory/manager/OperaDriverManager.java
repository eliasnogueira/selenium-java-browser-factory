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
import com.eliasnogueira.exceptions.HeadlessNotSupportedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import static com.eliasnogueira.config.ConfigurationManager.configuration;
import static io.github.bonigarcia.wdm.config.DriverManagerType.OPERA;
import static java.lang.Boolean.TRUE;

public class OperaDriverManager implements IDriverManager<OperaOptions> {

    @Override
    public WebDriver createDriver() {
        WebDriverManager.getInstance(OPERA).setup();

        return new OperaDriver(getOptions());
    }

    @Override
    public OperaOptions getOptions() {
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.addArguments("--start-maximized");
        operaOptions.addArguments("--disable-infobars");
        operaOptions.addArguments("--disable-notifications");

        if (TRUE.equals(configuration().headless()))
            throw new HeadlessNotSupportedException();

        return operaOptions;
    }
}
