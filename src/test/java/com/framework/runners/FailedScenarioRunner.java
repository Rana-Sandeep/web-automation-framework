package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Re-runs only the scenarios that failed in the previous execution.
 * Usage:  mvn test -DsuiteXmlFile=testng-rerun.xml
 */
@CucumberOptions(
        features = "@target/failed_scenarios.txt",
        glue = {"com.framework.stepdefinitions", "com.framework.hooks"},
        plugin = {"pretty", "html:target/cucumber-reports/rerun.html"}
)
public class FailedScenarioRunner extends AbstractTestNGCucumberTests {
}
