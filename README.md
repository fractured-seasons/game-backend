# Fractured Seasons Website Backend

This repository contains the RESTful API backend for the Fractured Seasons website. It provides secure authentication, user and admin management, community forum and wiki, support ticketing, file uploads, and update feeds to power the Fractured Seasons ecosystem.

---

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Prerequisites](#prerequisites)
* [Getting Started](#getting-started)
* [Configuration](#configuration)
* [Running the Application](#running-the-application)
* [API Reference](#api-reference)

    * [CSRF](#csrf)
    * [Authentication](#authentication)
    * [OAuth2 Login](#oauth2-login)
    * [Forum](#forum)
    * [Wiki](#wiki)
    * [Support Tickets & Comments](#support-tickets--comments)
    * [Updates](#updates)
    * [File Uploads](#file-uploads)
* [Security](#security)
* [Database Migrations](#database-migrations)
* [Elasticsearch installation](#elasticsearch-installation)
* [Testing](#testing)
* [Contributing](#contributing)

---

## Features

* **JWT & OAuth2 Authentication** (Google, GitHub)
* **CSRF Protection** via X-XSRF-TOKEN header
* **Role-Based Access Control** (User, Wiki Contributor, Support, Moderator, Admin)
* **User Profile Management**: view, update, delete
* **Admin Endpoints**: full user lifecycle management
* **Elasticsearch**: search for forum topics and wiki article using one of the best engines
* **Community Forum**: categories, sections, topics, threaded replies, markdown support
* **Collaborative Wiki**: categories, articles with versioning, approval workflow, contributor list
* **Support Ticketing**: ticket creation, status tracking, timeline view, comments
* **Updates Feed**: changelog entries, version filters, chronological ordering
* **File Uploads**: image uploading endpoint

## Tech Stack

* **Language**: Java 17
* **Framework**: Spring Boot, Spring Security, Spring Data JPA
* **Database**: PostgreSQL
* **Build Tool**: Maven
* **Authentication**: JWT Bearer, OAuth2 Client
* **Frontend Integration**: Exposes REST endpoints consumed by a React + Tailwind UI

## Prerequisites

* Java 17+
* Maven 3.6+
* PostgreSQL 12+
* (Optional) Docker & Docker Compose

## Getting Started

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-org/fractured-seasons-backend.git
   cd fractured-seasons-backend
   ```
2. **Configure environment variables** (see [Configuration](#configuration))
3. **Build the project**

   ```bash
   mvn clean install
   ```
4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

By default, the API is available at `http://localhost:8080/api`.

## Configuration

Configure either an `.env` file or system environment variables:

```properties
spring.application.name=backend
spring.datasource.url=jdbc:postgresql://localhost:5432/game?currentSchema=game_schema
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
# ...
spring.app.jwtSecret=${JWT_SECRET}
spring.app.jwtExpirationMs=${JWT_EXPIRATION}
frontend.url=${FRONTEND_URL}
# Mail settings
spring.mail.host=sandbox.smtp.mailtrap.io
# OAuth2 GitHub and Google
# File uploading
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
spring.web.resources.static-locations=file:uploads/
```

## Running the Application

* **Locally**: `mvn spring-boot:run`
* **Docker**: `docker-compose up --build`

## API Reference

### CSRF

| Endpoint                  | Method | Description                                     |
| ------------------------- | ------ | ----------------------------------------------- |
| **GET** `/api/csrf-token` | GET    | Fetch CSRF token cookie for subsequent requests |

### Authentication

| Endpoint                                         | Method | Description                          |
| ------------------------------------------------ | ------ | ------------------------------------ |
| `POST` `/api/auth/public/signup`                 | POST   | Register new user                    |
| `POST` `/api/auth/public/signin`                 | POST   | Login with username/email & password |
| `POST` `/api/auth/public/logout`                 | POST   | Logout user (invalidate token)       |
| `POST` `/api/auth/public/refresh-token/username` | POST   | Refresh JWT by username              |
| `POST` `/api/auth/public/refresh-token/email`    | POST   | Refresh JWT by email                 |
| `POST` `/api/auth/public/reset-password`         | POST   | Reset password with token link       |
| `POST` `/api/auth/public/forgot-password`        | POST   | Request password reset email         |
| `GET`  `/api/auth/public/check-auth`             | GET    | Check authentication status          |
| `GET`  `/api/auth/username`                      | GET    | Fetch current authenticated username |
| `GET`  `/api/auth/user`                          | GET    | Fetch current user details           |
| `POST` `/api/auth/user/update`                   | POST   | Update user profile                  |
| `POST` `/api/auth/user/change-password`          | POST   | Change current user password         |
| `POST` `/api/auth/user/delete`                   | POST   | Delete current user account          |
| `POST` `/api/auth/user/deactivate`               | POST   | Deactivate current user              |
| `POST` `/api/auth/user/activate`                 | POST   | Activate a user (admin only)         |

### OAuth2 Login

| Endpoint                               | Method | Description                              |
| -------------------------------------- | ------ | ---------------------------------------- |
| `GET`  `/oauth2/authorize/{provider}`  | GET    | Initiate OAuth2 login (google, facebook) |
| `GET`  `/login/oauth2/code/{provider}` | GET    | OAuth2 callback endpoint                 |

### User Profile (ROLE\_USER)

| Endpoint                 | Method | Description                 |
| ------------------------ | ------ | --------------------------- |
| `GET`  `/api/users/me`   | GET    | Get current user profile    |
| `PUT`  `/api/users/me`   | PUT    | Update current user profile |
| `DELETE` `/api/users/me` | DELETE | Delete current user account |

### Admin Management (ROLE\_ADMIN)

| Endpoint                                   | Method | Description             |
| ------------------------------------------ | ------ | ----------------------- |
| `GET`  `/api/admin/users`                  | GET    | List all users          |
| `GET`  `/api/admin/user/{userId}`          | GET    | Get user by ID          |
| `PUT`  `/api/admin/user/edit/{userId}`     | PUT    | Edit user details by ID |
| `PUT`  `/api/admin/update-role`            | PUT    | Update user role        |
| `PUT`  `/api/admin/deactivate`             | PUT    | Deactivate a user       |
| `DELETE` `/api/admin/user/delete/{userId}` | DELETE | Delete user by ID       |

### Forum

#### Categories

| Endpoint                                    | Method | Description                       |
| ------------------------------------------- | ------ | --------------------------------- |
| `GET`  `/api/forum/category`                | GET    | List all forum categories         |
| `POST` `/api/forum/category/create`         | POST   | Create a new category (mod/admin) |
| `GET`  `/api/forum/category/{categoryId}`   | GET    | Get category by ID                |
| `PUT`  `/api/forum/category/{categoryId}`   | PUT    | Update category by ID             |
| `DELETE` `/api/forum/category/{categoryId}` | DELETE | Delete category by ID             |

#### Sections

| Endpoint                                  | Method | Description                    |
| ----------------------------------------- | ------ | ------------------------------ |
| `GET`  `/api/forum/section`               | GET    | List all forum sections        |
| `POST` `/api/forum/section/create`        | POST   | Create new section (mod/admin) |
| `GET`  `/api/forum/section/{sectionId}`   | GET    | Get section by ID              |
| `PUT`  `/api/forum/section/{sectionId}`   | PUT    | Update section by ID           |
| `DELETE` `/api/forum/section/{sectionId}` | DELETE | Delete section by ID           |

#### Topics

| Endpoint                              | Method | Description            |
| ------------------------------------- | ------ | ---------------------- |
| `GET`  `/api/forum/topic`             | GET    | List all topics        |
| `POST` `/api/forum/topic/create`      | POST   | Create new topic       |
| `GET`  `/api/forum/topic/{topicId}`   | GET    | Get topic by ID        |
| `PUT`  `/api/forum/topic/{topicId}`   | PUT    | Update topic by ID     |
| `DELETE` `/api/forum/topic/{topicId}` | DELETE | Delete topic by ID     |
| `GET`  `/api/forum/topic/search?q=`   | GET    | Search topics by query |

#### Replies

| Endpoint                              | Method | Description          |
| ------------------------------------- | ------ | -------------------- |
| `GET`  `/api/forum/reply`             | GET    | List all replies     |
| `POST` `/api/forum/reply/add`         | POST   | Add reply to a topic |
| `PUT`  `/api/forum/reply/{replyId}`   | PUT    | Update reply by ID   |
| `DELETE` `/api/forum/reply/{replyId}` | DELETE | Delete reply by ID   |

### Wiki

#### Categories

| Endpoint                                   | Method | Description                        |
| ------------------------------------------ | ------ | ---------------------------------- |
| `GET`  `/api/wiki/category`                | GET    | List all wiki categories           |
| `POST` `/api/wiki/category/create`         | POST   | Create new category (contributors) |
| `GET`  `/api/wiki/category/{categoryId}`   | GET    | Get category by ID                 |
| `PUT`  `/api/wiki/category/{categoryId}`   | PUT    | Update category by ID              |
| `DELETE` `/api/wiki/category/{categoryId}` | DELETE | Delete category by ID              |
| `GET`  `/api/wiki/category/approved`       | GET    | List all approved categories       |

#### Articles

| Endpoint                                                 | Method | Description                               |
| -------------------------------------------------------- | ------ | ----------------------------------------- |
| `GET`  `/api/wiki/article`                               | GET    | List all articles                         |
| `POST` `/api/wiki/article/create`                        | POST   | Propose/create new article (contributors) |
| `GET`  `/api/wiki/article/{articleSlug}`                 | GET    | Fetch article details                     |
| `PUT`  `/api/wiki/article/{articleId}`                   | PUT    | Edit/update article (mod/admin)           |
| `DELETE` `/api/wiki/article/{articleId}`                 | DELETE | Remove article (mod/admin)                |
| `PUT`  `/api/wiki/article/{articleId}/toggle-visibility` | PUT    | Toggle visibility (publish/unpublish)     |
| `PUT`  `/api/wiki/article/{articleId}/approve`           | PUT    | Approve a pending article                 |
| `PUT`  `/api/wiki/article/{articleId}/reject`            | PUT    | Reject a pending article                  |
| `GET`  `/api/wiki/article/search?q=`                     | GET    | Search articles                           |
| `GET`  `/api/wiki/article/pending`                       | GET    | List pending articles                     |
| `GET`  `/api/wiki/article/approved`                      | GET    | List approved articles                    |

#### Contributors

| Endpoint                               | Method | Description                      |
| -------------------------------------- | ------ | -------------------------------- |
| `GET`  `/api/wiki/{slug}/contributors` | GET    | List contributors for an article |
| `GET`  `/api/wiki/contributors`        | GET    | List all wiki contributors       |

### Support Tickets & Comments

| Endpoint                                 | Method | Description                                   |
| ---------------------------------------- | ------ | --------------------------------------------- |
| `POST` `/api/contact`                    | POST   | Submit a support ticket/message               |
| `GET`  `/api/contact`                    | GET    | List all contact submissions                  |
| `GET`  `/api/contact/{contactId}`        | GET    | Get a specific contact submission             |
| `GET`  `/api/tickets`                    | GET    | List your tickets (ROLE\_SUPPORT/User)        |
| `GET`  `/api/tickets/{ticketId}`         | GET    | View ticket details                           |
| `PUT`  `/api/tickets/{ticketId}`         | PUT    | Update ticket status or add comment (support) |
| `POST` `/api/comments/add`               | POST   | Add comment to ticket                         |
| `GET`  `/api/comments/ticket/{ticketId}` | GET    | List comments for a ticket                    |
| `DELETE` `/api/comments/{commentId}`     | DELETE | Delete a ticket comment                       |

### Updates

| Endpoint                          | Method | Description                |
| --------------------------------- | ------ | -------------------------- |
| `GET`  `/api/update`              | GET    | List all changelog entries |
| `POST` `/api/update/create`       | POST   | Create new changelog entry |
| `PUT`  `/api/update/{updateId}`   | PUT    | Update a changelog entry   |
| `DELETE` `/api/update/{updateId}` | DELETE | Delete a changelog entry   |

### File Uploads

| Endpoint                    | Method | Description          |
| --------------------------- | ------ | -------------------- |
| `POST` `/api/uploads/image` | POST   | Upload an image file |

---

## Security

* **JWT Authentication**: Bearer tokens in `Authorization` header.
* **CSRF Protection**: Frontend must fetch and include `X-XSRF-TOKEN` for state-changing requests.
* **Role-Based Access Control**: Managed via Spring Security annotations and role hierarchy.
* **OAuth2 Login**: Google and Facebook integrations.

## Database Migrations

* **Development**: `spring.jpa.hibernate.ddl-auto=update`
* **Production**: Managed via Flyway migrations (in `src/main/resources/db/migration`).

## Elasticsearch installation

Run in Docker:

```bash
docker run -d --name es8155 -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" elasticsearch:8.15.5
```

## Testing

Run unit and integration tests:

```bash
mvn test
```

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/YourFeature`.
3. Commit your changes: `git commit -m "feat: description"`.
4. Push to your branch: `git push origin feature/YourFeature`.
5. Open a Pull Request.

---

You can also explore the full OpenAPI definition at `/v3/api-docs` (OAS 3.0).
