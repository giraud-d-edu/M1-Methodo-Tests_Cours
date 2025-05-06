import * as bookRepository from "../repositories/book.repository.ts";
import { Book, BookCandidate } from "./models/book.model.ts";

export const getAllBooksService = async (): Promise<Book[]> => {
    return await bookRepository.getAllBooks();
};

export const getBookByIdService = async (id: string): Promise<Book> => {
    return await bookRepository.getBookById(id);
};

export const createBookService = async (bookCandidate: BookCandidate): Promise<Book> => {
    return await bookRepository.createBook(bookCandidate);
};

export const updateBookService = async (book: Book): Promise<Book> => {

    // if (book.isbn.length !== 13) {
    //     throw new Error("ISBN must be 13 digits");
    // }

    // Pour le mock
    //
    // const existingBook = await bookRepository.getBookById(book.id);
    // if (!existingBook) {
    //     throw new Error(`Book with id ${book.id} not found`);
    // }
    return await bookRepository.updateBook(book);
};

export const deleteBookService = async (id: string): Promise<boolean> => {
    return await bookRepository.deleteBook(id);
};