# 📦 Blog Java com armazenamento em MongoDb

Este projeto é um blog desenvolvido em Java 21 que permite cadastro de usuários, criação, alteração e exclusão de posts e comentários  

## Funcionalidades

# 👤 Usuários

Este módulo gerencia operações relacionadas a usuários, incluindo cadastro, autenticação e consulta de informações.

---

### Endpoints


#### `POST` -> `/users/register`

Cria um novo usuário no sistema.

#### Request Body
```json
{
	"firstName":"example",
	"lastName":"example",
	"email":"teste@teste.com",
	"password":"12345678"
}
```

#### Response Body
```json
{
  "id": "8c90497d-98fe-4a77-8c3a-abe90a519727",
  "firstName": "example",
  "lastName": "example",
  "email": "teste@teste.com",
  "role": "CLIENT"
}
```

#### `POST` ->`/users/login`

Realiza login de usuário no sistema.

#### Request Body
```json
{
	"email":"teste@teste.com",
	"password":"12345678"
}
```

#### Response Body
```json
{
  "userId": "8c90497d-98fe-4a77-8c3a-abe90a519727",
  "token": "token jwt ...",
  "role": "CLIENT"
}
```
#### `GET` ->`/users/{id}}`

Consulta informações de usuário especificado pelo id no sistema.

# 👤 Posts

Este módulo gerencia operações relacionadas aos posts do blog.

---
### Endpoints

#### `POST` ->`/post`

Registra um post no blog atrelado a um usuário. 

#### Request Body
```json
{
  "userId":"8c90497d-98fe-4a77-8c3a-abe90a519727",
  "post":"teste"
}
```

#### `GET` ->`/post?userId=68cb524c3b168b03bd48c22f`

Consulta todos os post de um usuário do blog.

#### `GET` ->`/post/{id}`

Consulta post especificado por id.

#### `PUT` ->`/post/{id}`

Atuliza informação do post especificado por id.

#### Request Body
```json
{
  "post": "post updated"
}
```

#### `DELETE` ->`/post/{id}`

Deleta post especificado por id.

# 👤 Comentários

Este módulo gerencia operações relacionadas aos comentários do blog.

---
### Endpoints

#### `POST` ->`/comments`

Registra um comentário no blog atrelado a um post.

#### Request Body
```json
{
  "postId":"68cb524c3b168b03bd48c22f",
  "comment":"novo comentario"
}
```

#### `GET` ->`/comment?postId=68cb524c3b168b03bd48c22f`

Consulta todos os comentários de um post do blog.

#### `GET` ->`/comment/{id}`

Consulta comentário especificado por id.

#### `PUT` ->`/comment/{id}`

Atuliza informação do comentário especificado por id.

#### Request Body
```json
{
  "comment": "comment updated"
}
```

#### `DELETE` ->`/comment/{id}`

Deleta comentário especificado por id.

