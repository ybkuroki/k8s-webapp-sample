### docker build
```bash
docker build -t appserver_k8s:1.2 -f k8s/app/Dockerfile .
docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
docker build -t webserver_k8s:1.0 -f k8s/web/Dockerfile .
```

### create configmap for nginx
```bash
kubectl create configmap nginx-conf --from-file=k8s/web/nginx.conf 
kubectl create configmap server-conf --from-file=k8s/web/server.conf 
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
```
