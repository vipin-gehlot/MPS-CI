package com.yt.mps.jenkins.domain;

import java.util.List;

public class ProjectPipeline {

    private String projectName;
    private String projectDesc;
    private String gitRepo;
    private String repoBranch;
    private String defaultRepoCredentials;
    private String defaultBuildToolVersion;
    private List<PipelinePhase> pipelinePhases;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getGitRepo() {
        return gitRepo;
    }

    public void setGitRepo(String gitRepo) {
        this.gitRepo = gitRepo;
    }

    public String getRepoBranch() {
        return repoBranch;
    }

    public void setRepoBranch(String repoBranch) {
        this.repoBranch = repoBranch;
    }

    public String getDefaultRepoCredentials() {
        return defaultRepoCredentials;
    }

    public void setDefaultRepoCredentials(String defaultRepoCredentials) {
        this.defaultRepoCredentials = defaultRepoCredentials;
    }

    public String getDefaultBuildToolVersion() {
        return defaultBuildToolVersion;
    }

    public void setDefaultBuildToolVersion(String defaultBuildToolVersion) {
        this.defaultBuildToolVersion = defaultBuildToolVersion;
    }

    public List<PipelinePhase> getPipelinePhases() {
        return pipelinePhases;
    }

    public void setPipelinePhases(List<PipelinePhase> pipelinePhases) {
        this.pipelinePhases = pipelinePhases;
    }
}
