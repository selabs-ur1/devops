# GL

Projet de microservicisation en génie logiciel

## Organisation du repository

Les trois images png à la racine correspondent comme leurs noms l'indique à l'architecture à laquelle nous avons pensé au début du projet, avec et sans les bases de données des API et à l'architecture que nous avons obtenu à la fin de ce projet. 

L'application se trouve dans le dossier 'doodlestudent-main'. On y retrouve le front et l'ensemble des micro-services de l'application. 

## Exécution du projet sans script

Lors de la première exécution du projet, il est possible de devoir installer les modules nécessaires avec la commande 'npm install' dans tous les dossiers "api_" ainsi que dans le dossier front.

Il faut ensuite exécuter les api_* et le front avec la commande 'npm run start' ainsi que le dossier "api" avec la commande 'docker-compose up --detach & ./mvnw compile quarkus:dev' dans des terminaux différents. 
Une fois que tout est en cours d'éxécution, l'application est accessible via le localhost: http://localhost:4200.

## Exécution du projet avec script

Pour installer les modules nécessaires, il faut exécuter le script 'doodlestudent-main/scripts/installApp.sh'.

Pour lancer les micro-services, il faut modifier le script 'doodlestudent-main/scripts/launchApp.sh' et modifier les chemins absolus jusqu'aux script. Puis il faut l'exécuter via la commande './launchApp.sh'. Une fois que tout est en cours d'éxécution, l'application est accessible via le localhost: http://localhost:4200.
