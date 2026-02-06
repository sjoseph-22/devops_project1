def call(String imgName, String tag, String user) {
    echo "Scanning Image with Trivy..."
    // This assumes trivy is installed on the Jenkins server
    sh "trivy image ${user}/${imgName}:${tag}"
}