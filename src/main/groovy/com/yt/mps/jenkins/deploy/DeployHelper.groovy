package com.yt.mps.jenkins.deploy


String marathonUrl = System.getProperty('MARATHON_URL')

String appName = System.getProperty('APP_NAME')
String tag = System.getProperty('TAG')
String jsonFilePath = System.getProperty('JSON_FILES_PATH')
String username = System.getProperty('MARATHON_USER')
String password = System.getProperty('MARATHON_PASSWORD')

if (!marathonUrl || !jsonFilePath || !tag) {
    println 'usage: -DMARATHON_URL=<marathonUrl>  -DJSON_FILES_PATH=<jsonFilePath> -DTAG=<tag> [-DAPP_NAME=<appName>] [-DMARATHON_USER=<username>] [-DMARATHON_PASSWORD=<password>]'
    System.exit 1
}

if(!appName) appName='mock_nsg'

MarathonAppDeployer deployer = new MarathonAppDeployer(marathonUrl, appName, tag, jsonFilePath);

if(username && password){
    deployer.setCredentials username, password
}

boolean success = deployer.deploy(appName)

//TODO Deployment verification
