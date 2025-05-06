import { assertEquals } from "../deps.ts";
import * as bookRepository from "../repositories/book.repository.ts";
import { updateBookService } from "./book.service.ts";
import {Book} from "./models/book.model.ts";

// Création d'un mock pour bookRepository.updateBook
Deno.test("updateBookService - Cas nominal", async () => {

    // Given
    const mockBook = {
        id: "1",
        titre: "Titre de test",
        auteur: "Auteur de test",
        isbn: "1234567890123",
        datePublication: "2023-01-01",
    };
    const updateBookMock = async (book: Book): Promise<Book | null> => {
        return new Promise(resolve => resolve(book));
    };
    const mockRepository = { ...bookRepository, updateBook: updateBookMock };

    // When
    const result = await updateBookService(mockBook, mockRepository);

    // Then
    assertEquals(result, mockBook);
});