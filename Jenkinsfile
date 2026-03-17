pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn test --no-transfer-progress'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        success {
            echo "Build ${env.BUILD_NUMBER} of ${env.JOB_NAME} succeeded."
        }
        failure {
            echo "Build ${env.BUILD_NUMBER} of ${env.JOB_NAME} failed."
        }
        unstable {
            echo "Build ${env.BUILD_NUMBER} is unstable — some tests failed."
        }
    }
}
