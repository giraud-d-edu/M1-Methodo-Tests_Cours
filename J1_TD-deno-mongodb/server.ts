import { Application } from './deps.ts';
import bookRouter from './controllers/book.controller.ts';
import { connectDB } from "./repositories/database.ts";

await connectDB();

const app = new Application();

// Middleware pour gérer les erreurs
app.use(async (ctx, next) => {
    try {
        await next();
    } catch (err) {
        ctx.response.status = err.status || 500;
        ctx.response.body = { message: err.message };
        console.error(err);
    }
});

app.use(bookRouter.routes());
app.use(bookRouter.allowedMethods());

const port = 8000;
console.log(`Server running on port ${port}`);
await app.listen({ port });