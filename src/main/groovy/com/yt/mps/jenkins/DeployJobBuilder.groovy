package com.yt.mps.jenkins

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Created by vipin.gehlot on 11/25/2015.
 */
class DeployJobBuilder {

    String name
    String description
    String deployScript=""
    List<String> emails = []
    String downStreamProject
    Job buildJob(DslFactory dslFactory) {
        dslFactory.job(name){
            it.description(this.description)
            logRotator {
                numToKeep(5)
                artifactNumToKeep(1)
            }
            steps{
                shell (readFileFromWorkspace('resources/qa-deploy.sh'))
            }
            publishers {
                if (emails) {
                    mailer emails.join(' ')
                }
                if (this.downStreamProject) {
                    downstream(this.downStreamProject)
                }
            }
        }
    }
}
