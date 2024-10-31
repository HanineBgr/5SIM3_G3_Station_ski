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
    }

    stages {
        stage('Clone') {
            steps {
                echo 'Cloning Git repository...'
                git branch: "${BRANCH_NAME}", url: "${GIT_REPO}"
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Build and deployment succeeded.'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}

