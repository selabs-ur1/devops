# Software bots in Software Engineering

**BARTHELAT Etienne - CONDORI Miguel - LEMAITRE Lucas - MOLL Jean-Loup - JUGIEAU Théo**

L'objectif de cette PR est de présenter différents bots servant à automatiser des tâches sur des dépôts git.

## Renovatebot :
Ce bot sert à mettre à jour les dépendances dans un projet dès qu’il en trouve de nouvelles, il va donc émettre des pulls requests sur le repos (github, gitlab, bitbucket …) et même les auto-merger (si paramétré ainsi).

**Remarque :** L’installation est directe pour github sinon l’application doit être self-hosted.

**Installation sur un repos github :**
Rendez-vous sur [GitHub Apps - Renovate](https://github.com/apps/renovate) et installez le bot sur le projet ciblé.
![image](https://user-images.githubusercontent.com/102468174/207422325-c620f2bd-f548-4161-b9d8-55f5d5a5342b.png)

Puis 

![image](https://user-images.githubusercontent.com/102468174/207422661-86dbdad3-5b17-4944-a48d-7255ed51ef95.png)

Ensuite, rendez-vous sur [GitHub Apps - Renovate](https://github.com/apps/renovate) pour configurer le bot sur un projet spécifique si ce n’est pas déjà fait avec la phase d’installation.

Après l’installation et configuration, le bot va émettre une première pull request sur votre dépôt afin de créer un fichier renovate.json qui contiendra les paramètres du bot que vous pourrez alors modifier par la suite si besoin.

Par exemple, on peut ajouter l'option d'auto-merging des PR : ``"automerge": true``,
ou bien programmer les horaires des PR : ``"schedule": ["before 2am"]``

On peut trouver les autres options de configuration [ici](https://docs.renovatebot.com/configuration-options/)

**Exemple d'une pull request réalisée par le bot :**
 ![image](https://user-images.githubusercontent.com/102468174/206925292-2d8fbc69-5eac-4a1e-b7ab-1d11fead0f69.png)

## Rultor, a DevOps team assistant

Rultor permet d'automatiser les opérations git comme merge, deploy et release avec une interface chat-bot intégré dans les commentaires d'une pull request.
Rultor permet d'isoler le script de déploiement dans son propre environnement virtuel en utilisant Docker. Ce qui permet de réduire les états externes qui pourrait entrainer des erreurs lors des tests.

Lors d'une pull request, lorsqu'on va lui demander, Rultor va récupérer la branche master et y appliquer les modifications proposés. Il va ensuite tout exécuter sur son Docker et si tout se passe bien sans erreurs, il va merge la branche dans la branche master. Cela permet de réduire les risques que les développeurs cassent la branche master en faisant une pull request contenant des erreurs. Grâce à cela, les développeurs ont moins peur de faire des erreurs et augmente leur productivité. 
Rultor est très facilement utilisable grâce à des mots clés à mettre dans les commentaires. 

### Comment mettre en place Rultor ? 

Dans un premier temps, il faut récupérer Rultor dans le [Marketplace de Git](https://github.com/marketplace/rultor-com) et récupérer le fichier rultor.yml de ce [dépôt](https://github.com/yegor256/rultor) qui sera à mettre à la racine du projet.

Tout d'abord il faut créer un serveur Ubuntu accessible via l'adresse `b4.rultor.com`, Rultor s'y connectera via SSH. Il faut ensuite installer Docker Engine dessus.
```
$ sudo apt-get update
$ sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

Il faut ensuite configurer le serveur pour donner les droits d'accès à Rultor.
```
$ apt-get install -y bc
$ groupadd docker
$ adduser rultor
$ gpasswd -a rultor docker
$ echo 'rultor ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers
$ mkdir /home/rultor/.ssh
$ cat > /home/rultor/.ssh/authorized_keys
$ chown rultor:rultor -R /home/rultor/.ssh
$ chmod 600 /home/rultor/.ssh/authorized_keys
```

Puis, nous devons créer l'image que le docker va devoir utiliser.

On créé un conteneur `sudo docker run -i -t ubuntu /bin/bash`, on installe les packages désirés, et on génere son image `sudo docker commit 215d2696e8ad rultor/beta`.
Il est aussi possible de récupérer l'[image par défaut de Rultor](https://hub.docker.com/r/yegor256/rultor-image/).

Nous envoyons ensuite l'image sur le hub Docker `sudo docker push rultor/beta` et nous modifions le fichier rultor.yml : 
```
docker:
  image: rultor/beta
```

Enfin, pour exécuter Rultor, il suffit de faire une pull request sur la branche master du projet git et écrire `@rultor merge` en commentaire.

## Créer son propre bot avec Probot :

### Prérequis : npm, node, npx installés sur votre machine

**/!\\** La doc indique une version de node au moins 10, mais ce n'est pas à jour : il faut une version strictement supérieure à la 10 (nous avons fait avec la 12).

Pour l’installation de node, nous conseillons de passer par le gestionnaire de package nvm. 

**Installation nvm :** ``curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.11/install.sh | bash``


**Installation node/npm :**
``nvm install v12.10.0``
Si pas fait par défaut, activez le node que vous venez d’installer :
``nvm use v12.10.0``

**Installation npx :** ``npm install -g npx``

### Probot :
Exécutez  ``npx create-probot-app my-first-app``.
Des questions que vous pouvez ignorer vous sont posées.
![image](https://user-images.githubusercontent.com/102468174/207423162-e6b1b240-8996-4aca-842e-177db58269b4.png)

Un répertoire se crée alors, allez-y puis exécutez
``npm start``.
![image](https://user-images.githubusercontent.com/102468174/207423253-7d7916c8-9efb-4085-84f8-82ba55f364c4.png)

Rendez-vous ensuite sur votre navigateur à l’adresse indiquée dans votre navigateur.
![image](https://user-images.githubusercontent.com/102468174/206924655-3bbe8151-edef-48c6-b1c4-2d3e6d75548d.png)

Ensuite suivez les indications, ajoutez votre bot à un repos (cf renovatebot).

![image](https://user-images.githubusercontent.com/102468174/206924699-60e4a52d-0072-4f2c-938f-10d490db67e9.png)

![image](https://user-images.githubusercontent.com/102468174/207423433-2eb63231-655f-4110-8de0-2ed9746a7ecc.png)

Une fois fait, redémarrez le serveur: Ctrl+C, puis ``npm start``.
Pour voir si ça fonctionne avec le cas d’exemple des issues : créez un nouvel issue, et si tout se passe bien, le bot doit vous répondre.

C’est donc là que va commencer la partie intéressante qui va être la personnalisation du bot, qui est contenue dans le fichier index.js de votre bot.

Vous retrouverez la documentation des webhooks events associés à github [ici](https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads)
Il y a également de nombreux exemples de bots réalisés avec probot. Par exemple, le bot [WIP](https://github.com/wip/app) ajoute un marqueur à la PR lorsque l'on ajoute WIP au nom de sa PR permettant ainsi d'éviter qu'elle se fasse merger par erreur par quelqu'un d'autre. 
On pourrait par exemple imaginer un bot qui applique un linter aux pull requests afin que tout les développeurs aient la même mise en page de code, ou alors un bot qui crée des tickets lorsqu'il détecte des commentaires TODO dans le code.

## Repairnator
Repairnator est un bot conçu pour aider au développement de programmes Java. Il peut automatiquement identifier et corriger les erreurs dans le code Java en trouvant les erreurs et selon le type d'exception, en trouvant un patch qui peut être appliqué au code pour remédier à l'erreur, ce qui facilite la construction et la maintenance de projets pour les développeurs.

Dans le cadre de ce projet, nous avons exploré Repairnator en tant qu'application Github et en tant que plugin Maven car ce sont les déploiements les plus développés du bot. Cependant, il convient de noter que le bot est également disponible en tant qu'outil de ligne de commande, scanner Travis CI et en tant que scanner avec Flacocobot.

### Comment installer Repairnator sur GitHub

Pour utiliser Repairnator dans un dépôt GitHub, vous aurez besoin d'avoir un compte Travis CI valide. En plus d'avoir l'application Travis Ci github active sur le repository.
Allez sur la page GitHub de Repairnator et cliquez sur "Installer le bot".
![image](https://imgur.com/OWdmmvS.png)

Assurez-vous que le bot est actif dans le dépôt correct.
![image](https://imgur.com/glk1FYR.png)

Dans la racine de votre dépôt, créez un fichier .travis.yml qui inclut les étapes pour que Travis construise votre projet. Par exemple :

    cd folder/
    ignore tests = true
Finalement assurez-vous que le fichier .travis.yml contient la ligne :
    
    language = java
Pour que Repairnator sache qu'il doit être actif.

![image](https://imgur.com/8ja2LID.png)

### Comment installer Repairnator dans un projet Maven

Pour utiliser Repairnator dans un projet Maven, vous devrez installer le plugin Repairnator en l'ajoutant en tant que plugin dans le pom.xml.

    <!-- https://mvnrepository.com/artifact/fr.inria.repairnator/repairnator-core -->
    <dependency>
        <groupId>fr.inria.repairnator</groupId>
        <artifactId>repairnator-core</artifactId>
        <version>3.4</version>
    </dependency>

### Comment utiliser Repairnator

Une fois que vous avez installé Repairnator dans votre dépôt GitHub ou votre projet Maven, il essaiera automatiquement d'identifier et de corriger les erreurs qui se produisent pendant le processus de building.

Sur GitHub, Repairnator créera une pull request avec les modifications proposées, et laissera un commentaire indiquant ses actions sur la pull request échouée.
![image](https://imgur.com/iSSi7Hu.png)

Si Repairnator trouve un patch adapté, il modifiera le code afin que la pull request n'ait aucun conflit avec la branche de base.

Sur Maven, Repairnator essaiera directement de corriger le code.

## Conclusion sur l'utilisation des bots, et difficultés rencontrées

Nous avons vu qu'il existait des bots pour faire des tâches assez variées sur des dépôts git, cependant ils n'ont pas forcément tous leur place. Par exemple, un bot pour lancer une suite de tests n'a pas un énorme intérêt étant donné que d'autres outils comme Jenkins le font très bien. En revanche, un bot mettant à jour les dépendances ou facilitant la lecture du dêpots aux développeurs peut limiter l'erreur humaine.

Pour ce qui est des difficultés rencontrées durant ces TPs, on mentionnera en premier lieu les problèmes d'environnements : node et docker. Il y a également le fait que seul le propriétaire du dépôt puisse enregistrer les bots ce qui rend le travail en groupe plus compliqué : nous aurions aimé avoir un seul dépôt avec les bots de chacun ainsi au même endroit. Enfin, de nombreux bots doivent être self-hosted ce qui reste plus contraignant.
