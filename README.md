# KONE MORY, KOIZAN APPIA, COULIBALY MAWA, MONTCHO GLORIA, LAMIA GHARBI  
# Intégration continue, Analyse statique et Automatisation des tests

Pour ce tutoriel, nous utiliserons le front d'un projet réalisé dans le cadre du cours d'Architecture Logiciel fait en Angular.

## Problematique abordée:
L’objectif de ce travail était de se familiariser avec les outils d'intégration continue dans un contexte devops en donnant les différentes étapes du processus d’intégration continue, les différents outils utilisés, et en analysant également les résultats obtenus.L’intégration continue une pratique clé du processus DevOps qui consiste à intégrer fréquemment et automatiquement des modifications afin de détecter rapidement les erreurs et de réduire le temps nécessaire pour mettre à jour le code. Son processus implique l'utilisation d'outils d'automatisation de tests  qui permettent de compiler et de tester automatiquement le code à chaque modification, puis de signaler les erreurs aux développeurs. Les tests peuvent inclure des tests unitaires, des tests de performance, des tests de sécurité et d'autres types de tests qui vérifient que le code fonctionne correctement et est conforme aux exigences.

## Objectif:

Ces pratiques nous permettront d'améliorer la qualité de notre code, de détecter les erreurs plus rapidement et de déployer des versions de votre application plus rapidement et en toute confiance. Un des principaux but du DevOps


## Presentation d'outils
- Pour l'intégration continue
Il existe plusieurs plateformes d'intégration continue, telles que Travis CI, CircleCI, Jenkins, GitLab CI/CD, qui offrent des plans gratuits pour les projets open source.
- Pour l'analyse statique du code
On configure l'analyse statique du code en ajoutant des outils d'analyse statique tels que SonarQube, CodeClimate, Codacy, etc. Ces outils peuvent détecter des problèmes de qualité de code tels que des vulnérabilités, des erreurs de syntaxe et des erreurs de style.
- Pour l'automatisation des tests
On ajoute des tests automatisés pour les différentes parties de l'application en utilisant des frameworks de test tels que JUnit, PyTest, Mocha, etc. Ces tests automatisés doivent être ajoutés dans le fichier de configuration de l'étape de test sur la plateforme d'intégration continue.

## Utilisation des outils:
Nous nous sommes tournés vers l'utilisation de la version gratuite d'azure devops qui est une plateforme de microsoft qui nous regroupe tout ce dont on a besoin pour lancer l'intégration continue (à travers un fichier azure-pipelines.yml) avec une interface assez intuitive et interactive facilitant la , concernant l'analyse du code, nous la faisons via sonarqube qu'on paramètre à l'aide d'un fichier spécifié sonar-project.properties, nous faisons nos tests avec Karma, Karma est souvent utilisé pour tester des applications AngularJS, mais il peut être utilisé pour tester tout type d'application JavaScript. Il est également intégré dans de nombreux outils de développement tels que Visual Studio Code, WebStorm et IntelliJ IDEA.

## Etapes de mise en place :

- Créer un projet Azure DevOps 
On se connecte à notre compte Azure DevOps et on crée un nouveau projet pour notre application.

- Créer un pipeline d'intégration continue 
On Crée un pipeline d'intégration continue en utilisant Azure Pipelines. on configure le pipeline pour déclencher automatiquement lorsqu'un nouveau commit est poussé vers notre référentiel Git. Il est possible de configurer les étapes du pipeline pour construire, tester et déployer notre application, nous n'avons pas fait de déploiement par contre, cela pourrait  déboucher sur un approfondissement sur le sujet de notre part. La configuration de la pipeline se fait à travers le fichier azure-pipeline.yml

- Configurer l'analyse statique du code :
On ajoute des tâches dans notre pipeline pour effectuer une analyse statique du code en utilisant SonarQube. les configurations des règles d'analyse pour détecter les vulnérabilités, les erreurs de codage et les pratiques de codage non conformes sont dans notre fichier run.sh, il est possible de stopper le job lancé en cas de taux de coverage trop petit, dans ce genre de cas, il est demandé au developpeur de mieux suivre les règles de codage dans son code avant un éventuel déploiement

- Configurer l'automatisation des tests :
On ajoute des tâches dans notre pipeline pour exécuter des tests unitaires, des tests d'intégration et des tests de performance en utilisant Karma. On configure les tests pour s'exécuter automatiquement chaque fois qu'un nouveau commit est poussé vers votre référentiel Git.

- Configurer les notifications :
On configure des notifications pour être averti en cas d'échec du pipeline, de violation des règles d'analyse statique ou d'échec des tests automatisés. Les notifications peuvent être envoyées par e-mail ou Microsoft Teams selon la volonté de l'utilisateur.

## limites à notre solution:

- Dépendance à Microsoft : 
Azure DevOps est une solution cloud propriétaire de Microsoft. Les entreprises doivent donc s'assurer qu'elles sont à l'aise avec la dépendance à l'égard de Microsoft pour leur gestion de projets de développement logiciel.
- Besoin d'une connexion internet : 
Azure DevOps est une solution basée sur le cloud, ce qui signifie qu'une connexion Internet est nécessaire pour y accéder. Les entreprises doivent s'assurer que leur infrastructure informatique est capable de prendre en charge une telle solution basée sur le cloud.
