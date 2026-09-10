pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Thilak0402/e-challan-system.git'
            }
        }
        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }
        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}