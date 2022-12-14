# DevOps et Sécurité

## Groupe

Clément Bourdonnec, Melaine Le Bris, Benjamin Bouvier, Nicolas Ronceray

## Objectif

L’objectif de ce TP est de mettre en place un aspect DevOps sur le projet Doodle disponible à l’adresse suivante : https://github.com/selabs-ur1/doodle. Dans notre cas, nous appliquons l’aspect DevOps et sécurité.

# Kubernetes

La première partie du TP a été de lancer l’application Doodle sur Kubernetes. Kubernetes est un système qui permet d’automatiser le déploiement, la montée en charge et la mise en œuvre de conteneurs d'application. On parle alors d’un *cluster* Kubernetes qui contient des *nodes*. Les *nodes* sont en fait des machines qui hébergent les containers docker. Et dans chaque node, on pourra retrouver un ou plusieurs *pods* qui contiendra nos containers.

### **Installation**

Prérequis:
- [Kubernetes](https://kubernetes.io/releases/download/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/) (Necessaire si vous voulez faire tourner Kubernetes locallement)

Tout d’abord, si vous voulez faire tourner les pods Kubernetes en local, il faut lancer minikube  :
```
minikube start
```
*Si une image de Minikube existe déjà, celle-ci sera récupérée à nouveau et relancée.*

Avant d’initialiser Kubernetes, nous avons besoin des images des containers que nous voulons déployer sur Kubernetes. Dans notre cas de Doodle, nous avons déjà un fichier docker-compose qu’on peut utiliser pour créer nos images:
```
docker compose create
```

Ensuite, pour créer nos déploiements Kubernetes, nous devons assigner un déploiement par image. Dans notre cas, nous avons 3 services à déployer. Nous devons donc utiliser les commandes suivantes :
```
kubectl create deploy db --image=mysql --allow-missing-template-keys=true
kubectl set env deploy db MYSQL_ROOT_PASSWORD=root
kubectl create deploy etherpad --image=etherpad/etherpad
kubectl create deploy mail --image=bytemark/smtp
```
*La deuxième commande est nécessaire car la base de donnée à besoin d’avoir un mot de passe “root” pour être correctement initialisé.*

### **Résultat**

Toutes les images sont maintenant déployées sur Kubernetes. Vous pouvez le vérifier en utilisant la commande :
```
kubectl get deploy
```

Vous devez obtenir un résultat qui ressemble à ce qui suit :
``` console
 $ kubectl get deploy
NAME       READY   UP-TO-DATE   AVAILABLE   AGE
db         1/1     1            1           22s
etherpad   1/1     1            1           21s
mail       1/1     1            1           20s
```

En réalité, nous avons Minikube qui permet d'exécuter un cluster Kubernetes avec un seul *node* dans une machine virtuelle de notre machine locale :
``` console
 $ kubectl get node
NAME       STATUS   ROLES           AGE   VERSION
minikube   Ready    control-plane   56d   v1.25.2
```

Dans ce node nous avons alors 3 *pods* :
``` console
 $ kubectl get pods
NAME                        READY   STATUS    RESTARTS   AGE
db-5668767bb-dvt5n          1/1     Running   0          17m
etherpad-85cd757b58-pnm22   1/1     Running   0          17m
mail-5bf6696d74-rgvlx       1/1     Running   0          17m
```


# Vault

Nous passons ensuite à Vault qui est un manageur de secret. 
Il permet donc de mieux gérer les identités afin de mieux sécuriser les authentifications / autorisations sur un système. 


Pour cela, Vault va centraliser les secrets, les encrypte que ce soit au repos ou en transit. Donc même si les personnes savent où sont les secrets, maintenant ils ne sont plus aussi simple d’accès ou écrits en clair dans le code.

Vault va aussi permettre de sécuriser l’accès au code source, par exemple on pourrait dire que pour accéder au web serveur il nous faut le token de l’api.
De plus, il existe un audit de suivi qui permet de répertorier tous les accès ce qui permet un meilleur contrôle et une meilleure visibilité.

Vault peut aussi rendre dynamique les secrets, ce qui permet à chaque client d’avoir sa propre façon de s'identifier et ceux pour une courte durée. Cela permet d'ailleurs de révoquer les droits plus facilement. Par exemple, si vous avez 50 serveurs qui doivent s'identifier avec des secrets dynamiques, si le secret de l’un est compromis, ce ne sera que pour une période. On peut donc voir plus facilement quelle machine a été compromise et lui révoquer son secret sans révoquer celui des autres.

Afin d’installer Vault nous avons suivi la documentation de [Vault](https://developer.hashicorp.com/vault/tutorials/kubernetes/kubernetes-raft-deployment-guide) pour le déployer sur Kubernetes.

# Inspec

Dans cette partie nous allons installer et mettre en place quelques vérifications à l'aide de l'outil chef Inspec.

Inspec est un outil permettant de vérifier que la configuration de l'application sur laquelle on travaille répond bien aux spécifications attendues. Pour cela nous allons créer des scripts ruby qui seront utilisés par Inspec pour vérifier la configuration de l'application.

## **Installation**

Tout d'abord, il faut installer ruby sur votre machine si ce n'est pas déjà le cas. Ce tutoriel étant réalisé sur Ubuntu 22.04, certaines commandes peuvent différer selon votre OS. Pour installer ruby, nous allons exécuter la commande suivante :

```sh
apt-get install ruby ruby-dev gcc g++ make
```

Ensuite nous devons installer Inspec. Pour cela nous allons installer son package en utilisant la commande suivante fournie par la documentation de Chef Inspec :

```sh
curl https://omnitruck.chef.io/install.sh | sudo bash -s -- -P inspec
```

Vous pouvez vérifier l'installation avec la commande suivante, qui vous donnera la version :

```sh
inspec -v
```

## **Exemple d'utilisation**

Pour la suite de cette partie, nous nous placerons dans le dossier "/doodle/api"

### **Vérification d'un fichier local**

Pour le projet Doodle, nous utilisons une image docker d'Etherpad. Il est possible qu'en fonction des besoins, nous souhaitons utiliser une version précise d'Etherpad ou bien que nous souhaitons toujours avoir la dernière. Pour vérifier cela, nous allons utiliser un script ruby qui va vérifier que notre docker-compose.yml récupère bien la bonne version. Le script, etherpad_version_test.rb, est le suivant :

```rb
control "etherpad-version-test" do
    title "Test the Etherpad Docker image version in docker-compose.yaml"
    describe file('docker-compose.yaml') do
	 its('content') { should match 'etherpad/etherpad:latest'}
    end
end
```

Ce script va vérifier que le fichier "docker-compose.yaml" contient bien la chaîne de caractère "etherpad/etherpad:latest".

Pour tester le script, il faut utiliser la commande suivante :

```sh
inspec exec etherpad_version_test.rb
```

Normalement vous devriez avoir ceci :

```sh
Profile:   tests from etherpad_version_test.rb (tests from etherpad_version_test.rb)
Version:   (not specified)
Target:	local://
Target ID: dafc7a1c-581e-580e-b360-03d91b5824e5

  ✔  etherpad-version-test: Test the Etherpad Docker image version in docker-compose.yaml
 	✔  File docker-compose.yaml content is expected to match "etherpad/etherpad:latest"


Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 1 successful, 0 failures, 0 skipped

```
### **Vérification d'un container lancé**

Nous avons ici testé que le docker-compose.yaml sélectionne la bonne image mais maintenant nous voulons tester le container lancé sur notre pc.


Démarrez les conteneurs avec docker compose :

```sh
docker-compose up -d
```

Puis, il faut bien vérifier le nom de votre container avec la commande :

```
docker ps
```

```sh
CONTAINER ID   IMAGE                  	COMMAND              	CREATED   	STATUS               	PORTS                                              	NAMES
2dfc027afb13   etherpad/etherpad:latest   "docker-entrypoint.s…"   13 days ago   Up 2 minutes (healthy)   0.0.0.0:9001->9001/tcp, :::9001->9001/tcp          	api_etherpad_1
51e5058734de   mysql                  	"docker-entrypoint.s…"   13 days ago   Up 2 minutes         	0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   api_db_1

```

Ici, le nom du container est 'api_etherpad_1'

Ensuite nous créons un script etherpad_container_test.rb qui est le suivant :

```rb
control "etherpad-container-test" do
    title "Test the running Etherpad  docker container"
  describe docker_container('api_etherpad_1') do
	it { should exist }
	it { should be_running }
	its('id') { should_not eq '' }
	its('tag') { should eq 'latest' }
	its('ports') { should match '0.0.0.0:9001->9001/tcp' }
  end
end
```
Ce script va vérifier que le container existe et qu'il est lancé, que son id n'est pas nul, que son tag est "latest" et que ses ports d'entrée, de sortie sont corrects et qu'ils utilisent le protocole tcp.

Maintenant exécutez la commande suivante :

```rb
sudo inspec exec etherpad_container_test.rb
```

Normalement vous devriez avoir ce résultat :

```sh
Profile:   tests from etherpad_container_test.rb (tests from etherpad_container_test.rb)
Version:   (not specified)
Target:	local://
Target ID: dafc7a1c-581e-580e-b360-03d91b5824e5

  ✔  etherpad-container-test: Test the running Etherpad  docker container
 	✔  Docker Container api_etherpad_1 is expected to exist
 	✔  Docker Container api_etherpad_1 is expected to be running
 	✔  Docker Container api_etherpad_1 id is expected not to eq ""
 	✔  Docker Container api_etherpad_1 tag is expected to eq "latest"
 	✔  Docker Container api_etherpad_1 ports is expected to match "0.0.0.0:9001->9001/tcp"


Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 5 successful, 0 failures, 0 skipped
```

Nous savons maintenant vérifier le contenu d'un fichier ainsi que la vérification d'un conteneur lancé avec Inspec.
Les exemples vus sont basiques mais on peut voir l'utilité de cet outil. Il est donc possible de vérifier l'installation d'application en suivant soit des spécifications requises pour le projet ou vérifier le respect de standard de sécurité comme par exemple ceux de Owasp.


## **Références pour Inspec**

https://github.com/inspec/inspec

https://docs.chef.io/inspec/

# Clair

Clair est outil d’analyse statique qui permet de trouver les vulnérabilités des conteneurs d’application en parsant l’image et les différentes couches du système de fichiers. Pour cela, il va matcher le conteneur avec des vulnérabilités connues. Cette analyse ne se fait pas pendant que le conteneur est en fonctionnement mais elle se fait sur l’image du conteneur. Cela signifie que si le conteneur en route est compromis et que de nouvelles vulnérabilités apparaissent alors Clair ne pourra pas les détecter.

## **Clair-scanner**

Afin de faciliter l’utilisation de Clair, nous avons utilisé clair-scanner qui utilise Clair mais qui est plus simple d’utilisation.
https://github.com/arminc/clair-scanner

## **Pré-requis**

Il faut télécharger le binary depuis les releases de clair-scanner puis de le rendre exécutable sur votre machine.
Il est également nécessaire d’avoir Docker d’installé sur votre machine.

## **Lancement**

Il faut tout d’abord démarrer clair : 

```
docker run -p 5432:5432 -d --name db arminc/clair-db:latest
docker run -p 6060:6060 --link db:postgres -d --name clair arminc/clair-local-scan:latest
```

Puis nous pouvons lancer le scan d’une image docker (ici etherpad) : 

```
clair-scanner --ip YOUR_LOCAL_IP etherpad/etherpad
```

## **Résultat**

Une fois l’analyse terminée, clair-scanner va créer un rapport listant toutes les vulnérabilités du conteneur donné en entrée. Par exemple, nous l’avons fait analyser le conteneur etherpad du projet doodle-student et voici ce que clair-scanner nous a fourni (en ne donnant que les 5 premières vulnérabilités détectées car il y en a beaucoup) : 

```
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| STATUS     | CVE SEVERITY                | PACKAGE NAME | PACKAGE VERSION         | CVE DESCRIPTION                                              |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| Unapproved | Critical CVE-2022-1292      | openssl      | 1.1.1n-0+deb10u1        | The c_rehash script does not properly sanitise shell         |
|            |                             |              |                         | metacharacters to prevent command injection. This            |
|            |                             |              |                         | script is distributed by some operating systems in           |
|            |                             |              |                         | a manner where it is automatically executed. On such         |
|            |                             |              |                         | operating systems, an attacker could execute arbitrary       |
|            |                             |              |                         | commands with the privileges of the script. Use of           |
|            |                             |              |                         | the c_rehash script is considered obsolete and should        |
|            |                             |              |                         | be replaced by the OpenSSL rehash command line tool.         |
|            |                             |              |                         | Fixed in OpenSSL 3.0.3 (Affected 3.0.0,3.0.1,3.0.2).         |
|            |                             |              |                         | Fixed in OpenSSL 1.1.1o (Affected 1.1.1-1.1.1n).             |
|            |                             |              |                         | Fixed in OpenSSL 1.0.2ze (Affected 1.0.2-1.0.2zd).           |
|            |                             |              |                         | https://security-tracker.debian.org/tracker/CVE-2022-1292    |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| Unapproved | Critical CVE-2022-2068      | openssl      | 1.1.1n-0+deb10u1        | In addition to the c_rehash shell command injection          |
|            |                             |              |                         | identified in CVE-2022-1292, further circumstances where     |
|            |                             |              |                         | the c_rehash script does not properly sanitise shell         |
|            |                             |              |                         | metacharacters to prevent command injection were found       |
|            |                             |              |                         | by code review. When the CVE-2022-1292 was fixed it was      |
|            |                             |              |                         | not discovered that there are other places in the script     |
|            |                             |              |                         | where the file names of certificates being hashed were       |
|            |                             |              |                         | possibly passed to a command executed through the shell.     |
|            |                             |              |                         | This script is distributed by some operating systems         |
|            |                             |              |                         | in a manner where it is automatically executed. On such      |
|            |                             |              |                         | operating systems, an attacker could execute arbitrary       |
|            |                             |              |                         | commands with the privileges of the script. Use of the       |
|            |                             |              |                         | c_rehash script is considered obsolete and should be         |
|            |                             |              |                         | replaced by the OpenSSL rehash command line tool. Fixed      |
|            |                             |              |                         | in OpenSSL 3.0.4 (Affected 3.0.0,3.0.1,3.0.2,3.0.3).         |
|            |                             |              |                         | Fixed in OpenSSL 1.1.1p (Affected 1.1.1-1.1.1o).             |
|            |                             |              |                         | Fixed in OpenSSL 1.0.2zf (Affected 1.0.2-1.0.2ze).           |
|            |                             |              |                         | https://security-tracker.debian.org/tracker/CVE-2022-2068    |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| Unapproved | High CVE-2022-29155         | openldap     | 2.4.47+dfsg-3+deb10u6   | In OpenLDAP 2.x before 2.5.12 and 2.6.x before               |
|            |                             |              |                         | 2.6.2, a SQL injection vulnerability exists in the           |
|            |                             |              |                         | experimental back-sql backend to slapd, via a SQL            |
|            |                             |              |                         | statement within an LDAP query. This can occur during        |
|            |                             |              |                         | an LDAP search operation when the search filter              |
|            |                             |              |                         | is processed, due to a lack of proper escaping.              |
|            |                             |              |                         | https://security-tracker.debian.org/tracker/CVE-2022-29155   |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| Unapproved | High CVE-2021-33574         | glibc        | 2.28-10+deb10u1         | The mq_notify function in the GNU C Library (aka glibc)      |
|            |                             |              |                         | versions 2.32 and 2.33 has a use-after-free. It may          |
|            |                             |              |                         | use the notification thread attributes object (passed        |
|            |                             |              |                         | through its struct sigevent parameter) after it has          |
|            |                             |              |                         | been freed by the caller, leading to a denial of service     |
|            |                             |              |                         | (application crash) or possibly unspecified other impact.    |
|            |                             |              |                         | https://security-tracker.debian.org/tracker/CVE-2021-33574   |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
| Unapproved | High CVE-2022-23218         | glibc        | 2.28-10+deb10u1         | The deprecated compatibility function svcunix_create         |
|            |                             |              |                         | in the sunrpc module of the GNU C Library (aka glibc)        |
|            |                             |              |                         | through 2.34 copies its path argument on the stack           |
|            |                             |              |                         | without validating its length, which may result in           |
|            |                             |              |                         | a buffer overflow, potentially resulting in a denial         |
|            |                             |              |                         | of service or (if an application is not built with a         |
|            |                             |              |                         | stack protector enabled) arbitrary code execution.           |
|            |                             |              |                         | https://security-tracker.debian.org/tracker/CVE-2022-23218   |
+------------+-----------------------------+--------------+-------------------------+--------------------------------------------------------------+
```

# Conclusion

Nous avons pu voir des notions de Devops notamment avec le déploiement automatique et la sécurité avec certains outils existants. L’utilisation de ces technologies tout au long du développement vous aideront à faciliter la sécurisation de l'application et à détecter les failles de sécurité. Vous pourrez ainsi être plus efficient et avoir une application sécurisée.
