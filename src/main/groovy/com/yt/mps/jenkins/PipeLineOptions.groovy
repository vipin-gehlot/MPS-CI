package com.yt.mps.jenkins
/**
 * Created by vipin.gehlot on 11/26/2015.
 */

//build:dockerArtifacts:QADeploy:sanityTest:regressionTest:stagDeploy:prodDeploy:pipeline

public enum PipeLineOptions {

    BUILD("Ths job build the project and runs integration tests, static code analyser tools to provide the analysis in SonnarQube"),
    DOCKER_ARTIFACTS("The job builds the docker images and push to Docker repository"),
    QA_DEPLOY("QA Deployment"),
    TEST_SANITY("Runs the tests to check the sanity of the systems after deployment"),
    TEST_REGRESSION("regressionTest"),
    STAG_DEPLOY("Staging Deployment"),
    PROD_DEPLOY("Production Deployment"),
    PIPELINE("Creates build pipeline for component(s)")

    private String desc;
    private PipeLineOptions(String desc){
        this.desc = desc;
    }

    public getDesc(){
        desc;
    }


    public static String getDefaultOptions(){
        return new StringBuilder()
            .append(BUILD.name()).append("|")
            .append(DOCKER_ARTIFACTS.name()).append("|")
            .append(QA_DEPLOY.name()).append("|")
            .append(TEST_SANITY.name()).append("|")
            .append(TEST_REGRESSION.name()).append("|")
            .append(STAG_DEPLOY.name()).append("|")
            .append(PROD_DEPLOY.name()).append("|")
            .append(PIPELINE.name())
            .toString();
    }
}
