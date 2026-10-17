package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Main runner. Filter scenarios from the CLI with:  -Dcucumber.filter.tags="@smoke and not @wip"
 * Parallelism is driven by data-provider-thread-count in testng.xml.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.framework.stepdefinitions", "com.framework.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/failed_scenarios.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
