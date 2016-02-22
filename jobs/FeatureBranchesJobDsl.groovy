import com.yt.mps.jenkins.GradleCiJobBuilder
import com.yt.mps.jenkins.JenkinsViewBuilder
import com.yt.mps.jenkins.rest.GitlabRestClient
import com.yt.mps.jenkins.util.GitlabProjects
import javaposse.jobdsl.dsl.DslFactory

String gradleVersion = 'gradle-2.7'
String gitSSHCredentials = 'eb99a03b-f05b-4bc1-b81e-636f73652ff9'

GitlabProjects.values().each { project ->
    def gitProject = GitlabRestClient.getProject(project.getProjectId())
    if (gitProject) {
        final String projectName = GitlabRestClient.getName(gitProject)
        println "##################### Generating jobs for project - $projectName #####################"
        final List<Object> branches = GitlabRestClient.getBranches(project.getProjectId());
        final String getSshUrlToGitRepo = GitlabRestClient.getSshUrlToGitRepo(gitProject)
        def viewOrFolderName = projectName + "-features"
        final List<String> projectFeaturesJobsList = new ArrayList();
        branches.each { branch ->
            String branchName = GitlabRestClient.getName(branch)
            if (branchName.matches("feature.*")) {
                String removePrefixFeature = branchName.replace("feature/" , "");
                final String buildJobName = "$removePrefixFeature-build"

                def buildJobDescription = "Build Job for $branchName"
                def ciJobBuilder = new GradleCiJobBuilder(
                        folder: viewOrFolderName,
                        name: buildJobName,
                        description: buildJobDescription,
                        gitUrl: getSshUrlToGitRepo,
                        gitSSHCredentials: gitSSHCredentials,
                        gradleVersion: gradleVersion,
                        runJobOnNode: "ops-tjs-mps1.prd.yaanatech.com",
                        gitBranch: "origin/$branchName",
                        tasks: 'clean build runIT copyCoverageData copyIntegrationCoverageData mergeCoverageData generateBranchName sonarRunner',
                )
                ciJobBuilder.buildJob(this as DslFactory)
                projectFeaturesJobsList.add(buildJobName)

            }
        }
        def viewBuilder = new JenkinsViewBuilder(
                viewName: viewOrFolderName,
                regexPattern: viewOrFolderName +"/.*"
        )
        viewBuilder.buildView(this as DslFactory)
    }

}

