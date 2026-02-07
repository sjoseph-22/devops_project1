# CI Project using Jenkins and Docker
This document outlines the step-by-step process for deploying a java application on an AWS EC2 instance. The deployment is containerized using Docker. A full CI/CD pipeline is established using Jenkins to automate the build and deployment process whenever new code is pushed to a GitHub repository

Create AWS EC2 with these parameters:
--------
    - EC2 type: Ubuntu t2.medium
    - EBS volume: 30GB
    - Region: US-EAST-1
    
Connect to EC2 instance and install all tools as root user:
-------
    - sudo su
    
Install Jenkins:
-------
    - sudo apt update -y
    - sudo apt upgrade -y 
    - sudo apt install openjdk-17-jre -y
    - curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
    - echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
    - sudo apt-get update -y 
    - sudo apt-get install jenkins -y

<img width="959" height="419" alt="jenkins-running" src="https://github.com/user-attachments/assets/9c7ef73a-82ef-419d-b0c8-85baa63a052b" />

 
Change security group for EC2 instance:
--------------
    Security -> Security group -> Edit inbound rules
    Type: All Traffic
    Source: Anywhere-IPv4

<img width="959" height="441" alt="edit-security-group" src="https://github.com/user-attachments/assets/7c46dba2-37ef-473c-9ac0-570318c3ba07" />

  
Sign-in into Jenkins console:
-------------
    - http://<EC2_public_IP>:8080
    # Get admin password using the below command:
    - cat /var/lib/jenkins/secrets/initialAdminPassword

<img width="959" height="443" alt="jenkins-homepage" src="https://github.com/user-attachments/assets/fa1c8ef2-5bb6-4aa5-8197-c1246cd3e865" />

    
Create user and install below plugins:
-----------
    Settings -> Plugins -> Available plugins
    - Sonar Gerrit
    - SonarQube Scanner
    - SonarQube Generic Coverage
    - Sonar Quality Gates
    - Quality Gates
    - Artifactory
    - Jfrog
    
Create Pipeline:
--------
<img width="947" height="443" alt="jenkins-newitem" src="https://github.com/user-attachments/assets/92a9b4c9-fb36-415d-b8e6-061f8e5405a4" />                         
    <p>Add pipeline script as SCM and link Github repo.</p>
<img width="947" height="442" alt="add-git-pipeline" src="https://github.com/user-attachments/assets/a973639f-eaef-4c86-a1b0-3c6fd8689145" />

Setup Docker in EC2 instance:
-------

    - sudo apt update -y
    - sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
    - curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    - sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable" -y
    - sudo apt update -y 
    - apt-cache policy docker-ce -y
    - sudo apt install docker-ce -y
    - sudo systemctl status docker
    - sudo chmod 777 /var/run/docker.sock

<img width="959" height="418" alt="docker-installed" src="https://github.com/user-attachments/assets/67e757fd-1a95-4d16-9c27-e1b9ed0f50b2" />

Install SonarQube and start docker container:
-----------
    - docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube

Login into SonarQube dashboard:
-----------
    - http://<EC2_public_IP>:9000
    Username: admin
    Password: admin

<img width="947" height="398" alt="sonarqube-dashboard" src="https://github.com/user-attachments/assets/c4dbdbc2-1a73-43a2-b551-2c7057355372" />

Create Sonar token for Jenkins:
-----------
    SonarQube Dashboard -> Administration -> My Account -> Security -> Create Token (Save the token somewhere)

<img width="959" height="410" alt="sonar-jenkins-token" src="https://github.com/user-attachments/assets/d8a5612d-b86a-4569-bd0c-81a143f82997" />

Integrate Sonar with Jenkins:
-----------
    SonarQube Dashboard -> Administration -> Configuration -> Webhooks -> Add below name and url -> Save
    Name: Jenkins
    URL: http://<EC2_public_IP>:8080/sonarqube-webhook/

<img width="959" height="409" alt="sonarqube-webhook" src="https://github.com/user-attachments/assets/b95aaf12-7b21-4bb6-a738-43ce8a8f78ce" />

Install Maven in EC2 instance:
-----------
    - sudo apt update -y
    - sudo apt install maven -y
    - mvn -version

Install Trivy in EC2 instance for Docker image scan:
-----------
    - sudo apt-get install wget apt-transport-https gnupg lsb-release
    - wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo apt-key add -
    - echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | sudo tee -a /etc/apt/sources.list.d/trivy.list
    - sudo apt-get update
    - sudo apt-get install trivy

Add SonarQube in Jenkins:
-----------
    Jenkins Dashboard -> Settings -> Configure System.
    Click sonarqube servers -> add url and name -> Click add token -> Select Secret text -> Add sonar token saved earlier -> Name token as sonar-api

<img width="959" height="443" alt="sonarqube-api" src="https://github.com/user-attachments/assets/48f3ca14-54f7-4c7c-bc7a-e5fcb6032a94" />

Add DockerHub creds in Jenkins:
-----------
    # You need to create dockerhub repo
    Jenkins Dashboard -> Settings -> System -> Click Global credentials.
    # Add dockerhub creds with name as docker.

<img width="947" height="436" alt="jenkins-docker-integration" src="https://github.com/user-attachments/assets/f03e14e3-6690-46b2-8855-cce5864d6655" />

    
Add Jenkins shared library:
--------
    Manage Jenkins -> Configure System -> Global pipeline library

<img width="948" height="441" alt="git-repo-attach1" src="https://github.com/user-attachments/assets/9ceb2296-69f3-41fa-abe5-8ace36e2086f" />

<img width="946" height="442" alt="git-repo-attach2" src="https://github.com/user-attachments/assets/f37389da-bb54-4745-908d-2736423d84e7" />

Save and Build Pipeline:
--------
    # Once pipeline in run, check:
    - Jenkins log
    - Trivy scan vulnerabilites
    - Sonar dashboard for report

<img width="949" height="443" alt="jenkins-success" src="https://github.com/user-attachments/assets/4b181271-17d2-4664-a301-3cdfd5dd1dc4" />



    

