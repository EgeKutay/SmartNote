# Smart Note Application

A **lightweight** and **intelligent** note-taking app that automatically categorizes your text based on your input by sending your input to GeminiAI. Also, if you haven't put title it generates title for you based on text.
When tokens runs out it uses simple inbuild algorithm to decide categorization based on some keywords inside the text. 

---
<img width="1280" height="720" alt="image" src="https://github.com/user-attachments/assets/f0c4f167-7106-4ae9-b424-470c87b7bb93" />

## Prerequisites

- Java 17+
- PostgreSQL 18 (database: `smartnote`)
- Node.js + pnpm

---

## Running the Backend

```bash
cd backend
./mvnw spring-boot:run
```

On Windows:

```bash
cd backend
mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080`.

---

## Running the Frontend

```bash
cd frontend
pnpm install
pnpm run dev
```

The frontend starts on `http://localhost:5173`.
