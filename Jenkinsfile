
def final registryServer = "quay.io"
def final registryAccount = "zgrinber"
def final applicationName = "games-store-service"
def gitRepo = 'https://github.com/zvigrinberg/games-store-service.git'
def final mainBranch = "main"
def final gitBranch
def tag
def toBuildFullImage
pipeline {
    agent any
    tools {
        maven 'apache-maven'
        jdk  'java-jdk'
    }
    environment {
        JAVA_HOME = tool name: 'java-jdk'
        M2_HOME = tool name: 'apache-maven'
        TESTCONTAINERS_RYUK_DISABLED=true
    }
    stages {
        stage('Checkout Repository') {

            steps {
               checkout scm
            }
        }
        stage('Run Unit-tests') {
          agent { label 'jenkins-agent-podman' }
          steps {
                script{
                    withEnv(['QUARKUS_MONGODB_DEVSERVICES_ENABLED=false']){
                        sh 'podman run --name mongo -d -p 27017:27017 docker.io/mongo:4.4'
                        sh 'mvn clean test'
                        sh 'podman rm -f mongo'
                    }
                }
          }
        }
        stage('Jacoco - Generate Coverage Report') {
            steps{
                recordCoverage(tools: [[parser: 'JACOCO']],
                        id: 'jacoco', name: 'JaCoCo Coverage',
                        sourceCodeRetention: 'EVERY_BUILD')
            }
        }
        stage('RHDA - Security Scanner Analysis ') {
            steps {
                script {
                    def maven = tool 'apache-maven'
                    def mavenBinary = "$maven/bin/mvn"
                    invokeRhdaAnalysis("pom.xml", "",mavenBinary)

                }

            }
        }
        stage('Run Integration Tests') {
            agent { label 'jenkins-agent-podman' }

            steps {
                script {
                    withEnv(['QUARKUS_MONGODB_DEVSERVICES_ENABLED=false']){
                        try {
                            def maven = tool 'apache-maven'
                            def mavenBinary = "$maven/bin/mvn"
                            sh 'podman run --name mongo -d -p 27017:27017 docker.io/mongo:4.4'
                            sh 'sleep 30'
                            sh "${mavenBinary} clean verify -Pits"
                            sh 'podman rm -f mongo'
                        } catch (e) {
                            echo "Infrastructural error occured while ITs, continueing : ${e}"
                        }
                    }
                }

            }
        }
        stage('Open Pull Request') {
            steps{
                withCredentials([string(credentialsId: 'gh-pat', variable: 'GH_TOKEN')]) {
                    script {

//                        def final gitBranch = ${BRANCH_NAME}
                        gitBranch = env.BRANCH_NAME
                        def final gitHubAccountOrganizationName = "zvigrinberg"
                        def final gitOpsRepoName="${JOB_NAME}".replace("-job", "")
                        def prNumber = sh(script: "curl -X POST -H \"Accept: application/vnd.github+json\" -H \"Authorization: token ${GH_TOKEN}\" https://api.github.com/repos/${gitHubAccountOrganizationName}/${gitOpsRepoName}/pulls -d '{\"title\": \"build: CI tests and scannings passed successfully for build number ${BUILD_NUMBER} \",\"body\": \"Before reviewing, please take a look on the CI job at: ${BUILD_URL}\",\"head\": \"${gitBranch}\",\"base\": \"${mainBranch}\"}' | jq .number ", returnStdout: true).trim()
                        echo "Number of PR Created : "
                        echo "${prNumber}"
                        echo "PR URL:"
                        echo "${gitRepo.replace(".git","/pulls/${prNumber}")}"
                    }
                }
            }

        }

        stage('Wait for Code Review') {
            steps {
                timeout(time: 4, unit: 'HOURS') {
                    input message: 'Proceed to CD? ( Continuous Delivery) ', ok: 'Yes!', submitter: 'zgrinber'
                }
            }
        }

        stage('Build JAR Artifact') {
            steps{
                sh 'mvn package -DskipTests=true -Dquarkus.package.jar.type=uber-jar'
                stash includes: 'target/**', name: 'builtJar'

            }
        }

        stage('Build and Push Container Image') {
            agent { label 'jenkins-agent-podman' }
            steps {
               withCredentials([usernamePassword(credentialsId: 'quay-registry', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
                  script {
                    def buildNumber = "${env.BUILD_NUMBER}"
                    def baseVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    tag= "${baseVersion}-${buildNumber}"
                    toBuildFullImage = "${registryServer}/${registryAccount}/${applicationName}:${tag}"
                    sh "podman login -u ${USER} -p ${PASSWORD} ${registryServer}"
                    sh "podman build -f src/main/docker/Dockerfile.legacy-jar -t ${toBuildFullImage} ."
                    sh "podman push ${toBuildFullImage}"

                    }
               }

            }
        }

        stage('Create Release commit and Tag'){
              steps {
                  checkout scmGit(branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[url: gitRepo]])
                  script {
                      withCredentials([string(credentialsId: 'gh-pat', variable: 'GH_TOKEN')]) {
                          def authenticatedRemote = gitRepo.replace("https://", "https://${GH_TOKEN}@")
                          sh 'git config user.name jenkins-game-store-service-ci-user'
                          sh 'git config user.email jenkins-game-store-service-ci-user@users.noreply.github.com'
                          sh "git remote set-url origin ${authenticatedRemote}"
                          def deploymentYaml = readYaml file: 'deploy/deployment.yaml'
                          // Update image name to the new built image name
                          deploymentYaml.spec.template.spec.containers[0].image = "${toBuildFullImage}"
                          writeYaml charset: '', data: deploymentYaml, file: 'deploy/deployment.yaml'
                          def commitMessage = "build: prepare release ${tag}"
                          sh 'git add deploy/deployment.yaml'
                          sh "git commit -m ${commitMessage}"
                          sh 'git push'
                          // create tag
                          sh "git tag -a ${tag}"
                          // push tag to remote repository
                          sh "git push origin ${tag}"

                      }
                  }
              }

        }

        stage('Clean Workspace') {
            steps{
                cleanWs()
            }
        }

    }
}

private GString forwardDockerDaemonToPodmanSocket() {
//    systemctl --user enable podman.socket  --now
    def podmanSocket = sh(script:  "podman info --format '{{ .Host.RemoteSocket.Path }}' || true", returnStdout: true).trim()
    return "unix://${podmanSocket}"
}

private void invokeRhdaAnalysis(String manifestName,String pathToManifestDir,String mavenBinary) {
    final VULNERABLE_RETURN_CODE = "2"
    final GENERAL_ERROR_RETURN_CODE = "1"
    def theFile
    if(pathToManifestDir.trim().equals("")) {
        theFile = "${WORKSPACE}/${manifestName}"
    }
    else {
        theFile = "${WORKSPACE}/${pathToManifestDir}/${manifestName}"
        echo "theFile=${theFile}"
    }
    try {
        def result
        withEnv(['EXHORT_DEBUG=true', "EXHORT_MVN_PATH=${mavenBinary}"]) {
            result = rhdaAnalysis consentTelemetry: true, file: theFile
        }
        if (result.trim().equals(VULNERABLE_RETURN_CODE)) {
            unstable(message: "There are some vulnerabilities in Manifest, please take a look at the report , and upgrade according to it")
        }
        else {
            if(result.trim().equals(GENERAL_ERROR_RETURN_CODE)) {
                error(message: "Failed To Invoke RHDA Analysis")
            }
        }
    }
    catch (Exception e) {
        error(message: "Intercepted Error --> ${e}")
    }
}