def call() {
    echo "Running Integration Tests..."
    sh 'mvn verify -DskipUnitTests'
}