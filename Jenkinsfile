pipeline {
    agent any
    parameters {
        gitParameter(branch: '',
                defaultValue: 'origin/main',
                description: '',
                name: 'BRANCH_OR_TAG',
                quickFilterEnabled: false,
                sortMode: 'NONE',
                tagFilter: '*',
                type: 'PT_BRANCH_TAG')
        separator(name: "SERVICE_PARAMETERS", sectionHeader: "Service, environment and request",
                separatorStyle: "border-width: 0",
                sectionHeaderStyle: """
				font-size: 22px;
				font-weight: bold;
			"""
        )
        choice(name: 'HTTP_METHOD', choices: ['GET', 'POST'], description: '')
        password(name: 'X_API_KEY', defaultValue: '', description: '')
        string(name: 'BASE_URL', defaultValue: '', description: 'Example: https://jsonplaceholder.typicode.com')
        string(name: 'ENDPOINT', defaultValue: '', description: 'Example: /posts')
        string(name: 'QUERY_PARAMS', defaultValue: '', description: 'Example: ?userId=1')
        base64File 'REQUEST_BODY'
        booleanParam(name: 'REQUEST_BODY_IS_ARRAY', defaultValue: false, description: 'Treat request body file as an array of requests to be used as Feeder and used randomly during simulation\nUse REQUEST_BODY_ARRAY_MAPPING field to map request content. Read on Gatling JSON Feeders for syntax help')
        base64File 'REQUEST_BODY_ARRAY_MAPPING'

        separator(name: "SIMULATION_PARAMETERS", sectionHeader: "Load simulation configuration",
                separatorStyle: "border-width: 0",
                sectionHeaderStyle: """
				font-size: 22px;
				font-weight: bold;
			"""
        )
        string(name: 'CONSTANT_CONCURRENT_USERS', defaultValue: '', description: 'Closed model will produce this constant number of users and will keep them during simulation\nLeave empty for default value: 10')
        string(name: 'DURATION_MIN', defaultValue: '', description: 'Leave empty for default value: 1')
        string(name: 'USER_RAMP_DURING_SEC', defaultValue: '', description: 'Raise number of active users from 0 to defined in CONSTANT_CONCURRENT_USERS after the delay at start during given period of time\nLeave  empty for default value: 15')
        booleanParam(name: 'LOG_RESPONSE', defaultValue: false, description: 'Writes all responses during simulation to the default console output')

        separator(name: "CHECKS_PARAMETERS", sectionHeader: "What to check during the Load Test",
                separatorStyle: "border-width: 0",
                sectionHeaderStyle: """
				font-size: 22px;
				font-weight: bold;
			"""
        )
        string(name: 'P95_RESPONSE_TIME_MS', defaultValue: '', description: '95th percentile of response time to treat successful (in ms)\nLeave  empty for default value: 1000')
        string(name: 'SUCCESS_PERCENT', defaultValue: '', description: 'Percent of requests that return EXPECTED_STATUS_CODE response code and body length more than EXPECTED_MIN_BODY_LENGTH value\nLeave  empty for default value: 95.0')
        string(name: 'EXPECTED_STATUS_CODE', defaultValue: '', description: 'Check Status code should be equal to this value\nLeave  empty for default value: 200')
        string(name: 'EXPECTED_MIN_BODY_LENGTH', defaultValue: '', description: 'Check Response Body length in bytes must be greater than defined here\nLeave  empty for default value: 100')
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Gatling Load Test') {
            steps {
                withFileParameter(name:'REQUEST_BODY', allowNoFile: true) {
                    sh 'mv $REQUEST_BODY $WORKSPACE/src/main/resources/request_body.json'
                }
                withFileParameter(name:'REQUEST_BODY_ARRAY_MAPPING', allowNoFile: true) {
                    sh 'mv $REQUEST_BODY_ARRAY_MAPPING $WORKSPACE/src/main/resources/request_body_array_map.txt'
                }
                withMaven(mavenOpts: '--add-opens java.base/java.lang=ALL-UNNAMED') {
                    sh 'mvn gatling:test'
                }

            }
        }
    }
    post {
        always {
            script {
                def htmlFiles
                dir ('') {
                    htmlFiles = findFiles glob: '**/index.html'
                }
                publishHTML([
                        reportDir: '',
                        reportFiles: htmlFiles.join(','),
                        reportTitles: 'Gatling',
                        reportName: 'Load Test Result',
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true])
            }
            gatlingArchive()
        }
        cleanup {
            cleanWs()
        }
    }
}