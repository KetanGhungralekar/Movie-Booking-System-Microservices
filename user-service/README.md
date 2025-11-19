// Jenkins declarative pipeline for movie-service
pipeline {
  agent any
  options { timestamps() }

  parameters {
    string(
      name: 'IMAGE_TAG',
      defaultValue: "main-${env.BUILD_NUMBER}",
      description: 'Docker image tag to use'
    )
  }

  environment {
    SERVICE_NAME = 'payment-service'
    DOCKER_REPO = 'transyltoonia'

    // macOS: Ensure Jenkins sees Docker CLI
    PATH = "/usr/local/bin:/opt/homebrew/bin:/Applications/Docker.app/Contents/Resources/bin:${env.PATH}"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Check for Changes') {
      steps {
        script {
          sh "git fetch origin main"

          def changed = sh(
            script: """
              git diff --name-only HEAD~1 HEAD | grep "${SERVICE_NAME}/" || true
            """,
            returnStdout: true
          ).trim()

          if (changed == "") {
            echo "No changes detected in ${SERVICE_NAME}. Skipping build."
            currentBuild.result = 'NOT_BUILT'
            error("Skipping build for ${SERVICE_NAME}")
          } else {
            echo "Detected changes in ${SERVICE_NAME}:"
            echo changed
          }
        }
      }
    }

    stage('Build & Test') {
      steps {
        dir("${SERVICE_NAME}") {
          sh 'chmod +x mvnw'
          sh './mvnw -B clean verify'
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: "${SERVICE_NAME}/target/surefire-reports/*.xml"
        }
      }
    }

    stage('Docker Build') {
      steps {
        script {
          def imageTag = params.IMAGE_TAG ?: "main-${env.BUILD_NUMBER}"
          env.IMAGE_NAME = "${DOCKER_REPO}/${SERVICE_NAME}:${imageTag}"
        }
        dir("${SERVICE_NAME}") {
          sh 'docker build -t "$IMAGE_NAME" .'
        }
      }
    }

    stage('Docker Push') {
      when { expression { currentBuild.currentResult == 'SUCCESS' } }
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'docker_creds',
          usernameVariable: 'DOCKER_USERNAME',
          passwordVariable: 'DOCKER_PASSWORD'
        )]) {
          sh 'echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin'
          sh 'docker push "$IMAGE_NAME"'
        }
      }
    }
  }

  post {
    success {
      echo "Built & pushed ${env.IMAGE_NAME}"
    }
    failure {
      echo "Build failed or skipped for ${env.SERVICE_NAME}"
    }
  }
}
