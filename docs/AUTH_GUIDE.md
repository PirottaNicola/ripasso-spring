# Authentication Guide

## Overview

The project implements JWT (JSON Web Token) based authentication with refresh token mechanism. It uses Spring Security and follows a stateless authentication pattern.

## Components

### 1. Security Configuration (`SecurityConfig.java`)

- Configures Spring Security
- Defines public endpoints (`/api/auth/**`, `/h2/**`)
- Sets up JWT filter and authentication provider
- Configures password encoding using BCrypt

### 2. JWT Service (`JwtService.java`)

Handles all JWT-related operations:

- Generates access tokens (30 minutes validity)
- Generates refresh tokens (7 days validity)
- Validates tokens
- Extracts user information from tokens

### 3. JWT Authentication Filter (`JwtAuthenticationFilter.java`)

- Intercepts every request
- Extracts JWT from Authorization header
- Validates token
- Sets up Spring Security context if token is valid

### 4. Authentication Flow

#### Login Flow:

1. Client sends POST request to `/api/auth/authenticate` with:

```json
{
  "email": "user@example.com",
  "password": "password"
}
```

2. Server responds with:

```json
{
  "accessToken": "eyJhbGciOiJ...",
  "refreshToken": "eyJhbGciOiJ..."
}
```

#### Protected Endpoint Access:

1. Include access token in request header:

```
Authorization: Bearer eyJhbGciOiJ...
```

#### Token Refresh:

When access token expires, send POST to `/api/auth/refresh-token` with:

```json
{
  "refreshToken": "eyJhbGciOiJ..."
}
```

## Usage Examples

### 1. Authentication

```bash
curl -X POST http://localhost:8080/api/auth/authenticate \
-H "Content-Type: application/json" \
-d '{
    "email": "user@example.com",
    "password": "password"
}'
```

### 2. Accessing Protected Endpoint

```bash
curl http://localhost:8080/api/users \
-H "Authorization: Bearer eyJhbGciOiJ..."
```

### 3. Refreshing Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh-token \
-H "Content-Type: application/json" \
-d '{
    "refreshToken": "eyJhbGciOiJ..."
}'
```

## Token Lifetimes

- Access Token: 30 minutes
- Refresh Token: 7 days

## Security Considerations

1. Store tokens securely on client side
2. Never share refresh tokens
3. Use HTTPS in production
4. Access token is short-lived for security
5. Refresh token is long-lived for better UX

## Error Handling

Common error responses:

1. Invalid credentials:

```json
{
  "error": "Bad credentials"
}
```

2. Invalid token:

```json
{
  "error": "Invalid refresh token"
}
```

3. Expired token:

```json
{
  "error": "JWT expired"
}
```

## Protected Endpoints

All endpoints except the following require authentication:

- `/api/auth/**`
- `/h2/**`

## Implementation Files

- `SecurityConfig.java`: Security configuration
- `JwtService.java`: JWT operations
- `JwtAuthenticationFilter.java`: Request interception
- `AuthenticationService.java`: Authentication logic
- `AuthenticationController.java`: Authentication endpoints
