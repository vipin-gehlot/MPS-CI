package com.yt.mps.jenkins.rest

import groovy.json.JsonSlurper

import static com.yt.mps.jenkins.util.GitlabConstants.PRIVATE_TOKEN
import static com.yt.mps.jenkins.util.GitlabConstants.PROJECTS_BASE_URL
import static com.yt.mps.jenkins.util.GitlabConstants.NAME
import static com.yt.mps.jenkins.util.GitlabConstants.SSH_URL_TO_REPO

class GitlabRestClient {

    static List<Object> getBranches(String projectId) {
        final String gitlabBranchesURL = PROJECTS_BASE_URL + projectId + "/repository/branches?private_token=" + PRIVATE_TOKEN
        final String branchesAsJson = new URL(gitlabBranchesURL).text
        def branches = new JsonSlurper().parseText(branchesAsJson)
        branches as List<Object>
    }

    static String getName(Object projectOrBranch) {
        String branchName = projectOrBranch[NAME].toString();
        branchName
    }

    static Object getProject(String projectId) {
        final String gitProjectsGitApiURL = PROJECTS_BASE_URL + projectId + "?private_token=" + PRIVATE_TOKEN
        final String projectAsJson = new URL(gitProjectsGitApiURL).text
        def gitProject = new JsonSlurper().parseText(projectAsJson)
        gitProject
    }

    static String getSshUrlToGitRepo(Object project) {
        final sshUrlToGitRepo = project[SSH_URL_TO_REPO].toString()
        sshUrlToGitRepo
    }
}
