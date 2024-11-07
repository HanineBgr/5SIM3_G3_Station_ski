pipeline {
    agent any 
    tools {
        maven 'M2_HOME' //Maven version
        jdk 'JAVA_HOME' 
    }

    environment {
        PROJECT_NAME = "station-ski"
        GIT_REPO = "https://github.com/HanineBgr/5SIM3_G3_Station_ski"
        BRANCH_NAME = "OnsAMMAR-5SIM3-G3"
        SONAR_URL = "http://192.168.33.10:9000"
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

        stage('MOCKITO') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage ('SONARQUBE') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=223JFT4307ons! -Dmaven.test.skip=true';
            }
        }

        stage('NEXUS') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dusername=admin -Dpassword=nexus -Dmaven.test.skip=true'
            }
        }

        stage('Building Image') {
            steps {
                sh 'docker build -t onsammar/gestion-station-ski-2.0 .'
            }
        }
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

