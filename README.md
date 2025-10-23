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

