package com.yt.mps.jenkins

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.views.ListView

class JenkinsViewBuilder {
    String viewName
    String jobName
    List<String> multipleJobs;
    String regexPattern

    ListView buildView(DslFactory dslFactory) {
        dslFactory.listView(viewName) {
            description("View for " + viewName)
            jobs {
                if(jobName){
                    name(jobName)
                }

                if(multipleJobs){
                    multipleJobs.each {job ->
                        name(job)
                    }
                }

                if(regexPattern){
                    regex(regexPattern)
                }
                recurse()
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }
    }
}