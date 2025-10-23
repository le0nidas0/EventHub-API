# 🎉 EventHub API

## 📝 Descrição do Projeto

O **EventHub API** é um serviço RESTful desenvolvido em **Java 17** com **Spring Boot 3**, que simula uma plataforma completa de gerenciamento de eventos. O sistema suporta múltiplos perfis de usuários — **Comum** e **Organizador** — permitindo a criação de eventos e o registro de inscrições de forma segura e eficiente.

---

## 🔑 Tecnologias e Arquitetura

| Categoria       | Tecnologia                  | Função                                               |
|----------------|-----------------------------|------------------------------------------------------|
| Backend         | Java 17+, Spring Boot 3     | Núcleo da API, Lógica de Negócios                    |
| Segurança       | Spring Security, JWT        | Autenticação e Autorização Stateless                 |
| Persistência    | PostgreSQL (via Docker)     | Armazenamento de dados                               |
| Containerização | Docker, Docker Compose      | Empacotamento e orquestração do ambiente de dev      |

---

## 🚀 Guia de Execução

O projeto está totalmente containerizado. A forma mais rápida e recomendada de iniciar a aplicação e suas dependências é utilizando o `docker-compose.yml`.

### ✅ Pré-requisitos

- Docker e Docker Compose instalados
- Maven (CLI ou IDE com suporte) para gerar o `.jar`

### 🔨 1. Build da Aplicação

Compile o projeto para gerar o arquivo `.jar`:

```bash
O JAR será gerado em /target (ex: EventHub-API-0.0.1-SNAPSHOT.jar)

🐳 2. Iniciar o Ambiente com Docker Compose
bash
docker compose up --build -d
🌐 3. Acesso à Aplicação
API Principal: http://localhost:8080

🧹 Derrubar os Containers
bash
docker compose down
🔒 Segurança e Autenticação
A API utiliza autenticação baseada em JWT (JSON Web Tokens), sendo completamente stateless.

Obter Token: POST /api/usuarios/login

Uso do Token: Inclua no cabeçalho:

http
Authorization: Bearer <JWT_TOKEN>
Perfis de Usuário
Público: Sem token

Autenticado: Qualquer token válido

Organizador: Token válido com a role ORGANIZADOR

 🛣️ Endpoints da API


1. Usuários & Autenticação (/api/usuarios)
Método	Endpoint	Descrição	Autorização
POST	/registrar	Cria um novo usuário (organizador: true/false)	PÚBLICO
POST	/login	Autentica o usuário e retorna JWT	PÚBLICO
GET	/{id}	Busca usuário por ID	PÚBLICO
DELETE	/{id}	Remove usuário	PÚBLICO


2. Eventos (/api/eventos)
Método	Endpoint	Descrição	Autorização
POST	/	Cria evento vinculado ao organizador logado	ORGANIZADOR
GET	/	Lista todos os eventos ativos	PÚBLICO
GET	/futuros	Lista eventos com data futura	PÚBLICO
GET	/{id}	Detalhes de um evento específico	PÚBLICO
PUT	/{id}	Atualiza evento (somente organizador dono)	ORGANIZADOR
DELETE	/{id}	Exclui evento (somente organizador dono)	ORGANIZADOR


3. Inscrições (/api/inscricoes)
Método	Endpoint	Descrição	Autorização
POST	/evento/{eventoId}	Inscreve usuário autenticado no evento	AUTENTICADO
GET	/minhas	Lista inscrições do usuário logado	AUTENTICADO
DELETE	/{inscricaoId}	Cancela inscrição	AUTENTICADO
GET	/	Lista todas as inscrições do sistema	ORGANIZADOR
