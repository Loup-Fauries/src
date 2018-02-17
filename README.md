# BlackJack
> Projet Java STRI 2017/2018

![alt text](http://black-jack-en-ligne.net/wp-content/uploads/2016/10/blackjack-histoire.jpg)

### Version 1 : Casino et Joueurs

> Date de validation: 31 Jan. 2018

Dans cette première version, une seule partie est organisée à la fois. 1 serveur central est chargé d’héberger et de synchroniser le jeu des clients. Ce serveur central jouera le rôle de croupier (qui distribue les cartes, joue pour la banque et détermine le gagnant). Les clients doivent disposer de leur côté d’une application permettant de jouer.



### Version 2 : Casino, Tables et Joueurs

> Date de validation: 19 Fev. 2018

Maintenant, on souhaite dissocier les rôles auparavant assurés par le serveur central :
hébergement de parties et parties.



### Version 3 : Casino, Tables, Croupier et Joueurs

> Date de validation: 19 Mar. 2018

On souhaite maintenant dissocier les rôles de table et de croupier. Des croupiers (automatiques) sont recrutés par le Casino. Ils devront être connus du Casino et affectés lors de la création d’une table et licenciés lors de la destruction d’une table. Le reste des éléments ne change pas.



### Version 4 : Casino, Mises, Croupier et Joueurs

> Date de validation: 28 Mar. 2018

Afin de rendre le jeu plus intéressant, on va maintenant gérer les mises de jeton des joueurs. Un « service des mises » sera chargé de la gestion des mises de tous les joueurs ainsi que de la banque de toutes les tables (via le croupier).



### Options : Optimisation et/ou outils de communication
- Chaque croupier peut dorénavant héberger 1 partie. 
- Le serveur central dispose de la connaissance des tables (en cours ou à venir) et de leurs participants. En outre, l’application du client devra proposer des outils de communications (dont la portée est limitée à une table). 
- Un salon particulier sera proposé permettant aux joueurs de s’organiser pour proposer des parties et les rejoindre.