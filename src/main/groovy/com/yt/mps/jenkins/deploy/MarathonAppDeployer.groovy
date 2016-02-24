package com.yt.mps.jenkins.deploy

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.apache.http.HttpStatus

public class MarathonAppDeployer {

    final RESTClient restClient
    String appName;
    String tag;
    String jsonFilePath
    final String MARATHON_API_URI= "/marathon/v2/apps/"

    MarathonAppDeployer(String marathonUrl, String appName, String tag, String jsonFilePath){
        this.appName = appName;
        this.tag = tag;
        this.jsonFilePath =jsonFilePath
        restClient = new RESTClient(marathonUrl)
        restClient.handler.failure = { it }
    }

    void setCredentials(String username, String password) {
        restClient.headers['Authorization'] = 'Basic ' + "$username:$password".bytes.encodeBase64()
        restClient.ignoreSSLIssues()
    }

    boolean deploy(String appName){
        boolean success
        String jsonBody = readFileInWorkspace("$jsonFilePath/${appName}.json")

        if(isAppDeployed(appName)){
            success= upgradeApp(appName,jsonBody)
        }else{
            success= deployNewApp(appName, jsonBody)
        }
        println "$appName deployed : $success"
        success
    }

    boolean isAppDeployed(String appName){
        HttpResponseDecorator resp = restClient.get(
            path: "$MARATHON_API_URI/$appName",
            requestContentType: 'application/json'
        )
        if(resp.status != HttpStatus.SC_OK ){
            logFailure("$MARATHON_API_URI/$appName", resp.status, resp.reasonPhrase)
            return false;
        }
        return true;
    }

    boolean upgradeApp(String appName, String jsonBody){
        HttpResponseDecorator resp = restClient.put(
                path: "$MARATHON_API_URI/$appName",
                body: jsonBody,
                requestContentType: 'application/json',
        )
        if(resp.status != HttpStatus.SC_OK ){
            logFailure("$MARATHON_API_URI/$appName", resp. resp.status, resp.reasonPhrase)
            return false;
        }
        return true;
    }

    boolean deployNewApp(String appName, String jsonBody){
        HttpResponseDecorator resp = restClient.put(
                path: "$MARATHON_API_URI/$appName",
                body: jsonBody,
                requestContentType: 'application/json',
        )
        if(resp.status != HttpStatus.SC_OK ){
            logFailure("$MARATHON_API_URI/$appName", resp.status, resp.reasonPhrase)
            return false;
        }
        return true;

    }

    String readFileInWorkspace(String filePath) throws IOException {
        new File(filePath).text
    }

    private void logFailure(String uri ,HttpStatus status, String reason){
        println("ERROR: Http operation failed for : $uri, status code = $status, Reason = $reason")
        AssertionError
    }

}
