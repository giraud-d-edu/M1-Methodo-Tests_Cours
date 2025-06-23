package com.ynov.testing;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class CalculatriceTest {

    @Test
    @DisplayName("Should add two number")
    public void add_1_plus_1_should_return_2() {
        // Given
        Calculatrice calc = new Calculatrice();

        // When
        int res = calc.addition(1, 1);

        // Then
        assert(res == 2);
    }

    // Etape 0 : Implémenter la méthode vide addition() dans Calculatrice
    // RED - Le test doit échouer car la méthode n'existe pas
    // Etape 1 : Implémenter la méthode addition() pour que le test passe
    // GREEN - Implémentation minimale : return a + b;

    // ================== RÈGLES MÉTIER PROGRESSIVES ==================

    // Nouvelle règle : les nombres négatifs sont interdits dans cette calculatrice
    // Etape 2 : Implémenter un test ou plusieurs tests pour cette nouvelle règle
    // RED - Écrire un test qui vérifie qu'une exception est levée avec des nombres négatifs
    // Etape 3 : Implémenter le code source pour que le(s) test(s) passe(nt)
    // GREEN - Ajouter la vérification et lever IllegalArgumentException

    // Nouvelle règle : la calculatrice ne peut effectuer qu'un maximum de 100 opérations
    // Etape 4 : Implémenter un test ou plusieurs tests pour cette nouvelle règle
    // RED - Écrire un test qui effectue 100 opérations puis vérifie qu'une exception est levée à la 101ème
    // Etape 5 : Implémenter le code source pour que le(s) test(s) passe(nt)
    // GREEN - Ajouter un compteur d'opérations et la vérification de limite

    // ================== NOUVELLES MÉTHODES ==================

    // Nouvelle méthode : SOUSTRACTION
    // Etape 6 : Écrire un test pour la soustraction de base (ex: 5 - 3 = 2)
    // RED - Le test échoue car la méthode soustraction() n'existe pas
    // Etape 7 : Implémenter la méthode soustraction() basique
    // GREEN - return a - b;

    // Nouvelle règle pour soustraction : le résultat ne peut pas être négatif
    // Etape 8 : Écrire un test vérifiant qu'une exception est levée si résultat < 0
    // RED - Le test échoue car aucune vérification n'est faite
    // Etape 9 : Modifier soustraction() pour lever une exception si résultat négatif
    // GREEN - Vérifier le résultat avant de le retourner

    // Nouvelle méthode : MULTIPLICATION
    // Etape 10 : Écrire un test pour la multiplication de base (ex: 4 * 3 = 12)
    // RED - Méthode multiplication() inexistante
    // Etape 11 : Implémenter multiplication() basique
    // GREEN - return a * b;

    // Nouvelle règle pour multiplication : interdiction de multiplier par 0
    // Etape 12 : Écrire un test vérifiant qu'une exception est levée lors de multiplication par 0
    // RED - Aucune vérification du zéro
    // Etape 13 : Ajouter la vérification pour interdire la multiplication par 0
    // GREEN - Vérifier si a == 0 || b == 0 et lever exception

    // Nouvelle règle pour multiplication : le résultat ne peut pas dépasser 1000
    // Etape 14 : Écrire un test avec des nombres dont le produit > 1000
    // RED - Aucune vérification de limite
    // Etape 15 : Ajouter la vérification de limite dans multiplication()
    // GREEN - Vérifier le résultat avant de le retourner

    // Nouvelle méthode : DIVISION
    // Etape 16 : Écrire un test pour la division de base (ex: 10.0 / 2.0 = 5.0)
    // RED - Méthode division() inexistante
    // Etape 17 : Implémenter division() basique avec des doubles
    // GREEN - return a / b;

    // Règle classique : interdiction de diviser par zéro
    // Etape 18 : Écrire un test vérifiant qu'une ArithmeticException est levée pour division par 0
    // RED - Aucune vérification de division par zéro
    // Etape 19 : Ajouter la vérification de division par zéro
    // GREEN - if (b == 0.0) throw new ArithmeticException(...)

    // Nouvelle règle pour division : les résultats doivent être arrondis à 2 décimales
    // Etape 20 : Écrire un test avec une division donnant plusieurs décimales (ex: 10/3)
    // RED - Aucun arrondi n'est appliqué
    // Etape 21 : Implémenter l'arrondi à 2 décimales dans division()
    // GREEN - Utiliser Math.round() ou BigDecimal pour l'arrondi
}
