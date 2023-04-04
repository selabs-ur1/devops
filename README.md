Problematique abordée: L’objectif de ce travail était de se familiariser avec les outils d'intégration continue dans un contexte devops en donnant les différentes étapes du processus d’intégration continue, les différents outils utilisés, et en analysant également les résultats obtenus.L’intégration continue une pratique clé du processus DevOps qui consiste à intégrer fréquemment et automatiquement des modifications afin de détecter rapidement les erreurs et de réduire le temps nécessaire pour mettre à jour le code. Son processus implique l'utilisation d'outils d'automatisation de tests  qui permettent de compiler et de tester automatiquement le code à chaque modification, puis de signaler les erreurs aux développeurs. Les tests peuvent inclure des tests unitaires, des tests de performance, des tests de sécurité et d'autres types de tests qui vérifient que le code fonctionne correctement et est conforme aux exigences.


Presentation d'outils
Junit: JUnit est un framework open-source pour les tests unitaires en Java. Il fournit des annotations et des méthodes pour faciliter l'écriture de tests unitaires en Java. Les tests unitaires sont des tests qui vérifient le comportement d'une unité de code (par exemple, une méthode ou une classe) de manière isolée, afin de s'assurer qu'elle fonctionne comme prévu.JUnit définit deux types de fichiers de tests. Les TestCase (cas de test) sont des classes contenant un certain nombre de méthodes de tests. Un TestCase sert généralement à tester le bon fonctionnement d'une classe.Une TestSuite permet d'exécuter un certain nombre de TestCase déjà définis.
Maven: Maven est un outil de gestion et d'automatisation de production des projets logiciels Java en général et Java EE en particulier. Il est utilisé pour automatiser l'intégration continue lors d'un développement de logiciel, il fournit un moyen de décrire les dépendances de votre projet, ainsi que les étapes de construction et de déploiement 

Utlisation des outils:
Nous avons monté notre projet avec le gestionnaire de dépendances Maven.
Nous avons crée des classes de test dans notre projet en important la bibliothèque JUnit 4.
Dans nos classes de test, nous avons ecrit des méthodes de test pour chaque fonctionnalité que nous voulons tester.
Au niveau de notre fichier pom.xml, nous avons pu generer la dependance Junit4 et les plugings neccesaires pour faire des rapports de tests detaillés

Les limites des outils qu'on a utlisés:
Jenkins :
Configuration complexe : La configuration de Jenkins peut être complexe et difficile pour les débutants, ce qui peut nécessiter un certain temps pour apprendre et maîtriser.
Scalabilité : Bien que Jenkins soit très extensible, il peut ne pas être idéal pour les grandes équipes et les projets complexes qui nécessitent une grande capacité de traitement.
Dépendance aux plugins : Jenkins repose largement sur les plugins pour étendre ses fonctionnalités, ce qui peut entraîner des problèmes de compatibilité et de sécurité.
Maven :
Performance : Bien que Maven soit capable de gérer des projets de grande taille, il peut parfois être lent en raison de son approche basée sur les plugins et la configuration XML.
Dépendances : Maven gère les dépendances des projets en téléchargeant les bibliothèques requises à partir des dépôts centraux, ce qui peut poser des problèmes si les bibliothèques sont absentes ou obsolètes.
Intégration avec d'autres outils : Bien que Maven dispose d'une intégration solide avec de nombreux outils de développement, il peut être difficile d'intégrer certains outils personnalisés ou tiers.
JUnit :
Tests complexes : JUnit peut être limité dans la prise en charge de tests unitaires complexes ou sophistiqués.
Couverture de code : JUnit ne fournit pas de fonctionnalités de couverture de code intégrées, vous devez utiliser un plugin tiers ou une autre solution pour obtenir une couverture de code détaillée.
Test d'intégration : JUnit est conçu pour les tests unitaires et peut ne pas être idéal pour les tests d'intégration, qui peuvent nécessiter des outils supplémentaires pour être gérés.

