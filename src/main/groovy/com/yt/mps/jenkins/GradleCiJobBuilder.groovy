package com.yt.mps.jenkins

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Example Class for creating a Gradle build
 */
class GradleCiJobBuilder {
    String folder
    String name
    String description
    String gitUrl
    String gitSSHCredentials
    String gitBranch = 'origin/develop'
    String downStreamProject
    String gradleVersion = 'gradle-2.7'
    String pollScmSchedule = 'H/05 * * * *'
    String tasks
    String switches =''
    Boolean useWrapper = false
    //String junitResults = '**/build/test-results/*.xml'
    //String artifacts = '**/build/libs/*.jar'
    List<String> emails = []
    //HashMap envVarMap = []
    String runJobOnNode;

    Job buildJob(DslFactory dslFactory) {
        dslFactory.job(name) {
            if (folder) {
                dslFactory.folder(folder) {
                    displayName(folder)
                    description(folder)
                }
                name = folder + "/" + name
            }
            it.description this.description
            logRotator(5,5)
            blockOnDownstreamProjects()
            if (runJobOnNode) {
                label(runJobOnNode)
            }

            scm {
                git {
                    remote {
                        url gitUrl
                        credentials(this.gitSSHCredentials)
                    }
                    branch(this.gitBranch)
                    localBranch('${GIT_BRANCH}')
                }
            }
            triggers {
                scm this.pollScmSchedule
            }
            steps {
                gradle { node ->
                    tasks(this.tasks)
                    switches(this.switches)
                    useWrapper(this.useWrapper)
                    gradleName(gradleVersion)
                }
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