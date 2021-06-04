/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
import de.nycode.rabbitkt.gradle.configureMavenCentralMetadata
import de.nycode.rabbitkt.gradle.configureVersion
import de.nycode.rabbitkt.gradle.signPublicationIfKeyPresent

plugins {
    `java-platform`
    `maven-publish`
    signing
}

val me = project

javaPlatform.allowDependencies()
rootProject.subprojects {
    if (name != me.name) {
        me.evaluationDependsOn(path)
    }
}

dependencies {
    constraints {
        rootProject.subprojects.forEach {
            if (it.plugins.hasPlugin("maven-publish") && it.name != name) {
                it.publishing.publications.all {
                    if (this is MavenPublication) {
                        if (!artifactId.endsWith("-metadata") &&
                            !artifactId.endsWith("-kotlinMultiplatform")
                        ) {
                            api(groupId, artifactId, version)
                        }
                    }
                }
            }
        }
    }
}

val branch = getGitBranch()

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("javaPlatform"))
            configureVersion(project, branch)
            pom {
                configureMavenCentralMetadata(project)
            }
            signPublicationIfKeyPresent(project)
        }
    }
}