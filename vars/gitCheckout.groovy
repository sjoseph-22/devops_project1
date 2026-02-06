def call(Map config) {
    git branch: config.branch, url: config.url
}