def call(String imgName, String tag, String user) {
    echo "Cleaning up local Docker images..."
    sh "docker rmi ${user}/${imgName}:${tag} || true"
}