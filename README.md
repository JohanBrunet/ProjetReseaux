# ProjetReseaux

Projet du cours de r�seaux du S5 (Polytech' Montpellier).

## Sujet

Le projet consiste � cr�er un client et un serveur permettant le calcul d'une factorielle.
Le client demande le calcul de la factorielle d'un certain nombre n au serveur.
Le serveur cr�� un nouveau client (factice) qui lui demandera le calcul de la factorielle de n-1.
Par appels r�cursifs de clients factices, le serveur calcule alors n x fact(n-1) et renvoie le r�sultat au client initial.

Une deuxi�me partie du projet sera identique mais pour le cacul de la suite de fibonacci, avec cr�ation de deux clients pour le calcul de fib(n-1) et fib(n-2).
