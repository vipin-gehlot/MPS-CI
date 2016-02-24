package com.yt.mps.jenkins.deploy


String marathonUrl = System.getProperty('marathonUrl')

String appName = System.getProperty('appName')
String tag = System.getProperty('tag')
String jsonFilePath = System.getProperty('jsonFilePath')
String username = System.getProperty('username')
String password = System.getProperty('password')

if (!marathonUrl || !appName || !jsonFilePath || !tag) {
    println 'usage: -DmarathonUrl=<marathonUrl> -DappName=<appName> [-Dusername=<username>] [-Dpassword=<password>] [-DjsonFilePath=<jsonFilePath>] [-Dtag=<tag>]'
    System.exit 1
}

MarathonAppDeployer deployer = new MarathonAppDeployer(marathonUrl, appName, tag, jsonFilePath);

if(username && password){
    deployer.setCredentials username, password
}

boolean success = deployer.deploy(appName)


