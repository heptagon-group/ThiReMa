# Indice dei contenuti
1. [Docker]()
1. [Uso di minikube]()
1. [Uso di kubectl]()
1. [Uso di microk8s]()
1. [Comandi utili]()

# Docker
### fai il build dell'immagine
  * docker image build -f Dockerfile -t 127.0.0.1:32000/kafka:registry .

### crea il container interattivamente
  * docker container run -it --rm -p 9092:9092 -p 2181:2181 --name ks -h ks kafka-server:1.0 

### ispeziona elemento
  * docker inspect {objectID}

### lista qualche risorsa
  * docker {image,container,volume..} ls

### elimina risorsa
  * docker remove {image,container,volume..} {res ID}

----------------------------------------------------------

# Uso di minikube (https://auth0.com/blog/kubernetes-tutorial-step-by-step-introduction-to-basic-concepts/)
### inizializza il cluster(single node) con minikube
  * minikube start  

### prendi l'url dal cluster
  * minikube service hello-minikube --url

### inizializza con il docker di minikube
  * minikube docker-env     
  lancia l'ultima riga (eval $(minikube docker-env)) 

### dashboard
  * minikube dashboard

### stop minikube
  * minikube stop

----------------------------------------------------------

# Uso di kubectl

### namespaces
  * kubectl get all --all-namespaces  

### nodes
  * kubectl get nodes

### pods
  * kubectl get pods [-n pod's name]

### services
  * kubectl apply -f service.yaml

### ingress
  * kubectl get ingress

### creazione pod
  * kubectl create deployment hello-minikube --image=k8s.gcr.io/echoserver:1.10

### esponi come service
  * kubectl expose deployment hello-minikube --type=NodePort --port=8080

### fai deploy da file yaml 
  * kubectl apply -f deployment.yaml

### installa l'ingress controller nel cluster (nginx) (https://kubernetes.github.io/ingress-nginx/deploy/)
  * kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml
minikube addons enable ingress

### crea il service da yaml
  * kubectl apply -f service.yaml

### crea l'ingress da yaml
  * kubectl apply -f ingress.yaml

-------------------------------------------------------------------

# Uso di microk8s (https://microk8s.io/docs/)

### installazione
  * sudo snap install microk8s --classic --channel=1.17/stable

### start/stop
  1. snap enable microk8s
  1. snap disable microk8s
  1. microk8s.start
  1. microk8s.stop

### abilitazione add-ons
  * microk8s.enable dns ingress registry storage 

### inspection
  * microk8s.inspect  

### configura la registry del cluster
  1. add to /etc/docker/daemon.json and restart docker:
{
  "insecure-registries" : ["127.0.0.1:32000"]
}
  1. change /var/snap/microk8s/current/args/containerd-template.toml:
[plugins.cri.registry.mirrors."127.0.0.1:32000"]
  endpoint = ["http://127.0.0.1:32000"]

### fai il push dell'image nella cluster registry
  1. docker build . -t 127.0.0.1:32000/myimage:registry
  1. docker push 127.0.0.1:32000/myimage

### mostra le info delle images 
  * microk8s.ctr image ls

----------------------------------------------------------

# Comandi utili

### estrai il nome del kafka-server pod 
  * podname=$(kubectl get pods | grep -o "kafka-server-[a-z0-9]*-[a-z0-9]*")

### logs dal pod
  * kubectl logs $podname

### lancia bash da un container 
  * kubectl exec -it $podname -- /bin/bash 