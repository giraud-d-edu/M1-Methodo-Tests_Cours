import { MongoClient, Database } from "../deps.ts";
import { BookDBO } from "./dbos/book.dbo.ts";

const MONGO_URL = Deno.env.get("MONGO_URL") || "mongodb://127.0.0.1:27017";
const DB_NAME = Deno.env.get("DB_NAME") || "td-deno-mongodb";

let client: MongoClient | null = null;
let db: Database | null = null;

export async function connectDB(): Promise<Database> {
    if (db) {
        return db;
    }
    client = new MongoClient();
    try {
        await client.connect(MONGO_URL);
        db = client.database(DB_NAME);
        console.log("Connecté à MongoDB ! Base de données:", DB_NAME);
        return db;
    } catch (error) {
        console.error("Erreur de connexion à MongoDB:", error);
        Deno.exit(1);
    }
}

export function getBooksCollection() {
    if (!db) {
        throw new Error("La base de données n'est pas connectée. Appelez connectDB() d'abord.");
    }
    return db.collection<BookDBO>("books");
}