## k8s-test-sample

### start minikube
```bash
# start minikube
minikube start --vm-driver=virtualbox

# check that minikube is runnning
minikube status

# check kube-system's pods
kubectl get pods --all-namespaces
```

### build docker images
```bash
# check docker-env. set docker environment variable.
minikube docker-env

# build docker images
docker build -t appserver_k8s:1.2 -f k8s/app/Dockerfile .
docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
docker build -t webserver_k8s:1.0 -f k8s/web/Dockerfile .
```

### create deployments and services
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

# check LoadBalancer's URL
minikube service nginx-lb-k8s --url
```

### how to access a container of a pod

```bash
kubectl exec -it webserver-k8s-54b64f8dc-gdn8l --container nginx -- /bin/bash
```
