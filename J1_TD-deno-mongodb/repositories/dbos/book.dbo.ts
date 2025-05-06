import { ObjectId } from "../../deps.ts";
import { Book } from "../../services/models/book.model.ts";

export interface BookDBO {
    _id: ObjectId;
    titre: string;
    auteur: string;
    isbn: string;
    datePublication: string;
}

export function fromBookDboToBook(dbo: BookDBO): Book {
    return {
        titre: dbo.titre,
        auteur: dbo.auteur,
        isbn: dbo.isbn,
        datePublication: dbo.datePublication,
        id: dbo._id.toString()
    }
}

export function fromBookToBookDbo(book: Book): BookDBO {
    return {
        titre: book.titre,
        auteur: book.auteur,
        isbn: book.isbn,
        datePublication: book.datePublication,
        _id: new ObjectId(book.id)
    }
}
