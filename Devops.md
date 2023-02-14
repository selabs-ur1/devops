# Introduction

Le DevOps est un mouvement en ingénierie informatique et une pratique technique visant à l'unification 
du développement logiciel (DEV) et de l'administration des infrastructures informatiques (OPS), 
notamment l'administration système. 

Il correspond donc à l'union des personnes, processus, et produits permettant de mettre en place du Continuous 
Delivery pour fournir de la valeur aux utilisateurs finaux. Cela implique de créer une culture et 
un environnement dans lesquels le buidling, les tests, et les déploiements de versions logiciels peuvent être 
effectués rapidement, avec une fréquence plus élevée, plus de fiabilité, afin de pouvoir innover et fournir 
des produits aussi rapidement que dans une Start-Up. 

Devops intègre plusieurs outils par exemple Gitlab qui permet au DevOps de déposer son code source sur la 
plateforme collaborative du même nom. Objectif : permettre à plusieurs utilisateurs de travailler sur un projet 
commun et uniformiser le résultat final. Des options de déploiement continu, de monitoring et de sécurité 
applicative sont disponibles pour couvrir l’ensemble du travail du DevOps. 

# Contexte

DevOps est un etat d'esprit mais son adoption par les organisations est énormément compliquée 
en raison des grandes différences entre la manière dont DevOps promeut le travail et la manière 
traditionnelle dont la plupart des entreprises de logiciels travaillent depuis des décennies. 
C’est dans ce contexte que cet article a été écrit afin d'examiner les problèmes et les facteurs 
qui poussent ces entreprises à adopter le DevOps. 

# Objectif

L’étude réalisée dans cet article vise à la compréhension des problèmes, pour accélérer le processus 
de livraison de logiciels, auxquels de nombreuses entreprises sont confrontées au quotidien et qui 
justifient l'adoption du Devops dans l’organisation de leurs business.  

# Etude réalisée

L’étude a été réalisée sous forme d'entretiens approfondis et semi-structurés de novembre 2017 à février 
2020 sur plusieurs entreprises multinationales selon différents critères notamment la taille, le type, 
l'activité et la date. Elles ont intégré Devops dans leur pratique depuis maintenant 2 ans. Les entretiens 
ont été menés en face à face par deux chercheurs et ont duré environ 2,5 heures, bien que dans certains cas, 
des personnes ont été interrogées plusieurs fois. La plupart des entreprises participant à l'étude ont été 
contactées lors d'événements liés à DevOps, tels que DevOps Spain3, les événements itSMF4, et DevOpsDays5, entre autres. 
Ce sont entre autres 44 personnes dont des PDG, des DSI, des responsables de plateformes DevOps, 
des responsables de produits, des développeurs et des gestionnaires d’infrastructure au total qui ont été interrogés. 

L’étude s’accentue principalement sur deux questions de recherche auxquelles l’analyse de celle-ci doit répondre. 

## Question 1 : Quels problèmes les entreprises cherchent elles à résoudre en implémentant Devops ?

Des questions ont été posées aux entreprises dans le but de savoir les problématiques auxquelles elles sont confrontées. 
La figure ci-dessous indique le nombre de fois où celle-ci apparaissaient dans les entretiens. 

<img title="Question 1" alt="Question 1" src="/images-devops/1.png">
<img title="Question 2" alt="Question 2" src="/images-devops/2.png">

Les problèmes les plus pertinents qui Les problèmes qui motivent une transition vers DevOps concernaient le besoin 
d'être plus agile, rapide et les demandes ou tendances de l'entreprise et/ou du marché. 

D’abord, le besoin de rapidité est mis en évidence par les extraits suivants : 
« Le temps qui s'écoule entre le moment où les équipes de développement terminent un nouveau logiciel et celui où il 
est déployé en production est très long, parfois des mois ». 
Les entreprises sont unanimes du besoin d’être plus agile et rapide. 

Aussi les raisons d'adopter DevOps ne viennent pas de l'intérieur de l'organisation mais plutôt des demandes 
des clients lié aux facteurs de transformation numérique. Il en ressort que pour les clients, avec l'introduction de 
DevOps il est évident d’obtenir un logiciel de meilleure qualité en moins de temps. 

## Question 2 : Quels résultats les entreprises tentent-elles d'obtenir en mettant en œuvre DevOps ? 

La figure montre les résultats et avantages que les entreprises espèrent obtenir grâce à l'adoption de DevOps.

<img title="Résultat 3" alt="Résultat 3" src="/images-devops/3.png">

