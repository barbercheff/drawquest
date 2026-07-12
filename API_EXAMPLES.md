# DrawQuest API examples

Examples assume the backend is running locally:

```powershell
$baseUrl = "http://localhost:8080"
```

Swagger is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Authentication

Register creates a regular `ROLE_USER` account.

```powershell
$registerBody = @{
  username = "alice"
  password = "secret123"
  email = "alice@example.com"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "$baseUrl/auth/register" `
  -Method Post `
  -ContentType "application/json" `
  -Body $registerBody
```

Login returns a JWT.

```powershell
$loginBody = @{
  username = "alice"
  password = "secret123"
} | ConvertTo-Json

$login = Invoke-RestMethod `
  -Uri "$baseUrl/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body $loginBody

$token = $login.token
$headers = @{ Authorization = "Bearer $token" }
```

## Current User

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/users/me" `
  -Method Get `
  -Headers $headers
```

`GET /users` and `POST /users` require `ROLE_ADMIN`.

## Quests

Listing quests requires an authenticated user.

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/quests" `
  -Method Get `
  -Headers $headers
```

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/quests/1" `
  -Method Get `
  -Headers $headers
```

Creating and updating quests require `ROLE_ADMIN` or `ROLE_MODERATOR`. Deleting quests requires `ROLE_ADMIN`.

```powershell
$questBody = @{
  title = "Draw a castle"
  description = "Create a castle with at least one tower."
  difficulty = 2
  xpReward = 120
} | ConvertTo-Json

$quest = Invoke-RestMethod `
  -Uri "$baseUrl/quests" `
  -Method Post `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $questBody

$questId = $quest.id
```

```powershell
$questUpdateBody = @{
  title = "Draw a moonlit castle"
  description = "Create a castle at night with at least one tower."
  difficulty = 3
  xpReward = 150
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "$baseUrl/quests/$questId" `
  -Method Put `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $questUpdateBody
```

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/quests/$questId" `
  -Method Delete `
  -Headers $headers
```

## Drawings

Creating a drawing requires a valid quest ID and a valid image URL.

```powershell
$drawingBody = @{
  questId = 1
  imageUrl = "https://example.com/drawings/castle.png"
} | ConvertTo-Json

$drawing = Invoke-RestMethod `
  -Uri "$baseUrl/drawings" `
  -Method Post `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $drawingBody

$drawingId = $drawing.id
```

Users only see their own drawings.

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/drawings" `
  -Method Get `
  -Headers $headers
```

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/drawings/$drawingId" `
  -Method Get `
  -Headers $headers
```

```powershell
$drawingUpdateBody = @{
  imageUrl = "https://example.com/drawings/castle-v2.png"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "$baseUrl/drawings/$drawingId" `
  -Method Put `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $drawingUpdateBody
```

Approving a drawing requires `ROLE_ADMIN` or `ROLE_MODERATOR`. Approval marks the quest progress as completed and awards XP only once for that user and quest.

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/drawings/$drawingId/approve" `
  -Method Put `
  -Headers $headers
```

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/drawings/$drawingId" `
  -Method Delete `
  -Headers $headers
```

## Progress

Users only see their own progress.

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/progress" `
  -Method Get `
  -Headers $headers
```

```powershell
Invoke-RestMethod `
  -Uri "$baseUrl/progress/1" `
  -Method Get `
  -Headers $headers
```

## Validation and Errors

Validation failures return `400` with `errorCode = VALIDATION_ERROR`.

```powershell
$invalidQuestBody = @{
  title = ""
  description = "Invalid quest"
  difficulty = 0
  xpReward = -1
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "$baseUrl/quests" `
  -Method Post `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $invalidQuestBody
```

Common error codes:

- `UNAUTHORIZED`: missing or invalid JWT.
- `FORBIDDEN`: authenticated user does not have the required role.
- `NOT_FOUND`: resource does not exist or does not belong to the authenticated user.
- `VALIDATION_ERROR`: request JSON is valid but one or more fields failed validation.
- `BAD_REQUEST`: malformed JSON.
- `DUPLICATE_RESOURCE`: username or email is already in use.

## Local Role Setup

The public registration endpoint creates regular users. To test admin or moderator endpoints locally, assign roles directly in MySQL.

```sql
SELECT id, username FROM users;
SELECT id, name FROM roles;
```

```sql
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'alice'
ON DUPLICATE KEY UPDATE user_id = user_id;
```

Use `ROLE_MODERATOR` instead of `ROLE_ADMIN` to test moderator permissions.
