// def call() {
//     echo "Running Unit Tests..."
//     sh 'mvn test'
// }

def call() {
    echo "Skipping Tests to bypass K8s dependency..."
    sh 'mvn test -DskipTests' 
}
