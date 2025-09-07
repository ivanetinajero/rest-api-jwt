# REST API con JWT en Spring Boot

Este proyecto implementa una API REST profesional con autenticación y autorización basada en JWT (JSON Web Token) usando Spring Boot. A continuación se describe el flujo completo de autenticación JWT, los componentes involucrados y ejemplos prácticos de uso.

---

## Flujo de Autenticación y Autorización JWT

### 1. Login y generación de token
- **Componente:** `AuthController` (`login`)
- **Descripción:** El usuario envía sus credenciales (usuario y contraseña) al endpoint `/api/auth/login`. Si son correctas, se genera un JWT.
- **Clase/Método:**
  - `AuthController.login(Login loginDTO)`
  - Utiliza `IUsuariosService.autenticar()` para validar credenciales.
  - Utiliza `JwtUtil.generateToken(Usuario usuario)` para crear el token.

### 2. Respuesta de login
- **Componente:** `AuthController`
- **Descripción:** Si el login es exitoso, se retorna el JWT y un mensaje. Si falla, se retorna un mensaje de error y HTTP 401.
- **Clase/Método:**
  - `AuthController.login()`
  - Retorna `ResponseEntity<AuthResponse>`

### 3. Envío de token en peticiones protegidas
- **Componente:** Cliente/API consumer
- **Descripción:** El cliente debe enviar el JWT en el header `Authorization: Bearer <token>` para acceder a endpoints protegidos.

### 4. Validación del token en cada petición
- **Componente:** `JwtAuthenticationFilter`
- **Descripción:** El filtro intercepta cada petición, extrae y valida el JWT. Si es válido, permite el acceso; si no, retorna HTTP 403.
- **Clase/Método:**
  - `JwtAuthenticationFilter.doFilterInternal()`
  - Utiliza `JwtUtil.validateToken()`

### 5. Obtención de claims y roles
- **Componente:** `JwtUtil`
- **Descripción:** Extrae claims personalizados (usuario, perfiles, sucursal, etc.) para establecer el contexto de seguridad.
- **Clase/Método:**
  - `JwtUtil.validateToken(token)`

### 6. Configuración de seguridad y rutas protegidas
- **Componente:** `SecurityConfig`
- **Descripción:** Define qué rutas requieren autenticación y qué roles pueden acceder a cada endpoint.
- **Clase/Método:**
  - `SecurityConfig.configure(HttpSecurity http)`

### 7. Manejo de errores y respuestas JSON
- **Componente:** `GlobalExceptionHandler`
- **Descripción:** Centraliza el manejo de errores, devolviendo respuestas JSON claras y profesionales.
- **Clase/Método:**
  - `GlobalExceptionHandler.handleValidationExceptions()`
  - `GlobalExceptionHandler.handleJwtException()`

---

## Ejemplos de Peticiones

### 1. Login
**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "maria",
  "password": "123456"
}
```
**Response (200):**
```json
{
  "token": "<JWT_TOKEN>",
  "message": "Login exitoso"
}
```
**Response (401):**
```json
{
  "token": null,
  "message": "Usuario o contraseña incorrectos"
}
```

### 2. Acceso a endpoint protegido
**Request:**
```http
GET /api/productos
Authorization: Bearer <JWT_TOKEN>
```
**Response (200):**
```json
[
  { "id": 1, "nombre": "Producto A", ... },
  { "id": 2, "nombre": "Producto B", ... }
]
```
**Response (403):**
```json
{
  "error": "Acceso denegado. Token inválido o sin permisos."
}
```

### 3. Error de validación
**Request:**
```http
POST /api/productos
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "nombre": ""
}
```
**Response (400):**
```json
{
  "errors": [
    {
      "field": "nombre",
      "message": "El nombre es obligatorio"
    }
  ]
}
```

---

## Recomendaciones de Seguridad

- Usa una clave secreta robusta y guárdala fuera del código fuente (variables de entorno).
- Configura el algoritmo de firma JWT (HS256, HS384, etc.) según tus necesidades.
- Define tiempos de expiración cortos para el token (`exp` claim).
- Valida siempre el token en cada petición y maneja los errores correctamente.
- No expongas información sensible en los claims del JWT.
- Usa HTTPS en producción para proteger los tokens en tránsito.
- Actualiza dependencias de seguridad y JWT regularmente.

---

## Componentes Clave
- `AuthController`: Login y generación de token.
- `JwtUtil`: Generación, validación y extracción de claims del token.
- `JwtAuthenticationFilter`: Validación de token en cada petición.
- `SecurityConfig`: Configuración de rutas y roles.
- `GlobalExceptionHandler`: Manejo profesional de errores y respuestas JSON.

---

## Referencia rápida
- **Login:** `/api/auth/login` → `AuthController.login()`
- **Token en header:** `Authorization: Bearer <token>`
- **Validación:** `JwtAuthenticationFilter` + `JwtUtil`
- **Errores:** `GlobalExceptionHandler`

---

Este README sirve como guía y referencia para entender y mantener el flujo JWT en tu API REST con Spring Boot.
