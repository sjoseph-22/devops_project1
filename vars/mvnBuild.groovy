def call() {
    echo "Building Artifact..."
    sh 'mvn clean package -DskipTests'
}