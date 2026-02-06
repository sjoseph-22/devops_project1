def call(String imgName, String tag, String user) {
    echo "Pushing Image to DockerHub..."
    // Ensure you have a credential ID 'dockerhub-creds' in Jenkins
    withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
        sh "docker push ${user}/${imgName}:${tag}"
    }
}
