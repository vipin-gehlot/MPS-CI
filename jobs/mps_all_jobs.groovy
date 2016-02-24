import com.yt.mps.jenkins.GradleCiJobBuilder
import com.yt.mps.jenkins.PipeLineOptions

/**
 * Created by vipin.gehlot on 11/19/2015.
 */

String folderName
String gitBranch='develop'
String gradleVersion='gradle-2.7'
String gitSSHCredentials='eb99a03b-f05b-4bc1-b81e-636f73652ff9'

def defaultPipelinePhases = PipeLineOptions.getDefaultOptions()

readFileFromWorkspace("resources/projects.txt").eachLine {
    println(" Project Configuration : " + it.toString());
    def buildOptions = it.split(",")
    def projectName = buildOptions[0]
    def repositoryUrl = buildOptions[1]
    def projectDesc = buildOptions[2]

    def userPipeLinePhases = defaultPipelinePhases;

    if(buildOptions.length>=4){
        def tmp_pl_phase = buildOptions[3]
        if(!tmp_pl_phase.isEmpty()){
            userPipeLinePhases = tmp_pl_phase
        }
    }
    folderName = projectName

    folder(folderName) {
        displayName(projectName)
        description(projectDesc)
    }

    new GradleCiJobBuilder(
            name: "$folderName/$projectName-build",
            description: PipeLineOptions.BUILD.getDesc(),
            gitUrl: repositoryUrl,
            gitSSHCredentials: gitSSHCredentials,
            downStreamProject: "$folderName/$projectName-dockerArtifacts",
            gradleVersion: gradleVersion,
            runJobOnNode: "ops-tjs-mps1.prd.yaanatech.com",
            tasks: 'clean build runIT copyCoverageData copyIntegrationCoverageData mergeCoverageData generateBranchName sonarRunner',
    ).buildJob(this)

    if (userPipeLinePhases.contains(PipeLineOptions.DOCKER_ARTIFACTS.name())) {
        println("Creating Docker artifacts generation Job ... ")
        new GradleCiJobBuilder(
                name: "$folderName/$projectName-dockerArtifacts",
                description: PipeLineOptions.DOCKER_ARTIFACTS.getDesc(),
                gitUrl: repositoryUrl,
                gitSSHCredentials: gitSSHCredentials,
                downStreamProject: "$folderName/$projectName-QA-Deploy",
                gradleVersion: gradleVersion,
                runJobOnNode: "ops-tjs-mps1.prd.yaanatech.com",
                tasks: 'buildDocker',
        ).buildJob(this)
    }
    if (userPipeLinePhases.contains(PipeLineOptions.QA_DEPLOY.name())) {
        println("Creating QA deployment Job ... ")
        job("$folderName/$projectName-QA-Deploy") {
            description(PipeLineOptions.QA_DEPLOY.getDesc())
            logRotator(5,5)

            /*environmentVariables {
                propertiesFile('resources/qa/env.properties')
            }*/
            steps {
                shell readFileFromWorkspace('resources/qa-deploy.sh')
            }
            publishers {
                downstream("$folderName/$projectName-SanityTests")
            }
        }
    }
    if (userPipeLinePhases.contains(PipeLineOptions.TEST_SANITY.name())) {
        println("Creating Sanity Testing Job ... ")
        mavenJob("$folderName/$projectName-SanityTests") {
            description(PipeLineOptions.TEST_SANITY.getDesc())
            logRotator(5,5)
            scm {
                git {
                    remote {
                        url 'git@ops-tgit1.prd.yaanatech.com:MPS/mps-automation.git'
                        credentials(gitSSHCredentials)
                    }
                    branch(gitBranch)
                    localBranch('${GIT_BRANCH}')
                }
            }
            rootPOM("pom.xml")
            goals("clean install -skipTest=true")
            publishers {
                //TODO: Publish the test results
                downstream("$folderName/$projectName-RegressionTest", "SUCCESS")
            }
        }
    }

    if (userPipeLinePhases.contains(PipeLineOptions.TEST_REGRESSION.name())) {
        println("Creating Regression Testing Job ... ")
        mavenJob("$folderName/$projectName-RegressionTest") {
            description(PipeLineOptions.TEST_REGRESSION.getDesc())
            logRotator(5,5)
            scm {
                git {
                    remote {
                        url 'git@ops-tgit1.prd.yaanatech.com:MPS/mps-automation.git'
                        credentials(gitSSHCredentials)
                    }
                    branch(gitBranch)
                    localBranch('${GIT_BRANCH}')
                }
            }
            rootPOM("pom.xml")
            goals("clean install -skipTest=true")
            publishers {
                //TODO: publish test results
            }
        }
    }

    if (userPipeLinePhases.contains(PipeLineOptions.STAG_DEPLOY.name())) {
        println("Creating Stag Deployment Job ... ")
        job("$folderName/$projectName-Stag-Deploy") {
            description(PipeLineOptions.STAG_DEPLOY.getDesc())
            logRotator(5,5)
            steps {
                shell readFileFromWorkspace('resources/stag-deploy.sh')
            }
            publishers {

            }
        }
    }

    if (userPipeLinePhases.contains(PipeLineOptions.PROD_DEPLOY.name())) {
        println("Creating Production Deployment Job ... ")
        job("$folderName/$projectName-Prod-Deploy") {
            description(PipeLineOptions.PROD_DEPLOY.getDesc())
            logRotator(5,5)
            steps {
                shell readFileFromWorkspace('resources/prod-deploy.sh')
            }
            publishers {

            }
        }
    }
    if (userPipeLinePhases.contains(PipeLineOptions.PROD_DEPLOY.name())) {
        println("Creating build pipeline ... ")
        buildPipelineView("$folderName/$projectName-Pipeline") {
            displayedBuilds(5)
            title("$projectName-Pipeline")
            selectedJob("$folderName/$projectName-build")
            consoleOutputLinkStyle(OutputStyle.Lightbox)
            showPipelineDefinitionHeader(true)
            showPipelineParameters(true)
        }
    }
}