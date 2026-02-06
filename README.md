# CI/CD Project using Jenkins and Docker
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

 
Change security group for EC2 instance
--------------
    Security -> Security group -> Edit inbound rules
    Type: All Traffic
    Source: Anywhere for IPv4
  
Docker login
-------------
    docker login
    
Push docker image to dockerhub
-----------
    docker push vikashashoke/kubernetes-configmap-reload
    
Deploy Spring Application:
--------
    kubectl apply -f kubernetes-configmap.yml
    
Check Deployments, Pods and Services:
-------

    kubectl get deploy
    kubectl get pods
    kubectl get svc
    
Now Goto Loadbalancer and check whether service comes Inservice or not, If it comes Inservice copy DNS Name of Loadbalancer and Give in WebUI

    http://a70a89c22e06f49f3ba2b3270e974e29-1311314938.us-west-2.elb.amazonaws.com:8080/home/data
    
![2](https://user-images.githubusercontent.com/63221837/82123471-44f5f300-97b7-11ea-9d10-438cf9cc98a0.png)

Now we can cleanup by using below commands:
--------
    kubectl delete deploy kubernetes-configmap-reload
    kubectl delete svc kubernetes-configmap-reload
# springboot_k8s_application
# mrdevops_java_app
