

# INTEGRATION CONTINUE

### Configuration de Jenkins
Pour débuter la configuration, il faut aller dans le menu « Jenkins → Manage Jenkins → Global Tool Configuration » puis cliquez sur Add Maven afin d’installer Maven et add Jdk.

Le projet utilisant Maven pour la gestion des dépendances, Gitlab pour le dépôt des codes sources et docker, il faut d’abord installer les plugins Jenkins nécessaires. Pour cela allez dans le menu « Jenkins → Manage Plugins → Available » et ajouter les plugins suivants :

- Docker Plugin : permettant à Jenkins de se connecter au serveur Docker afin de gérer les images de conteneur et les conteneurs en cours d'exécution ainsi que démarrer/arrêter les conteneurs
- Maven integration Plugin : permettant à Jenkins de construire des projets Maven et d'exécuter les tests automatisés en utilisant Maven
- Git plugin : permettant à Jenkins de récupérer et surveiller le dépôt Git

Après l’installation des plugins, il est nécessaire de redémarrer Jenkins.

### Création d’une tâche Jenkins
- Cliquez sur "Nouvelle tâche" pour créer un nouveau projet Jenkins.
- Sélectionnez "Projet déclenché par un dépôt de code source" pour configurer Jenkins pour surveiller un dépôt de code source et déclencher une construction lorsqu'un développeur envoie un code.
- Entrez le nom de votre projet et sélectionnez un type de projet approprié, ici "Projet Maven" car Maven est utilisé pour gérer les dépendances et la construction de votre application.
- Configurez les détails de votre dépôt de code source, tels que l'URL du dépôt Git associé, les informations d'identification pour accéder au dépôt et les paramètres de la branche.

### Création des Job Jenkins
Un job Jenkins est un ensemble de paramètres et de configurations qui définissent comment Jenkins doit exécuter une tâche spécifique. Dans notre cas nous allons créer des jobs pour

Créer un nouveau job Jenkins pour chaque microservice de l’application. Configurez chaque job pour utiliser Maven pour construire le microservice, et Docker pour créer l'image de conteneur.

### Automatisation du build
Ici il s'agit de configurer Jenkins pour construire automatiquement les images de conteneur pour chaque microservice à partir de leur code source en utilisant Docker.

### Suivi des performances
Configurer Jenkins pour surveiller les performances des microservices en production et pour alerter en cas de problème.



# ANALYSE STATIQUE

### Installation et configuration de Jenkins pour l'analyse
Pour faire l'analyse statique des microservices, vous allons utiliser l’outils d'analyse statique de code Sonarqube.
Pour cela on installe le plugin SonarQube pour Jenkins.

### Exécution de l’analyse
- Ouvrez le job Jenkins associé à un microservice Poll par exemple
- Cliquez sur la section "Configurer" pour accéder à la configuration du job.
- Dans la section "Build", cliquez sur "Ajouter une étape de construction".
- Sélectionnez "Exécuter un script SonarQube"
- Dans le champ "Commande", entrez la commande de génération de rapport SonarQube. Par exemple, en utilisant le sonar-scanner pour générer un rapport  on a la commande :
	_sonar-scanner \_
	  _-Dsonar.host.url=https://your-sonarqube-instance \_
	  _-Dsonar.projectKey=your-project-key \_
	  _-Dsonar.projectName=Your Project Name \_
	  _-Dsonar.projectVersion=1.0 \_
	  _-Dsonar.sources=._

- Cliquez sur "Enregistrer" pour enregistrer les modifications.
- Le rapport sera disponible dans l’interface sonarQube. Le rapport contient :
	- La qualité du code
	- La couverture du code
	- La complexité du code
	- Les métriques techniques.


# AUTOMATISATION DES TESTS

L'automatisation des microservices peut être réalisée à l'aide de divers outils et méthodes, mais nous allons utiliser Jenkins pour automatiser les tests unitaires de ce projet.
- Configuration du script pipeline en y ajoutant le script permettant d’accéder au fichier de test JUnit écrit au préalable sur le dépôt git
- Lancement du build
- On obtient des graphiques permettant d’analyser :
	- Le temps de réponses de chaque page
	- Le pourcentage d’erreur des tests : la couverture
	- Le nombre de requête par seconde