export interface Book {
    id: string;
    titre: string;
    auteur: string;
    isbn: string;
    datePublication: string;
}

export interface BookCandidate {
    titre: string;
    auteur: string;
    isbn: string;
    datePublication: string;
}