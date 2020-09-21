# k8s-webapp-sample

## Preface
This project is a sample for running the web application on Kubernetes.
The web application consists of three layers: Web, App, DB.

For more information about this application, see:

- Web
  - [vuejs-webapp-sample](https://github.com/ybkuroki/vuejs-webapp-sample)
- App
  - [go-webapp-sample](https://github.com/ybkuroki/go-webapp-sample)

The previous source(used Spring Boot) can be found on [this branch](https://github.com/ybkuroki/k8s-webapp-sample/tree/spring_boot).

## Architecture
This sample deployed three layers to three services : webserver-k8s-service, appserver-k8s-service, dbserver-k8s-service.
There are three pods in the each service, but DB service is only one.
Different middleware and applications are deployed on each pod.
Web pods run a Vue.js application on Nginx, App pods run a golang web application. The DB pod uses PostgreSQL.

## Requirements
The following requirements for running are required:

- CentOS 7
- Docker
- Kind (Kubernetes In Docker)

## Install
Perform the following steps:

1. Install [Docker](https://www.docker.com/).
    ```bash
    ## install dependency packages
    sudo yum install -y yum-utils device-mapper-persistent-data lvm2
    ## add repository.
    sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    ## update package information
    sudo yum makecache fast
    ## install docker
    sudo yum install docker-ce
    ## add my account to the docker group
    sudo groupadd docker
    sudo usermod -aG docker $USER
    ```
1. Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/).
    ```bash
    ## add repository
    cat <<EOF > /etc/yum.repos.d/kubernetes.repo
    [kubernetes]
    name=Kubernetes
    baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
    enabled=1
    gpgcheck=1
    repo_gpgcheck=1
    gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
    EOF
    ## install kubectl
    yum install -y kubectl
    ```
1. Install [kind](https://kind.sigs.k8s.io/).
    ```bash
    ## download kind's binary files
    curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.8.1/kind-linux-amd64
    ## change authority
    chmod +x ./kind
    ## move kind's binary files
    mv ./kind /usr/local/bin/kind
    ```

## Starting this sample on Kubernetes
Perform the following steps:

1. Create a cluster.
1. Build Dockerfiles and upload docker images to kind.
1. Enable Ingress. Deploy ingress-nigix-controller.
1. Enable [Dashboard(optional)](https://github.com/kubernetes/dashboard).
1. Create deployments and services.
1. Access Ingress's URL in your browser and confirm that this application has started.

### Create a cluster
```bash
## create a cluster
kind create cluster --config k8s-cluster.yml

## check a cluster information
kubectl cluster-info

## check kube-system's pods
kubectl get pods --all-namespaces
```

### Build Dockerfiles and upload docker images to kind.
```bash
## build dockerfiles
docker build -t appserver_k8s:1.3 -f k8s/app/Dockerfile .
docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
docker build -t webserver_k8s:1.0 -f k8s/web/Dockerfile .

## upload docker images to kind
kind load docker-image appserver_k8s:1.3
kind load docker-image dbserver_k8s:1.0
kind load docker-image webserver_k8s:1.0
```

### Enable Ingress
Ingress controller doesn't deploy in the local kubernetes as a default, so we have to deploy it by oneself. In this here, we will deploy [Ingress nginx controller](https://github.com/kubernetes/ingress-nginx).
```bash
## deploy ingress controller
kubectl apply -f k8s/ext/ingress/deploy.yaml

## check that the setup of ingress have finished in the following command
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s
```

### Enable Dashboard (Optional)
We will deploy the dashboard for kubernetes so that it is easy to check the status of the pods.
```bash
## create pods and services for dashbord
kubectl apply -f k8s/ext/dashboard/recommended.yaml

## create a account for dashboard
kubectl apply -f k8s/ext/dashboard/account.yaml

## get a authentication token for login
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')

## start proxy of kubernetes
kubectl proxy

## access the following URL in Chrome and then login
```
[http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/)

### Create deployments and services
```bash
## deploy redis
kubectl apply -f k8s/redis/deployment.yaml
kubectl apply -f k8s/redis/service.yaml

## deploy db
kubectl apply -f k8s/db/statefulset.yaml
kubectl apply -f k8s/db/service.yaml

## deploy application
kubectl apply -f k8s/app/deployment.yaml
kubectl apply -f k8s/app/service.yaml

## deploy web
kubectl apply -f k8s/web/deployment.yaml
kubectl apply -f k8s/web/service.yaml

## check deployed pods
kubectl get pods

## check deployed services
kubectl get services

## check all services and pods
kubectl get all

## access the following URL in Chrome and then login
```
- URL: [http://localhost/](http://localhost/)
- User Name: ``test``
- Password: ``test``

### Access a container of a pod
```bash
## for db
kubectl exec -it dbserver-k8s-sfs-0 --container dbserver -- psql -U testusr testdb

## for application
kubectl exec -it appserver-k8s-67dbd4f8b8-z5f6v --container appserver -- /bin/bash

## for web
kubectl exec -it webserver-k8s-54b64f8dc-gdn8l --container nginx -- /bin/bash

## for redis
kubectl exec -it redis-k8s-75d7484844-hxs94 --container redis -- redis-cli
```

## Stopping this sample on Kubernetes
1. Delete deployments and services.
1. Delete a cluster.

### Delete deployments and services
```bash
# delete all services and pods
kubectl delete all --all

# or perform the following commands
# delete the pods and the service for redis
kubectl delete -f k8s/redis/deployment.yaml
kubectl delete -f k8s/redis/service.yaml

# delete the pods and the service for db
kubectl delete -f k8s/db/statefulset.yaml
kubectl delete -f k8s/db/service.yaml

# delete the pods and the service for application
kubectl delete -f k8s/app/deployment.yaml
kubectl delete -f k8s/app/service.yaml

# delete the pods and the service for web
kubectl delete -f k8s/web/deployment.yaml
kubectl delete -f k8s/web/service.yaml
```

### Commands for troubleshooting
```bash
# display summary for any pod
kubectl describe pods [pod name]
# display summary for any service
kubectl describe services [service name]

# check log of any pod
kubectl logs [pod name]
```

## Project Map
The follwing figure is the map of this sample project.

```
- k8s                   ... Define yml for Kubernetes.
  - ext                 ... Define the extension functions for Kubernetes.
  - app                 ... Define deployments for application service.
  - db                  ... Define deployments for database service.
  - lb                  ... Define deployments for ingress.
  - redis               ... Define deployments for redis.
  - web                 ... Define deployments for web service.
```

## License
The License of this sample is *MIT License*.
