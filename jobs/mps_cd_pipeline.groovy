import com.esotericsoftware.yamlbeans.YamlReader
import com.yt.mps.jenkins.GradleCiJobBuilder
import com.yt.mps.jenkins.domain.JobTypes
import com.yt.mps.jenkins.domain.PipelinePhase
import com.yt.mps.jenkins.domain.ProjectPipeline


def reader = new YamlReader(new FileReader("resources/jobs-config.yaml"));
def defaultGitRepo
def defaultBranch
def defaultRepoCredentials;
def defaultBuildToolVersion;
ProjectPipeline pipeline;

while((pipeline=reader.read(ProjectPipeline.class))!=null){

    defaultGitRepo= pipeline.defaultBuildToolVersion
    defaultBranch = pipeline.repoBranch
    defaultRepoCredentials = pipeline.defaultRepoCredentials
    defaultBuildToolVersion = pipeline.defaultBuildToolVersion

    folder(pipeline.getProjectName()) {
        displayName(pipeline.projectName)
        description(pipeline.projectDesc)
    }

    pipeline.pipelinePhases.each {pipelinePhase ->
        log("Job type :  $pipelinePhase.jobType")
        def paramsMap = getJobParameters(pipelinePhase, pipeline.projectName, defaultGitRepo,defaultBranch, defaultRepoCredentials, defaultBuildToolVersion);
        switch(pipelinePhase.jobType){
            case JobTypes.GRADLE:
                createGradleJob (pipelinePhase, paramsMap)
                break;
            case JobTypes.MAVEN:
                createMavenJob(pipelinePhase, paramsMap)
                break;
            case JobTypes.DEPLOY:
                createDeployJob(pipelinePhase, paramsMap)
                break;
            case JobTypes.CUSTOM:
                break;
        }
        log("$pipeline.projectName pipeline :  Phase { ${pipelinePhase.phase.name} }, - ${paramsMap.get('jobName')} job created.")
    }
}

private void createGradleJob(PipelinePhase pipelinePhase, Map paramsMap ){

    new GradleCiJobBuilder(
            name: paramsMap.get('jobName'),
            description: paramsMap.get('jobDesc'),
            gitUrl: paramsMap.get('repoUrl'),
            gitBranch: paramsMap.get('repoBranch'),
            gitSSHCredentials: paramsMap.get('repoCredentials'),
            gradleVersion: paramsMap.get('defaultBuildToolVersion'),
            runJobOnNode: pipelinePhase.runJobOnNode,
            tasks: pipelinePhase.buildCommand,
            downStreamProject: paramsMap.get('downStreamProjects')
    ).buildJob(this)
}

private createMavenJob(PipelinePhase pipelinePhase,Map paramsMap){
    mavenJob( paramsMap.get('jobName')) {
        description(paramsMap.get('jobDesc'))
        logRotator(5, 5)
        scm {
            git {
                remote {
                    url paramsMap.get('repoUrl')
                    credentials(paramsMap.get('repoCredentials'))
                }
                branch(paramsMap.get('repoBranch'))
            }
        }
        rootPOM("pom.xml")
        goals(pipelinePhase.buildCommand)
        publishers {
            if(pipelinePhase.downStreamProjects)
                downstream(paramsMap.get('downStreamProjects'), "SUCCESS")
        }
    }
}

private createDeployJob(PipelinePhase pipelinePhase,Map paramsMap){
    new GradleCiJobBuilder(
            name: paramsMap.get('jobName'),
            description: paramsMap.get('jobDesc'),
            gitUrl: paramsMap.get('repoUrl'),
            gitBranch: paramsMap.get('repoBranch'),
            gitSSHCredentials: paramsMap.get('repoCredentials'),
            gradleVersion: paramsMap.get('defaultBuildToolVersion'),
            runJobOnNode: pipelinePhase.runJobOnNode,
            buildEnvVarFilePath: pipelinePhase.envVarFilePath,
            tasks: pipelinePhase.buildCommand,
            downStreamProject: paramsMap.get('downStreamProjects')
    ).buildJob(this)

}

private getJobParameters(PipelinePhase pipelinePhase, String projectName,
                         String defaultGitRepo,String defaultBranch,
                         String defaultRepoCredentials, String defaultBuildToolVersion) {

    def jobName = "$projectName/$projectName-${pipelinePhase.phase.name}"
    def jobDesc = (pipelinePhase.phaseDesc) ? "${pipelinePhase.phaseDesc}" : "${pipelinePhase.phase.desc}"
    def repoUrl = (pipelinePhase.gitRepo) ? "${pipelinePhase.gitRepo}" : defaultGitRepo
    def branch = (pipelinePhase.branch) ? "${pipelinePhase.branch}" : defaultBranch
    def repoCredentials = (pipelinePhase.credentials) ? pipelinePhase.credentials : defaultRepoCredentials
    def downStreamProjects = (pipelinePhase.downStreamProjects) ?
            "$projectName/$projectName-${pipelinePhase.downStreamProjects.name}" : null

    ['jobName': jobName,
     'jobDesc': jobDesc,
     'repoUrl': repoUrl,
     'repoBranch': branch,
     'repoCredentials': repoCredentials,
     'downStreamProjects':downStreamProjects,
     'defaultBuildToolVersion': defaultBuildToolVersion]
}

private getCommaSeperatedDownStreamProjects(List<String> downStreamProjects) {
    def
    downStreamProjects.each {

    }
}

void log(String msg){
    println msg;
}








