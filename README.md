# Web Automation Framework

Industry-style UI test automation framework built with **Java 17, Selenium 4, TestNG, Cucumber (BDD)** and Maven,
with CI/CD via **GitHub Actions** and **Jenkins**.

Application under test: [SauceDemo](https://www.saucedemo.com) (public practice site).

## Highlights

- **BDD** with Gherkin feature files, tag-based execution (`@smoke`, `@regression`, `@negative`)
- **Page Object Model** with a `BasePage` that wraps explicit waits (no `Thread.sleep`, no implicit waits)
- **Thread-safe parallel execution** (`ThreadLocal<WebDriver>` + TestNG parallel data provider)
- **Dependency injection** (PicoContainer) to share state between step classes
- **Config layering**: `config.properties` -> `{env}.properties` -> `-D` CLI overrides
- **Cross-browser** (Chrome / Firefox / Edge), headless mode, optional **Selenium Grid / Docker** via `remote.url`
- **Reporting**: Extent Spark HTML report, Cucumber HTML/JSON, TestNG/JUnit XML; screenshot auto-attached on failure
- **Logging** with Log4j2 (console + `logs/automation.log`)
- **Failed-scenario rerun** runner
- **CI/CD**: GitHub Actions workflow + declarative Jenkinsfile (parameterised, nightly + webhook triggered)

## Project structure

```
.
├── Jenkinsfile
├── .github/workflows/ci.yml
├── testng.xml                      # suite + parallel thread count
├── testng-rerun.xml                # rerun only failed scenarios
├── pom.xml
└── src
    ├── main/java/com/framework
    │   ├── config/ConfigReader.java
    │   ├── driver/DriverFactory.java, DriverManager.java
    │   └── pages/BasePage.java, LoginPage.java, InventoryPage.java, CartPage.java
    └── test
        ├── java/com/framework
        │   ├── context/TestContext.java
        │   ├── hooks/Hooks.java
        │   ├── stepdefinitions/LoginSteps.java, CartSteps.java
        │   └── runners/TestRunner.java, FailedScenarioRunner.java
        └── resources
            ├── features/login.feature, cart.feature
            ├── config/config.properties, qa.properties, staging.properties
            ├── extent.properties, cucumber.properties, log4j2.xml
```

## Prerequisites

JDK 17+, Maven 3.9+, and Chrome (or Firefox/Edge). Selenium Manager downloads the matching driver automatically.

## Running tests

```bash
mvn clean test                                              # all scenarios, Chrome
mvn clean test -Dheadless=true                              # headless
mvn clean test -Dbrowser=firefox                            # other browser
mvn clean test -Dcucumber.filter.tags="@smoke"              # by tag
mvn clean test -Dcucumber.filter.tags="@regression and not @negative"
mvn clean test -Denv=staging                                # environment overlay
mvn clean test -Dremote.url=http://localhost:4444           # Selenium Grid
mvn test -DsuiteXmlFile=testng-rerun.xml                    # rerun failures
```

Reports after a run:

| Report | Path |
|---|---|
| Extent Spark | `target/extent-report/ExtentSpark.html` |
| Cucumber HTML / JSON | `target/cucumber-reports/` |
| TestNG / JUnit XML | `target/surefire-reports/` |
| Logs | `logs/automation.log` |

## Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit: Selenium + TestNG + Cucumber framework"
git branch -M main
git remote add origin https://github.com/<your-username>/web-automation-framework.git
git push -u origin main
```

GitHub Actions starts automatically on push/PR and nightly. Results appear under the **Actions** tab, and reports
are downloadable from the run's **Artifacts** section.

## Jenkins setup

1. **Install plugins**: Git, GitHub, Pipeline, HTML Publisher, JUnit (and optionally Email Extension).
2. **Configure tools** (Manage Jenkins > Tools): Maven named `Maven-3.9`, JDK named `JDK-17`.
   The Jenkins agent needs Chrome installed (or point `remote.url` to a Selenium Grid).
3. **Create the job**: New Item > *Pipeline* (or *Multibranch Pipeline*) > Pipeline script from SCM >
   Git > repo URL > branch `main` > Script Path `Jenkinsfile`. Add GitHub credentials (PAT) for private repos.
4. **Webhook** (instant builds on push): GitHub repo > Settings > Webhooks > Payload URL
   `http://<jenkins-host>:8080/github-webhook/`, content type `application/json`, event *push*.
   For a local Jenkins, expose it with `ngrok http 8080` and use the ngrok URL.
5. **Run**: *Build with Parameters* to choose browser, environment, tags and thread count.

## Design decisions (interview talking points)

- **Why ThreadLocal driver?** Each parallel scenario gets an isolated browser, so tests don't leak state.
- **Why PicoContainer?** Step classes stay small and share one `TestContext` per scenario without static state.
- **Why explicit waits only?** Mixing implicit and explicit waits causes unpredictable timeouts.
- **Why Selenium Manager?** Removes driver binary management and a third-party dependency.
- **Why layered config?** Same code runs in any environment or CI without edits; CLI always wins.
- **Why tags?** Fast smoke on every PR, full regression nightly.

## Ideas to extend

API tests with RestAssured, Docker Selenium Grid (`docker-compose`), Allure reporting, Dockerfile for the test
runner, SonarQube/Checkstyle quality gates, and Slack/Teams notifications from the pipeline.
