package com.yt.mps.jenkins

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

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
                artifactNumToKeep(5)
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