Les résultats communs à toutes les entreprises sont les délais de livraison plus rapide des produits, 
l’obtention d’un logiciel de meilleure qualité et l’automatisation des processus augmentant l’efficacité, 
l’optimisation et la productivité des équipes.  

D’abord, Les organisations ont déclaré que le bénéfice le plus attendu lorsqu'elles ont initié leur transformation 
DevOps était l'accélération de la mise sur le marché en réduisant les délais de développement, de test, d'assurance 
qualité, de déploiement et de livraison. 

Ensuite, les technologies DevOps aident à livrer des logiciels plus rapidement, mais aussi à construire des logiciels 
de meilleure qualité grâce aux tests automatisés qui vont être exécutés. 

Enfin grâce à l'intégration et déploiement continu, les entreprises s’attendent à la réduction du temps consacré à 
la mise en place des environnements et le manque de normalisation et d'automatisation des tâches, auparavant effectuées 
manuellement. Cela permet d'éviter les erreurs humaines et de réduire le temps de mise en production 

# Ouverture

DevOps résout certaines problématiques des entreprises mais cette philosophie n’est pas appréciée de tous 
et certaines personnes pensent le contraire. 

Tel est le cas de Lee Briggs, ingénieur logiciel chez Pulumi, une société d'infrastructure en tant que code, 
qui soutient que l'approche DevOps est un échec[1]. Selon lui, DevOps représente tout simplement des opérationnels 
qui essaient de convaincre les développeurs de faire les choses à leur manière. En effet, la quasi-totalité de 
l'outillage commercialisé sous le nom de "DevOps tooling" est axée sur les opérations. 
C’est dans ce sens par exemple qu’il propose de passer à une approche qu’il qualifie de SoftOps 
(Operations for Software Developers) qui consiste à construire des pratiques opérationnelles centrées sur
l'amélioration de la vie des développeurs. 

En outre, l'adoption de DevOps peut souvent être soumis à certains contraintes comme :

- Une complexité accrue : en intégrant plusieurs outils et technologies dans le processus 
  de développement logiciel, les entreprises peuvent créer un environnement de production 
  plus complexe, difficile à gérer et à dépanner. 

- Un manque de normalisation : dans nos recherches, nous n’avons trouvé actuellement aucune 
  normalisation à l'échelle du secteur pour DevOps. Par conséquent, les entreprises qui adoptent 
  le DevOps peuvent avoir besoin de créer leurs propres processus et outils personnalisés, 
  ce qui peut prendre du temps, être coûteux et entraîner une certaine confusion parmi 
  les employés quant à la meilleure façon d'utiliser les techniques DevOps. 

 
- Une augmentation des coûts : en exigeant l'achat de ressources matérielles et logicielles 
  supplémentaires et l'embauche de professionnels DevOps expérimentés, les entreprises peuvent 
  constater une augmentation significative de leurs dépenses informatiques. 

# Conclusion

Afin de rendre le voyage vers le cloud fluide et efficace, les entreprises technologiques devraient 
adopter les principes et les pratiques DevOps, même s’il est parfois très difficile d’implémenter 
toutes ces pratiques, nous pouvons citer ici par exemple le cas du passage à des architectures microservices 
alors que les applications ont été conçues depuis des années avec des architectures monolithes. 
On peut adopter les autres bonnes pratiques comme l’implémentation des tests unitaires dans un système 
d’intégration continue afin que les équipes puissent intégrer leur modification plus paisiblement pour 
ce type d’architecture monolithe et leur dégager plus de temps pour leurs tâches supplémentaires. 

DevOps vise à créer une culture et un environnement dans lesquels la conception, les tests et la diffusion 
de logiciels peuvent être réalisés rapidement, fréquemment et efficacement. 

Aujourd’hui le mur entre développements et opérations s’écoule progressivement car finalement, ces deux pôles œuvrent pour un objectif commun.

# Bibliographie

1 : https://alm.developpez.com/actu/334699/DevOps-est-il-un-echec-Un-ingenieur-logiciel-de-Pulumi-pense-que-c-est-le-cas-et-propose-de-passer-a-une-nouvelle-approche-appelee-SoftOps-pour-resoudre-les-problemes-que-DevOps-n-a-pu-resoudre.

2 : https://www.orientsoftware.com/blog/advantages-and-disadvantages-of-devops/

3 :  https://blog.ippon.fr/2020/07/29/le-devops-explique-a-mes-parents/ 
