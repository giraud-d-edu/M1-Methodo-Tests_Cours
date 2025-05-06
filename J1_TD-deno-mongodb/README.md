# Deno MongoDB Book API

A REST API built with Deno and MongoDB for managing books.

## Prerequisites

- [Deno](https://deno.land/) (v1.32 or higher)
- [MongoDB](https://www.mongodb.com/try/download/community) (v4.4 or higher)

## Installation & Setup Steps

1. Create the database: Ensure MongoDB is running locally on port 27017. Then, create a database named td-deno-mongodb and a collection named books:

```
mongo
use td-deno-mongodb
db.createCollection("books")
```

2. Run the application in development mode:

```bash
deno run dev
```

3. Test the API using curl commands:

Get all books:
``` bash
curl -X GET http://localhost:8000/books
```

Ge[deno.lock](deno.lock)t a book by ID:
``` bash
curl -X GET http://localhost:8000/books/<id>
```

Create a new book:
``` bash
curl -X POST http://localhost:8000/books \
-H "Content-Type: application/json" \
-d '{"titre": "New Book", "auteur": "Author Name", "isbn": 42, "datePublication":"2024-01-01"}'
```

Update a book:
``` bash
curl -X PUT http://localhost:8000/books/<id> \
-H "Content-Type: application/json" \
-d '{"titre": "Updated Book", "auteur": "Updated Author",  "isbn": 42, "datePublication":"2024-01-01"}'
```

Delete a book:
``` bash
curl -X DELETE http://localhost:8000/books/<id>
```

4. Run tests:

```bash
deno task test
```