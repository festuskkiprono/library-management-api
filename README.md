# library-management-api

# Library Management System API

A secure REST API for managing school library operations with JWT authentication, role-based access control, and shopping cart-style book borrowing. Built with Spring Boot, Spring Security, and JPA.

---

##  Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Architecture & Design](#architecture--design)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)

---

##  Project Overview

**Project Name:** School Library Management System  
**Status:** v0.0.1-SNAPSHOT (Active Development)

**Core Purpose:** A secure REST API backend for managing school library operations including book inventory, user management, borrowing transactions, and cart functionality. The system implements JWT-based authentication with role-based access control (RBAC) to ensure secure operations across different user types.

---

##  Key Features

-  **JWT Authentication** - Access/refresh token mechanism with HttpOnly cookies
-  **Role-Based Authorization** - USER, ADMIN, and LIBRARIAN roles
-  **Shopping Cart Workflow** - Intuitive book borrowing experience
-  **Transaction Tracking** - Complete borrowing history and overdue management
-  **Secure by Design** - BCrypt password encryption, XSS protection, stateless sessions

---

##  Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 4.0.0 |
| **Security** | Spring Security + JWT | Latest |
| **Database** | JPA/Hibernate | (via Spring Boot) |
| **Validation** | Jakarta Validation | Latest |
| **Mapping** | MapStruct | Spring |
| **Build Tool** | Maven | 3.x |

**Key Dependencies:**
- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-security` - Authentication & authorization
- `spring-boot-starter-web` - REST API
- `lombok` - Boilerplate code reduction
- `mapstruct` - DTO mapping

---

##  Architecture & Design

### API Design
**Type:** RESTful API following HTTP standards

**Authentication Flow:**
```
1. User → POST /auth/login (email + password)
2. Server → Returns access token (JWT) + refresh token (HttpOnly cookie)
3. Client → Includes access token in Authorization header for protected requests
4. Token expires → POST /auth/refresh with cookie to get new access token
```

### Architectural Pattern
**Layered Architecture (N-Tier):**
```
┌─────────────────────────────────────┐
│   Controllers (REST Endpoints)      │  ← HTTP requests
├─────────────────────────────────────┤
│   Services (Business Logic)         │  ← Transaction management
├─────────────────────────────────────┤
│   Repositories (Data Access)        │  ← JPA/Database
├─────────────────────────────────────┤
│   Entities (Domain Models)          │
└─────────────────────────────────────┘
```

**Flow Example (Book Borrowing):**
```
User adds books to cart → Cart persists items → User checks out cart 
→ BorrowTransactionService creates transaction → Books marked as borrowed 
→ Cart cleared → Transaction tracked with due dates
```

### Key Design Choices

**1. Security Implementation:**
- **JWT Filter Chain:** Custom `JwtAuthenticationFilter` intercepts requests before Spring Security's default filters
- **Stateless Sessions:** No server-side session storage (`SessionCreationPolicy.STATELESS`)
- **Role Prefix Convention:** Roles stored as `USER/ADMIN` but checked as `ROLE_USER/ROLE_ADMIN`

**2. Dependency Injection:**
```java
@AllArgsConstructor // Lombok generates constructor injection
public class AuthController {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    // Spring automatically injects dependencies
}
```

**3. Data Validation:**
- `@Valid` annotation on request DTOs
- Jakarta Validation constraints (`@NotNull`, `@Size`)
- Custom business logic validation in services

**4. Error Handling:**
```java
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<Void> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
```

**5. Security Features:**
- **HttpOnly Cookies:** Refresh tokens stored in cookies inaccessible to JavaScript (XSS protection)
- **BCrypt Password Encoding:** Passwords hashed before storage
- **CSRF Disabled:** Appropriate for stateless JWT APIs
- **Path-Based Cookie Scope:** Refresh token cookie only sent to `/auth/refresh` endpoint

---

##  Getting Started

### Prerequisites

- **JDK 17** or higher
- **Maven 3.6+**
- **Database** (PostgreSQL/MySQL recommended, H2 for testing)
- **Postman** (or similar API client for testing)

### Configuration

Create `application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.access-token-expiration=3600      # 1 hour in seconds
jwt.refresh-token-expiration=604800   # 7 days in seconds
jwt.secret=your-secret-key-here       # Use strong secret in production
```



### Running Tests
```bash
# Run all tests
mvn test



---

##  API Documentation

### Base URL
```
http://localhost:8080
```

### Core Endpoints

#### **Authentication**

##### 1. Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs..."
}
```
**Sets Cookie:** `refreshToken` (HttpOnly, Secure, 7-day expiration)

---

##### 2. Refresh Access Token
```http
POST /auth/refresh
Cookie: refreshToken=<token>
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

---

