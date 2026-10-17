pipeline {
    agent any

    tools {
        // Names must match Manage Jenkins > Tools
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        choice(name: 'BROWSER',  choices: ['chrome', 'firefox', 'edge'], description: 'Browser to run on')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser headless')
        choice(name: 'ENV',      choices: ['qa', 'staging'], description: 'Target environment')
        string(name: 'TAGS',     defaultValue: 'not @wip', description: 'Cucumber tag expression, e.g. @smoke or @regression')
        string(name: 'THREADS',  defaultValue: '3', description: 'Parallel scenarios')
    }

    triggers {
        githubPush()                 // fires on GitHub webhook
        cron('H 2 * * 1-5')          // nightly run, weekdays
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                mvn 'clean compile -B -q'
            }
        }

        stage('Run Tests') {
            steps {
                mvn "test -B -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Denv=${params.ENV} " +
                    "-Dcucumber.filter.tags=\"${params.TAGS}\" -Ddataproviderthreadcount=${params.THREADS}"
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'

            publishHTML(target: [
                allowMissing         : true,
                alwaysLinkToLastBuild: true,
                keepAll              : true,
                reportDir            : 'target/extent-report',
                reportFiles          : 'ExtentSpark.html',
                reportName           : 'Extent Report'
            ])

            archiveArtifacts artifacts: 'target/extent-report/**, target/cucumber-reports/**, logs/**',
                             allowEmptyArchive: true, fingerprint: true
        }
        failure {
            echo 'Build failed. Check the Extent Report for screenshots.'
            // Uncomment once the Email Extension plugin is configured:
            // emailext subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //          body: "See ${env.BUILD_URL}",
            //          to: 'team@example.com'
        }
        cleanup {
            cleanWs()
        }
    }
}

// Works on both Linux and Windows agents
def mvn(String args) {
    if (isUnix()) {
        sh "mvn ${args}"
    } else {
        bat "mvn ${args}"
    }
}
