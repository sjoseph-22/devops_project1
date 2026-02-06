def call(String credentialsId) {
    echo "Running SonarQube Analysis..."
    // 'SonarQube' must match the name in Manage Jenkins -> System -> SonarQube Server
    withSonarQubeEnv('sonarqube-api') {
        sh "mvn sonar:sonar -Dsonar.login=${credentialsId}"
    }
}
