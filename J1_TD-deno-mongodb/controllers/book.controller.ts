import { Router, Context, RouterContext } from '../deps.ts';
import * as bookService from '../services/book.service.ts';
import {
    BookDTO,
    BookCandidateDTO,
    fromBookToDto,
    fromBookCandidateDTOToBookCandidate,
    fromDtoToBook
} from './dtos/book.dto.ts';
import { NotFoundException } from "../utils/exceptions.ts";
import { Book } from "../services/models/book.model.ts";

const router = new Router();

router
    .get('/books', getAllBooksController)
    .get('/books/:id', getBookByIdController)
    .post('/books', createBookController)
    .put('/books', updateBookController)
    .delete('/books/:id', deleteBookController);

async function getAllBooksController(ctx: Context) {
    ctx.response.body = (await bookService.getAllBooksService()).map(book => fromBookToDto(book));
}

async function getBookByIdController(ctx: RouterContext<"/books/:id">) {
    const idParam = ctx.params.id;
    if (!idParam) {
        ctx.response.status = 400;
        ctx.response.body = { error: "Missing book ID" };
        return;
    }

    try {
        ctx.response.body = fromBookToDto(await bookService.getBookByIdService(idParam));
    } catch (error) {
        if (error instanceof NotFoundException) {
            ctx.response.status = 404;
            ctx.response.body = { error: "Book not found" };
        } else {
            console.error("Error getting book by ID:", error);
            ctx.response.status = 500;
            ctx.response.body = { error: "Internal server error" };
        }
    }
}

async function createBookController(ctx: Context) {
    try {
        if (!ctx.request.hasBody) {
            ctx.response.status = 400;
            ctx.response.body = { error: "Request body is required" };
            return;
        }
        const body = await ctx.request.body({ type: 'json' }).value;
        const { titre, auteur, isbn, datePublication } = body as BookCandidateDTO;

        if (!titre || !auteur || !isbn || !datePublication) {
            ctx.response.status = 400;
            ctx.response.body = { error: "Missing required fields (titre, auteur, isbn, datePublication)" };
            return;
        }

        const bookCandidateDTO: BookCandidateDTO = { titre, auteur, isbn, datePublication };
        const bookCandidate = fromBookCandidateDTOToBookCandidate(bookCandidateDTO);
        ctx.response.status = 201;
        ctx.response.body = fromBookToDto(await bookService.createBookService(bookCandidate));
    } catch (e) {
        console.error("Error creating book:", e);
        ctx.response.status = 500;
        ctx.response.body = { error: "Internal server error" };
    }
}

async function updateBookController(ctx: Context) {

    if (!ctx.request.hasBody) {
        ctx.response.status = 400;
        ctx.response.body = { error: "Request body is required" };
        return;
    }
    const body = await ctx.request.body({ type: 'json' }).value;
    const { id, titre, auteur, isbn, datePublication } = body as BookDTO;

    if (!id || !titre || !auteur || !isbn || !datePublication) {
        ctx.response.status = 400;
        ctx.response.body = { error: "Missing required fields for update (titre, auteur, isbn, datePublication)" };
        return;
    }

    const bookDTO: BookDTO = { id, titre, auteur, isbn, datePublication };
    const book: Book = fromDtoToBook(bookDTO);
    const updatedBookDto = fromBookToDto(await bookService.updateBookService(book));
    if (updatedBookDto) {
        ctx.response.status = 200;
        ctx.response.body = { message: "Book updated successfully", body: updatedBookDto };
    } else {
        ctx.response.status = 404;
        ctx.response.body = { error: "Book not found" };
    }
}

async function deleteBookController(ctx: RouterContext<"/books/:id">) {
    const idParam = ctx.params.id;
    if (!idParam) {
        ctx.response.status = 400;
        ctx.response.body = { error: "Missing book ID" };
        return;
    }

    const success = await bookService.deleteBookService(idParam);
    if (success) {
        ctx.response.status = 204;
        ctx.response.body = null;
    } else {
        ctx.response.status = 404;
        ctx.response.body = { error: "Book not found" };
    }
}

export default router;