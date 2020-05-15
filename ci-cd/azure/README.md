# Indice dei contenuti
1. [Login]()
1. [Inizializza il cluster kubernetes]()
1. [Installa nginx su kubernetes]()
1. [Usa volume file peristence]()
1. [Usa la volumes persistence]()
1. [Crea il timescaleDB]()
1. [Crea backend]()
1. [Crea il frontend]()

# Login (az deve essere installato separatamente)
  * az login
    
# Inizializza il cluster kubernetes (https://docs.microsoft.com/en-us/azure/aks/kubernetes-walkthrough)
  * az aks get-credentials --resource-group myResourceGroup --name myAKSCluster #configure kubectl

# Installa nginx su kubernetes (helm deve essere installato separatamente)
### 1. https://kubernetes.github.io/ingress-nginx/deploy/
  * helm install nginx stable/nginx-ingress --namespace ingress-basic --set controller.replicaCount=1  --set controller.nodeSelector."beta\.kubernetes\.io/os"=linux  --set defaultBackend.nodeSelector."beta\.kubernetes\.io/os"=linux
### 2. prendi le info dall'ingress
  * kubectl get service -l app=nginx-ingress --namespace ingress-basic
### 3. crea l'ingress con ingress.yml 
### 4. rendi la connessione sicura
  * openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out certificate.pem
  * kubectl create secret tls tls-secret --key key.pem --cert certificate.pem

# Usa volume file peristence (non raccomandato, mancano alcune funzionalità) (https://docs.microsoft.com/en-us/azure/aks/azure-files-volume)
### 1. crea lo storage account e preleva le chiavi
  * . ./create_az_volume.sh
### 2. crea il kubernetes secret (elimina se già esiste)
  * kubectl create secret generic azure-secret-vol --from-literal=azurestorageaccountname=$AKS_PERS_STORAGE_ACCOUNT_NAME --from-literal=azurestorageaccountkey=$STORAGE_KEY

# Usa la volumes persistence (https://docs.microsoft.com/en-us/azure/aks/azure-disk-volume)
  * resGroup=$(az aks show --resource-group <myResourceGroup> --name <myAKSCluster> --query nodeResourceGroup -o tsv)
  * az disk create \
  --resource-group $resGroup \
  --name <myAKSDisk> \
  --size-gb 5 \
  --query id --output tsv
  * use last command output to refer to disk

# Crea il timescaleDB
### 1. crea il kubernetes secret per la password
  * kubectl create secret generic postgres-pwd  --from-literal=password=admin
### 2. crea il timescaleVol e riferiscilo in timescale_deployment.yml
### 3. crea il db pod (usando apply)
### 4. crea il db per sonarqube (all'interno del container)
  * createdb -h localhost -U postgres thirema
### 5. abilita connessioni all'interno del cluster (all'interno del container)
  * echo "host all all 0.0.0.0/0 trust" >> /var/lib/postgresql/data/pg_hba.conf
### 6. ricarica la conf(sempre meglio che ricaricare il server completamente)
  * su - postgres
  * pg_ctl reload -D /var/lib/postgresql/data/

# Crea backend
### 0. crea container registry in azure, il login si trova in azure, sostituisci il nome della registry nei deployment. Sostituisci il nuovo indirizzo Ip esterno di nginx sui deployment e sui pom
### 0.1 modifica la CI usando il nuovo nome della registry e cambia i segreti (user, password)
### 1. crea un disk chiamato mavenDisk e riferiscilo nel deployment.yml del backend
### 2. fai push della container image (base) su azure
  * cd backend
  * docker login ThiReMaRegistry2.azurecr.io
  * az aks update -n ThiReMa -g myResourceGroup --attach-acr ThiReMaRegisty2
  * docker image build -f Dockerfile -t ubuntu-env .
  * docker container run -it --rm --env DBURL=postgres --name backend -p 1234:1234 ubuntu-env 
  * docker tag ubuntu-env ThiReMaRegistry2.azurecr.io/ubuntu-env
  * docker push ThiReMaRegistry2.azurecr.io/ubuntu-env
### 3. configura kubernetes per usare la azure registry
  * kubectl create secret docker-registry azregistry \
    --docker-server ThiReMaRegistry2.azurecr.io \
    --docker-username=ThiReMaRegistry2 \
    --docker-password Z/pVJdgFyXJfqTCuP7ebQqIrQbImAWOw
### 4. fai lo stesso per l'immagine backend e kafka impostando la giusta image registry nei dockerfiles
### 5. crea pod e services

# Crea il frontend (riusando la  backend image)
### 1. crea un disk chiamato jsDisk e riferiscilo nel deployment.yml del frontend
### 2. crea il pod e i services