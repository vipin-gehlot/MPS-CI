package com.yt.mps.jenkins.domain;

import com.yt.mps.jenkins.PipeLineOptions;

import java.util.List;

public class PipelinePhase {

    private PipeLineOptions phase;
    private JobTypes jobType;
    private String runJobOnNode;
    private String phaseDesc;
    private String gitRepo;
    private String branch;
    private String credentials;
    private String envVarFilePath;
    private String buildCommand;
    private List<String> downStreamProjects;

    public JobTypes getJobType() {
        return jobType;
    }

    public void setJobType(JobTypes jobType) {
        this.jobType = jobType;
    }

    public String getRunJobOnNode() {
        return runJobOnNode;
    }

    public void setRunJobOnNode(String runJobOnNode) {
        this.runJobOnNode = runJobOnNode;
    }

    public PipeLineOptions getPhase() {
        return phase;
    }

    public void setPhase(PipeLineOptions phase) {
        this.phase = phase;
    }

    public String getPhaseDesc() {
        return phaseDesc;
    }

    public void setPhaseDesc(String phaseDesc) {
        this.phaseDesc = phaseDesc;
    }

    public String getGitRepo() {
        return gitRepo;
    }

    public void setGitRepo(String gitRepo) {
        this.gitRepo = gitRepo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getEnvVarFilePath() {
        return envVarFilePath;
    }

    public void setEnvVarFilePath(String envVarFilePath) {
        this.envVarFilePath = envVarFilePath;
    }

    public String getBuildCommand() {
        return buildCommand;
    }

    public void setBuildCommand(String buildCommand) {
        this.buildCommand = buildCommand;
    }

    public List<String> getDownStreamProjects() {
        return downStreamProjects;
    }

    public void setDownStreamProjects(List<String> downStreamProjects) {
        this.downStreamProjects = downStreamProjects;
    }
}
