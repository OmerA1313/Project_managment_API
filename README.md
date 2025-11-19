# Project Management API

A Spring Boot–based REST API that provides project and task management features, including pagination, error handling and DTO mapping.

---

## 📌 Overview

This API allows clients to:

- Create, read, update, and delete **Projects**
- Create, read, update, and delete **Tasks** under specific projects
- Retrieve paginated lists of projects and tasks
- Receive structured errors and validation feedback
- Use DTO-based responses to avoid leaking internal entity structure

This README describes all endpoints, input/output formats, and expected behavior.

---

## 📂 Base URL (on dev environment)

```
http://localhost:8080
```

---

# 🧱 Project Endpoints — `/projects`

## ➕ Create Project

**POST** `/projects`

Creates a new project.

### Request Body (JSON)

```json
{
  "name": "New Project",
  "description": "A sample project"
}
```

### Response (201)

```json
{
  "id": 1,
  "name": "New Project",
  "description": "A sample project",
  "tasks": []
}
```

---

## 🔍 Get Project by ID

**GET** `/projects/{id}`

### Response (200)

```json
{
  "id": 1,
  "name": "New Project",
  "description": "A sample project",
  "tasks": []
}
```

### Errors

- **404 Not Found** — project does not exist  
  `{ "message": "Project not found with id X" }`

---

## 📄 Get All Projects (Paginated)

**GET** `/projects?page={page}&size={size}`

Default: `page=0`, `size=10`

### Response (200)

```json
{
  "items": [
    { "id": 1, "name": "Project A", "description": "Desc" }
  ],
  "page": 0,
  "size": 10,
  "totalItems": 1
}
```

---

## ✏ Update Project

**PUT** `/projects/{id}`

### Request Body

```json
{
  "name": "Updated Name",
  "description": "Updated description"
}
```

### Response (200)

```json
{
  "id": 1,
  "name": "Updated Name",
  "description": "Updated description"
}
```

---

## ❌ Delete Project

**DELETE** `/projects/{id}`

### Response (200)

Empty body.

### Errors

- **404 Not Found**

---

# 📝 Task Endpoints — `/projects/{projectId}/tasks` and `/tasks/{taskId}`

## ➕ Create Task under Project

**POST** `/projects/{projectId}/tasks`

### Request Body

```json
{
  "title": "Design database",
  "description": "Create schema",
  "status": "IN_PROGRESS"
}
```

If no status is provided, default = `"TODO"`.

### Response (201)

```json
{
  "id": 5,
  "title": "Design database",
  "description": "Create schema",
  "status": "IN_PROGRESS",
  "projectId": 1
}
```

### Errors

- **404 Project Not Found**

---

## 🔍 Get Task by ID

**GET** `/tasks/{taskId}`

### Response (200)

```json
{
  "id": 5,
  "title": "Design database",
  "description": "Create schema",
  "status": "IN_PROGRESS",
  "projectId": 1
}
```

---

## 📄 Get Tasks for a Project (Paginated)

**GET** `/projects/{projectId}/tasks?page={page}&size={size}`

### Response Example

```json
{
  "items": [
    {
      "id": 5,
      "title": "Design API",
      "description": "Define endpoints",
      "status": "TODO",
      "projectId": 1
    }
  ],
  "page": 0,
  "size": 10,
  "totalItems": 1
}
```

---

## ✏ Update Task

**PUT** `/tasks/{taskId}`

### Request Body

```json
{
  "title": "Updated title",
  "description": "Updated desc",
  "status": "DONE"
}
```

### Response

```json
{
  "id": 5,
  "title": "Updated title",
  "description": "Updated desc",
  "status": "DONE",
  "projectId": 1
}
```

---

## ❌ Delete Task

**DELETE** `/tasks/{taskId}`

### Response

`200 OK` (empty body)

### Errors

- **404 Task Not Found**

---

# ⚠ Error Response Format

All errors return the same structure:

```json
{
  "timestamp": "2025-01-01T12:00:00",
  "status": 404,
  "message": "Task not found",
  "path": "/tasks/99"
}
```

---

# 🚀 Deployment Recommendation

For handling **10k daily users**:

### Backend
- Package as Docker image
- Use AWS ECS Fargate or AWS Elastic Beanstalk  
- Include autoscaling policies

### Database
- Use AWS RDS
- Enable automated backups + Multi-AZ

### Observability
- Use CloudWatch for logs and metrics

---



