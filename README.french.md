# Doodle in quarkus

Ce repository est une application de type doodle développée avec quarkus.io pour le back et angular pour le front. 

Elle initialise automatiquement un pad pour la réunion et un salon de discussion. 

Le but est de faire travailler les étudiants sur la partie déploiement de ce type d'application dite cloud native. 

Votre mission est de mettre en production une telle application en permettant 
- qu'à chaque commit sur ce repository, si les tests passent, alors nous déployons automatiquement une nouvelle version dans un contexte (Continuous Deployement)
- que l'application doit être monitorer finement. 
- que l'application redémarre automatiquement en cas de crash du serveur ou de crash d'un des services de l'application. 
- que Les accès doivent http doivent utiliser https. 


Une démo de l'application est accessible [ici](https://doodle.diverse-team.fr).

- Voici une petite [vidéo](https://drive.google.com/file/d/1GQbdgq2CHcddTlcoHqM5Zc8Dw5o_eeLg/preview) de présentation des fonctionnalités de l'application.
- Voici une petite [vidéo](https://drive.google.com/file/d/1l5UAsU5_q-oshwEW6edZ4UvQjN3-tzwi/preview) de présentation de l'architecture de l'application.
- Voici une petite [vidéo](https://drive.google.com/file/d/1jxYNfJdtd4r_pDbOthra360ei8Z17tX_/preview) de revue de code de l'application.

Un descriptif du cours, des TPs et des étapes du projet est lui accessible [ici](https://hackmd.diverse-team.fr/s/SJqu5DjSD)
