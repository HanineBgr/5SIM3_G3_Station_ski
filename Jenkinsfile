pipeline {
    agent any 

    tools {
        maven 'M2_HOME' //Maven version
        jdk 'JAVA_HOME' 
    }

    environment {
        PROJECT_NAME = "station-ski"
        GIT_REPO = "https://github.com/HanineBgr/5SIM3_G3_Station_ski"
        BRANCH_NAME = "main"
        SONAR_URL = "http://192.168.33.10:9000"
        //SONAR_TOKEN = credentials('squ_6452bf5c8aa2dee5d4d2186e46019d9a788362fc')
    }

    stages {
        stage('GIT') {
            steps {
                echo 'Getting project from git...'
                git branch: "${BRANCH_NAME}", url: "${GIT_REPO}"
            }
        }
        
        stage('MVN CLEAN') {
            steps {
                echo 'Running Maven clean...'
                sh 'mvn clean'
            }
        }

        stage('MVN COMPILE') {
            steps {
                echo 'Running Maven compile...'
                sh 'mvn compile'
            }
        }
        
        stage ('SonarQube') {
            steps {
            sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=223JFT4307ons! -Dmaven.test.skip=true';
            }
        }
        
        /*stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') { // Ensure Jenkins SonarQube configuration matches this name
                    sh 'mvn sonar:sonar -Dsonar.password=223JFT4307ons! -Dsonar.host.url=${SONAR_URL} -Dsonar.login=admin -Dmaven.test.skip=true'
                }
            }
        }*/
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'SUCCESS.'
        }
        failure {
            echo 'FAIL.'
        }
    }
}

