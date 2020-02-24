# k8s-webapp-sample

## Preface
TBD

## Architecture
TBD

## Install
### Requirements
TBD

## Starting Kubernetes
### Start minikube
```bash
# start minikube
minikube start --vm-driver=virtualbox

# check that minikube is runnning
minikube status

# check kube-system's pods
kubectl get pods --all-namespaces
```

### Create docker images
```bash
# check docker-env. set docker environment variable.
minikube docker-env

# build docker images
docker build -t appserver_k8s:1.2 -f k8s/app/Dockerfile .
docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
docker build -t webserver_k8s:1.0 -f k8s/web/Dockerfile .
```

### Create deployments and services
```bash
# redis
kubectl apply -f k8s/redis/deployment.yaml
kubectl apply -f k8s/redis/service.yaml

# db
kubectl apply -f k8s/db/statefulset.yaml
kubectl apply -f k8s/db/service.yaml

# apserver (For Local environment)
kubectl apply -f k8s/app/deployment.yaml
kubectl apply -f k8s/app/service.yaml

# web
kubectl apply -f k8s/web/deployment.yaml
kubectl apply -f k8s/web/service.yaml

# LB
kubectl apply -f k8s/lb/loadbalancer.yaml

# check deployed pods
kubectl get pods

# check deployed services
kubectl get services

# check all services and pods
kubectl get all

# check LoadBalancer's URL
minikube service nginx-lb-k8s --url
```

### Access a container of a pod
```bash
# db server
kubectl exec -it dbserver-k8s-sfs-0 --container dbserver -- psql -U testusr testdb

# app server
kubectl exec -it appserver-k8s-67dbd4f8b8-z5f6v --container appserver -- /bin/bash

# web server
kubectl exec -it webserver-k8s-54b64f8dc-gdn8l --container nginx -- /bin/bash
```

### Delete deployments and services
```bash
# delete all services and pods
kubectl delete all --all

# or perform the following commands
# redis
kubectl delete -f k8s/redis/deployment.yaml
kubectl delete -f k8s/redis/service.yaml

# db
kubectl delete -f k8s/db/statefulset.yaml
kubectl delete -f k8s/db/service.yaml

# apserver
kubectl delete -f k8s/app/deployment.yaml
kubectl delete -f k8s/app/service.yaml

# web
kubectl delete -f k8s/web/deployment.yaml
kubectl delete -f k8s/web/service.yaml

# LB
kubectl delete -f k8s/lb/loadbalancer.yaml
```

### Commands for troubleshooting
```bash
# summary for any pod
kubectl describe pods [pod name]
# summary for any service
kubectl describe services [service name]

# check log of any pod
kubectl logs [pod name]
```

### Stop minikube
```bash
minikube stop
```

### Delete minikube cluster
```bash
minikube delete
```

## Project Mapping
TBD

## License
TBD
