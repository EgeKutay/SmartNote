# Smart Note Application

A **lightweight** and **intelligent** note-taking app that automatically categorizes your text based on your input. Perfect for staying organized and boosting productivity.

---

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
