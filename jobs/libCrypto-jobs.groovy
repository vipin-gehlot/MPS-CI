/**
 * Created by vipin.gehlot on 11/19/2015.
 */

import com.yt.mps.jenkins.GradleCiJobBuilder

String basePath='libCrypto-lib'
//String gitBranch='develop'
String gradleVersion='gradle-2.7'
String gitSSHCredentials='eb99a03b-f05b-4bc1-b81e-636f73652ff9'
String gitUrl='git@ops-tgit1.prd.yaanatech.com:MPS/libcrypto.git'

folder(basePath){
    displayName('libCrypto')
    description("Cryptography library for secure Messaging service")
}

new GradleCiJobBuilder(
        name: "$basePath/libCrypto-build",
        description: 'Ths job build the project and run the unit test cases',
        gitUrl: gitUrl,
        gitSSHCredentials: gitSSHCredentials,
        downStreamProject: "$basePath/libCrypto-QC",
        gradleVersion: gradleVersion,
        tasks: 'clean build',
).buildJob(this)

new GradleCiJobBuilder(
        name: "$basePath/libCrypto-QC",
        description: 'Ths job runs the integration tests and code analyser tools PWD, findBugs and Jacoco and provide the analysis in SonnarQube',
        gitUrl: gitUrl,
        gitSSHCredentials: gitSSHCredentials,
        gradleVersion: gradleVersion,
        tasks: 'runIT copyCoverageData copyIntegrationCoverageData mergeCoverageData generateBranchName sonarRunner',
).buildJob(this)


new GradleCiJobBuilder(
        name: "$basePath/libCrypto-Release",
        description: 'Ths job release the new version of library in archiva',
        gitUrl: gitUrl,
        gitSSHCredentials: gitSSHCredentials,
        gradleVersion: gradleVersion,
        tasks: 'clean build release',
).buildJob(this)



