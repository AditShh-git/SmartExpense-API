# SmartExpense API 💸

**SmartExpense** is a secure, robust, and intelligent REST API for personal expense tracking. Built with Spring Boot, it allows users to manage their finances, set budgets, and gain insights into their spending habits.

This is a backend-only, portfolio project demonstrating a modern Java tech stack, secure API practices, and advanced business logic.

---

## ✨ Key Features

* **Secure Authentication:** Full user registration and login system using **Spring Security** and **JWT (JSON Web Tokens)**.
* **Secure Endpoints:** All endpoints are protected. Users can only create, read, update, or delete their *own* expenses and categories.
* **Data Aggregation:** A `GET /api/summary/monthly` endpoint that performs server-side calculations to return a full monthly report (total spending, top category, and a complete category breakdown).
* **Real-time Budget Alerts:** Users can set a monthly budget. When adding a new expense, the API checks the total spending in real-time and returns a `budgetWarning` if the user has exceeded their limit.
* **Advanced Filtering:** The `GET /api/expenses` endpoint can be dynamically filtered by date range or by category.
* **Custom Exception Handling:** A professional, global exception handler (`@RestControllerAdvice`) provides clean, safe, and consistent JSON error messages for all API errors (e.g., 404 Not Found, 400 Bad Request).

---

## 🛠️ Technology Stack

* **Framework:** Spring Boot
* **Security:** Spring Security, JWT
* **Database:** Spring Data JPA, PostgreSQL (or MySQL)
* **Testing:** H2 Database (for in-memory testing)
* **Build:** Maven
* **Utilities:** Lombok, `jakarta.validation`

---

## 🚀 API Endpoints

A high-level overview of the core API endpoints.

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user. |
| `POST` | `/api/auth/login` | Log in and receive a JWT. |
| `GET` | `/api/expenses` | Get all expenses (with filters). |
| `POST` | `/api/expenses` | Add a new expense. |
| `PUT` | `/api/expenses/{id}` | Update an expense. |
| `DELETE` | `/api/expenses/{id}` | Delete an expense. |
| `GET` | `/api/categories` | Get all user-created categories. |
| `POST` | `/api/categories` | Create a new category. |
| `PUT` | `/api/user/budget` | Set the user's monthly budget. |
| `GET` | `/api/summary/monthly` | Get the full summary for a given month. |

---

## 🔮 Future Implementation

* **Recurring Expenses:** A `@Scheduled` job to automatically add recurring expenses (e.g., "Netflix") every month.
* **Data Export:** A `GET /api/export/csv` endpoint to download a complete CSV file of monthly expenses.
