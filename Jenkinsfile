
def final registryServer = "quay.io"
def final registryAccount = "zgrinber"
def final applicationName = "games-store-service"
def gitRepo = 'https://github.com/zvigrinberg/games-store-service.git'
def tag
def final mainBranch = "main"
pipeline {
    agent any
    tools {
        maven 'apache-maven'
        java  'java-jdk'
    }
    stages {
        stage('Checkout Repository') {

            steps {
               checkout scm
            }
        }
        stage('Run Unit-tests') {
          steps {
              sh 'mvn clean test'
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
                    env.EXHORT_MVN_PATH = "$maven/bin/mvn"
                    invokeRhdaAnalysis("pom.xml", "")
                }

            }
        }
        stage('Run Integration Tests') {
            steps{
                sh 'mvn clean verify -Pits'
            }

        }

        stage('Open Pull Request') {
            steps{
                withCredentials([string(credentialsId: 'gh-pat', variable: 'GH_TOKEN')]) {
                    script {

//                        def final gitBranch = env.BRANCH_NAME
                        def final gitBranch = "${env.BRANCH_NAME}"
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
                input message: 'Proceed to CD? ( Continuous Delivery) ', ok: 'Yes!', submitter: 'zgrinber'
            }
        }

        stage('Build JAR Artifact') {
            steps{
                sh 'mvn package -Dquarkus.package.jar.type=uber-jar'
                stash includes: 'target/**', name: 'builtJar'

            }
        }

        stage('Build and Push Container Image') {
            agent { label 'jenkins-agent-podman' }
            withCredentials([usernamePassword(credentialsId: 'quay-registry', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
                steps {
                    def buildNumber = "${env.BUILD_NUMBER}"
                    def baseVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    tag= "${baseVersion}-${buildNumber}"
                    script {

                        sh "podman login -u ${USER} -p ${PASSWORD} ${registryServer}"
                        sh "podman build -f src/main/docker/Dockerfile.legacy-jar -t ${registryServer}/${registryAccount}/${applicationName}:${tag} ."
                        sh "podman push ${registryServer}/${registryAccount}/${applicationName}:${tag}"

                    }
                }

            }
        }

        stage('Create Release commit and Push Tag'){
          withCredentials([string(credentialsId: 'gh-pat', variable: 'GH_TOKEN')]) {
              steps{
                  checkout scmGit(branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[url: gitRepo]])
                  script{

                      def authenticatedRemote = gitRepo.replace("https://","https://${GH_TOKEN}@")
                      sh 'git config user.name jenkins-game-store-service-ci-user'
                      sh 'git config user.email jenkins-game-store-service-ci-user@users.noreply.github.com'
                      sh "git remote set-url origin ${authenticatedRemote}"
                  }
              }
          }
        }


        stage('Clean Workspace') {
            steps{
                cleanWS
            }

        }





    }
}

private void invokeRhdaAnalysis(String manifestName,String pathToManifestDir) {
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
        withEnv(['EXHORT_DEBUG=true']) {
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