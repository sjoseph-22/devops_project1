def call(String imgName, String tag, String user) {
    echo "Building Docker Image: ${user}/${imgName}:${tag}"
    sh "docker build -t ${user}/${imgName}:${tag} ."
}