# Generic Gatling Load Test
Solution encapsulates [Gatling Closed Model Simulation](https://gatling.io/docs/gatling/reference/current/core/injection/) and applicable for quick generic REST load tests.

## Overview
Solution is applicable for the most of the general cases, where quick answer on Performance is required. It is designed to perform generic REST load tests, so that has some limitations. In case there are specific business cases, advanced logic, requests chaining, solution can be modified and/or adapted to serve the specific need.

✅ supported  
🚧 planned  
❌ not planned

### Request Settings
#### HTTP Methods
✅ GET  
✅ POST  
🚧 PUT  
🚧 PATCH  
🚧 DELETE  
🚧 OPTIONS

#### Request Parameters
✅ Single endpoint  
❌ Multiple endpoints  
✅ Query Parameters for a single request  
✅ Request body from a file for a single request  
🚧 Multiple Query Parameters for a different requests distribution  
✅ Multiple request bodies from file for a different requests distribution  
✅ Custom Headers  
❌ Authorization Header (Can use `Authorization=Bearer ...` custom Header instead)

### Load Settings
✅ Configurable Load duration  
✅ Configurable number of constant Requests per Second (Active Users)  
✅ Warm-Up — time to get from zero to desired number of Requests per Second (Active Users)  
✅ Log responses — ability to get data on what exactly was returned during Load  
❌ Stress Test (Can use a workaround, set 0 duration and a desired warm-up to simulate stress)

### Checks Settings
✅ Expected Status Code  
✅ Expected Response Time (in context of 95th percentile)  
✅ Expected Response Length  
✅ Expected Percent of Successful Requests  
❌ Expected Status change during the Load Test

### Notes
There is a separate password type field `x-api-key` to use for secured resources while not exposing one. In cae you resources you test do not require this, just ignore it. 

## Execution
### Run with Jenkins
Using Jenkins is the most convenient and easy way to run quick Generic Load Test.

#### Steps to run on Jenkins:
* Create a pipeline project
* Define path to project's Git repo in `Pipeline script from SCM`
* Run `Build Now` — this action will create all the necessary parameters
* Run `Build with Parameters` — now fill out all the parameters and run actual Load Test

#### Required Plugins
* [Gatling](https://plugins.jenkins.io/gatling/)
* [File Parameter](https://plugins.jenkins.io/file-parameters/)
* [Parameter Separator](https://plugins.jenkins.io/parameter-separator/)
* [HTML Publisher](https://plugins.jenkins.io/htmlpublisher/)
* [Pipeline Utility Steps](https://plugins.jenkins.io/pipeline-utility-steps/)
* [Pipeline Maven Integration](https://plugins.jenkins.io/pipeline-maven/)

#### Notes
Your Jenkins Job should not have spaces in the name in order to correctly accept file for the request body

### Run Locally
You would need to define required Request and Simulation parameters as Environment Variables. Have a look at Jenkinsfile (`parameters` section) to get understanding on what to be defined.

In case you're running `POST` request, place `request_body.json` with request body under `src/main/resources`