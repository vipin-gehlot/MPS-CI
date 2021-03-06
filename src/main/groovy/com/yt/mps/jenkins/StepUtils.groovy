package com.yt.mps.jenkins

class StepUtils {

    static void proxiedGradle(context, String gradleTasks) {
        context.with {
            gradle {
                useWrapper true
                tasks gradleTasks
                switches '''
                    -Dhttp.proxyHost=xxx
                    -Dhttps.proxyHost=xxx
                    -Dhttp.proxyPort=xxx
                    -Dhttps.proxyPort=xxx
                '''.stripIndent().trim()
            }
        }
    }
}