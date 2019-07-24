# Greetings Application

This is a simple demo application demonstrating a Java application's journey from development to production. 

It comprises of a Spring Boot application that exposes a single API endpoint that returns a greeting as a string. Cliche. Right? That needs to find its way to Kubernetes. Not so cliche for a Greetings app. Huh?

### Packaging to jar

```bash
$ ./gradlew build
```

Build and skip tests...

```bash
$ ./gradlew build -x test
```

Test application....
```bash
$ curl localhost:8080
```

### Packaging to Docker image using gradle plugin jib

```bash
$ ./gradlew jibDockerBuild
```

List docker images....
```bash
$ docker images
```

Running a container from the docker image....
```bash
$ docker run --name greetings-app -p 8080:8080 -d greetings
```

or with an environment variable

```bash
$ docker run --name greetings-app -p 8080:8080 --env GREETINGS_SALUTATION=Niaje! -d greetings
```

Test application....
```bash
$ curl localhost:8080
```

## To developer Kubernetes instance (minikube)

Minikube hack to build image to kubernetes 
```bash
$ eval $(minikube docker-env)
$ ./gradlew jibDockerBuild
```

Deploy app to kubernetes on developer machine
```bash
$ kubectl apply -f manifest/vanilla/greetings-application-v1.yaml
```

#### Get deployments
```bash
$ kubectl get deployments -n ns-workshop
```
NAME        READY   UP-TO-DATE   AVAILABLE   AGE

greetings   1/1     1            1           2m26s


#### Get pods
```bash
$ kubectl get pods -n ns-workshop
```
NAME                         READY   STATUS    RESTARTS   AGE

greetings-6b7c4bbf77-jnbvx   1/1     Running   0          2m36s

#### Get services
```bash
$ kubectl get services -n ns-workshop
```
NAME            TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE

greetings-app   NodePort   10.104.188.88   <none>        8080:30245/TCP   19s

#### Test deployed app
```bash
$ minikube service -n ns-workshop greetings-app
```


#### Switch to production kubernetes context
```bash
$ kubectl config get-contexts
```
CURRENT   NAME             CLUSTER          AUTHINFO         NAMESPACE

*         gke_my-lab-...   gke_my-lab-...   gke_my-lab-...

          minikube.istio   minikube.istio   minikube.istio   

```bash
$ kubectl config use-context <context-name>
```


## Helm

### What is helm?

#### Test Deploy
```bash
$ helm install ./manifest/helm-charts/greetings --debug --dry-run
```

#### Deploy
```bash
$ helm install ./manifest/helm-charts/greetings
```

NAME:   yellow-ferret
LAST DEPLOYED: Wed Jul 24 23:04:14 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Service
NAME                     TYPE       CLUSTER-IP    EXTERNAL-IP  PORT(S)   AGE
yellow-ferret-greetings  ClusterIP  10.36.12.175  <none>       8080/TCP  1s

==> v1/Deployment
NAME                     DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
yellow-ferret-greetings  1        1        1           0          1s

==> v1/Pod(related)
NAME                                      READY  STATUS             RESTARTS  AGE
yellow-ferret-greetings-547b4d5bd9-ns828  0/1    ContainerCreating  0         1s


#### Delete helm install
```bash

```

## Istio

### What is istio?

### Why istio?
Out of the box features:
* Request routing
* Fault injection
* Traffic shifting
* Querying metrics
* Visualizing metrics
* Collecting logs
* Rate limiting
* Ingress gateways
* Accessing external services
* Visualizing your mesh

### Querying metrics with Prometheus
Open Prometheus dashboard
```bash
$ kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=prometheus -o jsonpath='{.items[0].metadata.name}') 9090:9090 &
```
Go to  http://localhost:9090

#### Querying metrics from Prometheus
Execute a Prometheus query.

In the “Expression” input box at the top of the web page, enter the text: istio_requests_total. Then, click the Execute button

Total count of all requests to greetings-app service
```bash
$ istio_requests_total{destination_service="greetings-app.ns-workshop.svc.cluster.local"}
```

Total count of all requests to version 4 of greetings-app service
```bash
$ istio_requests_total{destination_service="greetings-app.ns-workshop.svc.cluster.local", destination_version="v3"}
```

### Visualize metrics with Grafana
Open Grafana dashboard
```bash
$ kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') 3000:3000 &
```
Go to http://localhost:3000/dashboard/db/istio-mesh-dashboard

Go to http://localhost:3000/dashboard/db/istio-service-dashboard

Go to http://localhost:3000/dashboard/db/istio-workload-dashboard


## Linkerd

### What is linkerd?

### Why linkerd?
Out of the box features:
* HTTP, HTTP/2, and gRPC Proxying
* TCP Proxying and Protocol Detection
*  Retries and Timeouts
*  Automatic mTLS
*  Automatic Proxy Injection
*  Service Profiles
*  Ingress
*  Telemetry and Monitoring
*  Load Balancing
*  Dashboard and Grafana


### Open Linkerd dashboard
```bash
$ linkerd dashboard
```

### Injecting proxy to nairobijvm namepsace if auto-injection is disabled
This command retrieves all of the deployments running in the nairobijvm namespace, runs the set of Kubernetes resources through inject, and finally reapplies it to the cluster.
```bash
$ kubectl get -n ns-workshop deploy -o yaml \
  | linkerd inject - \
  | kubectl apply -f -
```

### Test deployed app
```bash
$ minikube service -n ns-workshop greetings-app
$ while true; do curl http://192.168.39.9:31081/; done
```

## Cleanup
To clean up the resources you created in your cluster:

### Delete greetings service
```bash
$ kubectl delete service greetings-app -n ns-workshop
```

### Delete greetings deployment
```bash
$ kubectl delete deployment greetings -n ns-workshop
```

### Packaging to Docker image and Push to a registry using gradle plugin jib

```bash
$ ./gradlew jib
```
Pull and use [registry:v2](https://hub.docker.com/_/registry) to quickly setup a local docker image registry.