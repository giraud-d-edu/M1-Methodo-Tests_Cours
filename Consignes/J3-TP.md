# J3-TP

## Intro

- TP noté
- Binome
- Lien du classroom : https://classroom.github.com/a/xdH9Y1XD
- Il doit y avoir vos prénoms pour le nom du groupe => exemple : RémyIngrid

## Sujet

En continuant à utiliser Spring Boot Starter Test (JUnit 5, Mockito, AssertJ) implémenter les test pour la classe EventService (sont fourni le modèle, le service et le repository)

1. Avoir un coverage de 100 % sur les tests
2. Faire du [TDD](https://fr.wikipedia.org/wiki/Test_driven_development) pour pouvoir :
   - récupérer les événements du jour
   - archiver les événements vieux de plus de 30 jours
   - modifier l'entité pour lui ajouter un status qui permet d'annuler l'événement puis ajouter une règle métier qui empêche d'annuler moins de 24H avec l'événement.
   - (contexte événement e-sport) permettre d'indiquer les deux équipes qui vont s'affronter depuis l'entité event
   - (contexte événement e-sport) ajouter un champs teaser qui décrit le nom des équipes, des joueurs qui la compose, la date, la ville du match
3. Passer par les annotations `@Mock` et `@InjectMocks` en remplacement de ` @MockitoBean` et `@Autowired` pour gagner en rapidité (enlève le contexte spring complet)

## Livrables Attendus

- Code source complet avec tests (repository GitHub)
    - Vous êtes autorisé à modifier le code de `src` fournit, mais il faut le justifier
    - Merci de lié chaque changement à un commit avec un message clair par soucis de lisibilité
- Rapport de couverture JaCoCo (>= 100%) OU screenshot de votre IDE
- Démonstration des fonctionnalités implémentées et des choix techniques (dans le `README.md`)
