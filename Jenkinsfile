pipeline {
    agent any

    triggers { githubPush() }   // Webhook

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    def changedFiles = sh(
                        script: "git diff --name-only HEAD~1 HEAD || true",
                        returnStdout: true
                    ).trim().split("\n")

                    echo "Changed files:\n${changedFiles}"

                    // Map folder → Jenkins job names
                    def serviceMap = [
                        "api-gateway"       : "api-gateway-pipeline",
                        "user-service"      : "User-Service-Pipeline",
                        "movie-service"     : "movie-service-pipeline",
                        "booking-service"   : "booking-service-pipeline",
                        "payment-service"   : "Payment-Service-Pipeline",
                        "notification-service" : "Notification-Service-Pipeline"
                    ]

                    def infraFolders = ["redis", "rabbitmq", "elk", "ingress", "user-db", "movie-db", "booking-db","user-db-using-zalando"]

                    env.MICROSERVICES_TO_BUILD = ""
                    env.INFRA_CHANGED = "false"

                    // Detect changed folders
                    changedFiles.each { file ->
                        serviceMap.each { folder, job ->
                            if (file.startsWith(folder + "/")) {
                                env.MICROSERVICES_TO_BUILD =
                                    env.MICROSERVICES_TO_BUILD ?
                                        env.MICROSERVICES_TO_BUILD + "," + job :
                                        job
                            }
                        }
                        infraFolders.each { folder ->
                            if (file.startsWith(folder + "/")) {
                                env.INFRA_CHANGED = "true"
                            }
                        }
                    }

                    echo "Microservices to build: ${env.MICROSERVICES_TO_BUILD}"
                    echo "Infra changed: ${env.INFRA_CHANGED}"
                }
            }
        }

        stage('Apply Infra Changes') {
            when { expression { env.INFRA_CHANGED == "true" } }
            steps {
                script {
                    echo "Applying infrastructure updates..."

                    sh """
                        kubectl apply -f redis/
                        kubectl apply -f rabbitmq/
                        kubectl apply -f elk/
                        kubectl apply -f ingress/
                        kubectl apply -f user-db/
                        kubectl apply -f movie-db/
                        kubectl apply -f booking-db/
                        kubectl apply -f user-db-using-zalando/
                    """

                    echo "Infra updated successfully"
                }
            }
        }

        stage('Build Microservices in Parallel') {
            when { expression { env.MICROSERVICES_TO_BUILD?.trim() } }
            steps {
                script {
                    def jobs = env.MICROSERVICES_TO_BUILD.split(",")
                    def parallelBuilds = [:]

                    jobs.each { jobName ->
                        parallelBuilds[jobName] = {
                            build job: jobName, wait: true
                        }
                    }

                    parallel parallelBuilds
                }
            }
        }

        stage("Nothing Changed") {
            when {
                expression {
                    !env.MICROSERVICES_TO_BUILD?.trim() && env.INFRA_CHANGED == "false"
                }
            }
            steps {
                echo "No changes detected. Pipeline exiting."
            }
        }
    }

    post {
        success { echo "Master build complete." }
        failure { echo "Some component failed." }
    }
}
