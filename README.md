# Library Management System (LMS)

A Spring Boot–based Library Management System with role-based access, session-based authentication for UI, JWT-secured APIs, and PostgreSQL as the database.  
This project focuses on clean backend logic, real-world library workflows, and gradual feature expansion.

---

## Tech Stack

- Java (Spring Boot)
- Spring MVC
- Spring Data JPA (Hibernate)
- Spring Security
- Thymeleaf (UI)
- PostgreSQL
- Maven
- Bootstrap (UI styling)
---

## Core Features

### User & Admin Roles
- Two roles: **ADMIN** and **USER**
- Separate navigation bars for admin and user (Thymeleaf templates)
- Role-based access control using Spring Security

---

### Authentication & Security

#### UI Security
- Session-based authentication for UI (Thymeleaf)
- CSRF protection is **disabled for UI**
- Login/logout handled via Spring Security sessions

#### API Security
- External API endpoints are secured using **JWT authentication**
- UI does **not** use JWT (purely session-based)

---

### Book Management

- Add, update, view, archive, and restore books
- **Soft delete implemented using an `active` flag**
  - `active = true` → book is available
  - `active = false` → book is archived (inactive)
- Archiving does not delete records from the database
- Archived books can be restored

---

### Borrowing System

- Users can borrow and return books
- Each borrow record contains:
  - `borrowDate`
  - `dueDate`
  - `returnDate` (if returned)
- Borrow status is tracked properly
- A book cannot be borrowed if:
  - It is already borrowed
  - It is archived (inactive)

---

### Borrow Records

- Borrow history is maintained
- Admins can view borrow logs
- Borrow records are linked to users and books
- Due date logic is implemented at the record level

---

### Pagination, Sorting, and Filtering

- Pagination is implemented **only on the user-side book dashboard**
- Sorting and filtering are applied selectively where needed
- Admin views currently prioritize functionality over UI polish

---

### Database

- Uses **PostgreSQL** 
- Entities are mapped using JPA
- Relationships are properly maintained between:
  - Users
  - Books
  - Borrow Records

---

### Transaction Management

- `@Transactional` is used where data consistency is required
- Ensures atomic operations for borrow/return and book state changes

---

## Project Structure

The project follows a standard Spring Boot layered architecture:

- Controller layer for request handling
- Service layer for business logic
- Repository layer for database interaction
- Security configuration separated from business logic
- Templates structured clearly for admin and user views
  
---
  
## Known Limitations

- UI requires polish and better consistency across admin views
- Pagination is not implemented everywhere
- No advanced analytics or reporting yet
- No email or notification system
- API documentation (Swagger/OpenAPI) not added yet

This project focuses more on backend correctness and architecture than UI perfection.

---

## Possible Future Iterations

- UI redesign with better responsiveness and consistency
- Pagination and filtering across all dashboards
- Fine-grained permissions (beyond USER / ADMIN)
- Book reservation system
- Overdue tracking and penalty calculation
- API documentation using Swagger
- Caching frequently accessed data
- Deployment configuration (Docker / cloud setup)
---

## How to Run the Project

1. Clone the repository
2. Configure PostgreSQL credentials in `application.properties`
3. Run the application using: mvn spring-boot:run
5. Access the application via browser

