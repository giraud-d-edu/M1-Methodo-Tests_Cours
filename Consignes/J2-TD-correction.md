# J2-TD Correction

## Niveau simple

### 📝 Exercice 1: Tests Unitaires de Base

**Fichier:** `src/test/java/com/ynov/testing/service/PlayerServiceTest.java`

**Rendu d'un étudiant** :

```java
    @Test
    @DisplayName("Should activate player successfully")
    void activatePlayer_WithValidId_ShouldActivatePlayer() {
        // Given
        Long playerId = 1L;Add commentMore actions
        savedPlayer.setActive(false); // Simulate inactive player
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        // When
        Player result = playerService.activatePlayer(playerId);

        // Then
        assertNotNull(result);
        assertTrue(result.getActive());
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should not change state when activating an already active player")
    void activatePlayer_AlreadyActive_ShouldRemainActive() {
        // Given
        Long playerId = 1L;
        savedPlayer.setActive(true); // Player is already active
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        // When
        Player result = playerService.activatePlayer(playerId);

        // Then
        assertNotNull(result);
        assertTrue(result.getActive(), "Player should remain active");
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(any(Player.class));
    }

```

**Problèmes Identifiés**


1. Mock incorrect du repository.save()
Le mock when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer) retourne toujours le même objet savedPlayer, peu importe ce qui est passé en paramètre. Cela ne reflète pas le comportement réel de la méthode save().

2. Réutilisation problématique de l'objet savedPlayer
L'objet savedPlayer est modifié entre les tests, ce qui peut créer des effets de bord.

3. Manque de test pour les cas d'erreur
Aucun test pour les cas où le joueur n'existe pas.

**Correction**

```java
@Test
@DisplayName("Should activate inactive player successfully")
void activatePlayer_WithInactivePlayer_ShouldActivatePlayer() {
    // Given
    Long playerId = 1L;
    Player inactivePlayer = new Player("John", "Doe", "john@example.com", 25, "Forward");
    inactivePlayer.setId(playerId);
    inactivePlayer.setActive(false); // Player is inactive
    
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(inactivePlayer));
    // CORRECTION: Mock save() pour retourner l'argument passé en paramètre
    when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> {
        Player playerToSave = invocation.getArgument(0);
        return playerToSave; // Retourne exactement ce qui est sauvegardé
    });

    // When
    Player result = playerService.activatePlayer(playerId);

    // Then
    assertNotNull(result);
    assertTrue(result.getActive(), "Player should be activated");
    assertEquals(playerId, result.getId());
    
    // Vérifier que save() est appelé avec un joueur activé
    verify(playerRepository).findById(playerId);
    verify(playerRepository).save(argThat(player -> 
        player.getId().equals(playerId) && player.getActive() == true
    ));
}

@Test
@DisplayName("Should remain active when activating already active player")
void activatePlayer_WithActivePlayer_ShouldRemainActive() {
    // Given
    Long playerId = 1L;
    Player activePlayer = new Player("Jane", "Smith", "jane@example.com", 23, "Midfielder");
    activePlayer.setId(playerId);
    activePlayer.setActive(true); // Player is already active
    
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(activePlayer));
    // CORRECTION: Même approche pour le mock save()
    when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> 
        invocation.getArgument(0)
    );

    // When
    Player result = playerService.activatePlayer(playerId);

    // Then
    assertNotNull(result);
    assertTrue(result.getActive(), "Player should remain active");
    assertEquals(playerId, result.getId());
    
    verify(playerRepository).findById(playerId);
    verify(playerRepository).save(argThat(player -> 
        player.getId().equals(playerId) && player.getActive() == true
    ));
}

@Test
@DisplayName("Should throw exception when player not found")
void activatePlayer_WithNonExistentId_ShouldThrowException() {
    // Given
    Long playerId = 999L;
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> playerService.activatePlayer(playerId)
    );
    
    assertEquals("Player not found with ID: " + playerId, exception.getMessage());
    verify(playerRepository).findById(playerId);
    verify(playerRepository, never()).save(any(Player.class));
}

@Test
@DisplayName("Should throw exception when player ID is null")
void activatePlayer_WithNullId_ShouldThrowException() {
    // Given
    Long playerId = null;

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> playerService.activatePlayer(playerId)
    );
    
    assertEquals("Player ID cannot be null", exception.getMessage());
    verify(playerRepository, never()).findById(any());
    verify(playerRepository, never()).save(any(Player.class));
}

```


### 📝 Exercice 2: Tests de Contrôleur Corrigés

**Fichier:** `src/test/java/com/ynov/testing/controller/PlayerControllerTest.java`

**Rendu étudiant** :

```java
    @Test
    @DisplayName("Should return paginated players with correct parameters")
    void getPlayers_WithPaginationParams_ShouldReturnPaginatedResult() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "ASC";

        List<Player> players = Arrays.asList(
                createPlayer(1L, "John", "Doe", "john@example.com", 25, "Forward"),
                createPlayer(2L, "Jane", "Smith", "jane@example.com", 27, "Midfielder"),
                createPlayer(3L, "Bob", "Johnson", "bob@example.com", 23, "Defender")
        );

        Page<Player> playerPage = new PageImpl<>(players,
                PageRequest.of(page, size),
                players.size());

        when(playerService.getAllPlayers(any(Pageable.class))).thenReturn(playerPage);

        mockMvc.perform(get("/api/players")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sortBy + "," + direction))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.number").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andDo(print());

        verify(playerService).getAllPlayers(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle invalid JSON input")
    void createPlayer_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidJson = "{\"name\": , \"email\": \"invalid-email\"}";

        // When & Then
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(playerService, never()).createPlayer(any());
    }

    @Test
    @DisplayName("Should verify CORS headers")
    void verifyCorsPolicies() throws Exception {
        // When & Then
        mockMvc.perform(options("/api/players")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods",
                        containsString("GET")))
                .andExpect(header().exists("Access-Control-Max-Age"))
                .andDo(print());
    }
```

Bien, mais il manque quelques cas limites et certaines assertions sont incomplètes

```java
    // AJOUT: Tests pour cas limites
    @Test
    @DisplayName("GET /api/players with negative page should return 400")
    void getAllPlayers_WithNegativePage_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/players")
                .param("page", "-1")
                .param("size", "10"))
            .andExpect(status().isBadRequest());
            
        verify(playerService, never()).getAllPlayers(any(Pageable.class));
    }
    
    @Test
    @DisplayName("GET /api/players with excessive page size should be limited")
    void getAllPlayers_WithExcessivePageSize_ShouldBeLimited() throws Exception {
        mockMvc.perform(get("/api/players")
                .param("page", "0")
                .param("size", "10000"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", containsString("Page size too large")));
    }
```


### 📝 Exercice 3: Tests d'Intégration Repository

**Fichier:** `src/test/java/com/ynov/testing/repository/PlayerRepositoryTest.java`

C'est une hérésie de tester cette couche, ne faites jamais ça !


### 📝 Exercice 4: Tests d'Intégration End-to-End

**Fichier:** `src/test/java/com/ynov/testing/integration/PlayerIntegrationTest.java`

Il vaut mieux partir sur des tests d'API, E2E pour ces cas-là pour se rapprocher des conditions de prod.