##### 3. Get Current User Profile
```http
GET /auth/me
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

#### **User Management**

##### 4. Register New User
```http
POST /users
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane@example.com",
  "password": "securePassword123"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane@example.com"
}
```
**Location Header:** `/users/2`

**Roles Required:** LIBRARIAN or ADMIN

---

##### 5. Get All Users
```http
GET /users?sort=name
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Alice Admin",
    "email": "alice@library.com"
  },
  {
    "id": 2,
    "name": "Bob User",
    "email": "bob@library.com"
  }
]
```

**Roles Required:** LIBRARIAN or ADMIN

---

#### **Cart Operations**

##### 6. Create Shopping Cart
```http
POST /carts
Authorization: Bearer <access_token>
```

**Response (201 Created):**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "items": []
}
```

---

##### 7. Add Book to Cart
```http
POST /carts/{cartId}/items
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "bookId": 42
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "bookId": 42,
  "copies": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin"
}
```

**Conflict (409):** Book already in cart (only 1 copy per book allowed)

---

##### 8. View Cart
```http
GET /carts/{cartId}
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "items": [
    {
      "id": 1,
      "bookId": 42,
      "title": "Clean Code",
      "copies": 1
    }
  ]
}
```

---

##### 9. Remove Book from Cart
```http
DELETE /carts/{cartId}/items/{bookId}
Authorization: Bearer <access_token>
```

**Response (204 No Content)**

---

##### 10. Clear Entire Cart
```http
DELETE /carts/{cartId}/items
Authorization: Bearer <access_token>
```

**Response (204 No Content)**

---

### Authorization Matrix

| Endpoint | USER | ADMIN |
|----------|------|-------|
| POST /auth/login | $\checkmark$ Public | $\checkmark$ Public |
| POST /auth/refresh | $\checkmark$ Public | $\checkmark$ Public |
| POST /auth/validate | $\checkmark$ Public | $\checkmark$ Public |
| GET /auth/me | $\checkmark$ | $\checkmark$ |
| POST /users | - | $\checkmark$ |
| GET /users | - | $\checkmark$ |
| GET /users/{id} | $\checkmark$ | $\checkmark$ |
| PUT /users/{id} | $\checkmark$ | $\checkmark$ |
| DELETE /users/{id} | - | $\checkmark$ |
| POST /users/{id}/change-password | $\checkmark$ | $\checkmark$ |
| POST /carts | $\checkmark$ | $\checkmark$ |
| GET /carts/{cartId} | $\checkmark$ | $\checkmark$ |
| POST /carts/{cartId}/items | $\checkmark$ | $\checkmark$ |
| DELETE /carts/{cartId}/items/{bookId} | $\checkmark$ | $\checkmark$ |
| DELETE /carts/{cartId}/items | $\checkmark$ | $\checkmark$ |
| POST /books | - | $\checkmark$ |
| GET /books | $\checkmark$ | $\checkmark$ |
| GET /books/{id} | $\checkmark$ | $\checkmark$ |
| PUT /books/{id} | - | $\checkmark$ |
| DELETE /books/{id} | - | $\checkmark$ |
| PUT /borrow/{id}/return | - | $\checkmark$ |

---

##  Database Schema Overview

**Key Entities:**
- **User:** id, name, email, password (hashed), role
- **Book:** id, title, author, isbn, genre, availableCopies, totalCopies
- **Cart:** id (UUID), items (one-to-many with CartItem)
- **CartItem:** id, cart, book, copies
- **BorrowTransaction:** id, user, borrowDate, dueDate, returnDate, status
- **BorrowedBook:** id, book, borrowTransaction (many-to-many join entity)

---

##  Security

### Important Security Configurations

 **Critical Security Features:**

1. **Refresh Token Storage:** Stored in HttpOnly cookies to prevent XSS attacks
2. **Password Encoding:** BCrypt with default strength (10 rounds)
3. **CORS:** Configured appropriately for production environments
4. **HTTPS:** Set `cookie.setSecure(true)` 
5. **Secret Key:** Use environment variables for JWT secrets 

### Production Checklist

- [ ] Enable HTTPS
- [ ] Configure CORS allowed origins
- [ ] Use strong JWT secret (256-bit minimum)
- [ ] Set appropriate token expiration times
- [ ] Enable database connection pooling


---

##  Testing

### Sample Postman Collection
[View Collection](https://web.postman.co/workspace/My-Workspace~e486209d-e3ba-4c7a-8f8f-7a5b3fde27c0/collection/38701568-7cfd93c6-3dd5-4caf-9a2d-79bde6fde7bf)

### Quick Test Flow
```bash
# 1. Register a user
POST /users
{"name": "Test User", "email": "test@test.com", "password": "test123"}

# 2. Login
POST /auth/login
{"email": "test@test.com", "password": "test123"}
# Copy access token from response

# 3. Create cart (use token in Authorization header)
POST /carts
Authorization: Bearer <token>

# 4. Add books to cart
POST /carts/{cartId}/items
{"bookId": 1}
```

---

##  Future Enhancements

- [ ] Implement overdue book notifications
- [ ] Add book reservation system
- [ ] Generate borrowing history reports
- [ ] Implement fine calculation for late returns
- [ ] Add email verification for registration
- [ ] Create admin dashboard endpoints
- [ ] Add pagination for large datasets
- [ ] Implement search and filtering for books
- [ ] Add rate limiting


---





---
