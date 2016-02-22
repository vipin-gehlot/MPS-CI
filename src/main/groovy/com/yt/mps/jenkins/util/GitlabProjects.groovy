/*
* Copyright 2011 Yaana Technologies. All rights reserved.
* Yaana Technologies PROPRIETARY/CONFIDENTIAL.
*/

package com.yt.mps.jenkins.util;

enum GitlabProjects {

    AUTH_SERVICE("54"),
    LIBCRYPTO("55"),
    MPS_CORE("56"),
    SECURE_MSG("51"),
    KMS("70"),
    SECURE_MSG_PORTAL("88");

    private String projectId;

    GitlabProjects(String projectId){
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }
}
