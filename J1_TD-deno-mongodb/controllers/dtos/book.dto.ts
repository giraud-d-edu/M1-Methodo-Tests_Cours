import { Book, BookCandidate } from "../../services/models/book.model.ts";

export interface BookDTO {
    id: string;
    titre: string;
    auteur: string;
    isbn: string;
    datePublication: string;
}

export interface BookCandidateDTO {
    titre: string;
    auteur: string;
    isbn: string;
    datePublication: string;
}

export function fromBookToDto(book: Book): BookDTO {
    return {
        ...book,
    };
}

export function fromDtoToBook(dto: BookDTO): Book {
    return {
        ...dto,
    };
}

export function fromBookCandidateDTOToBookCandidate(dto: BookCandidateDTO): BookCandidate {
    return {
        ...dto
    };
}