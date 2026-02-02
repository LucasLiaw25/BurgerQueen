# 🍔 BurgerQueen API

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker)

O **BurgerQueen** é uma API robusta de delivery projetada para gerenciar o fluxo completo de uma hamburgueria, desde o cadastro de produtos até a finalização de pedidos com pagamentos automatizados.

## 🚀 Funcionalidades Principal
- **Gestão de Pedidos:** Fluxo completo (Recebido, Em Preparo, Concluído).
- **Segurança:** Autenticação e Autorização via Spring Security com tokens JWT.
- **Pagamentos:** Integração real com API PIX.
- **Notificações:** Envio automatizado de confirmação de pedidos via E-mail (SMTP Gmail).
- **Infraestrutura:** Totalmente conteinerizado com Docker e Docker Compose.

## 🛠️ Tecnologias Utilizadas
- [cite_start]**Back-end:** Java 21, Spring Boot 3.
- [cite_start]**Banco de Dados:** PostgreSQL[cite: 2, 4].
- [cite_start]**Segurança:** Spring Security + JWT (Chaves RSA .pem/.pub).
- [cite_start]**Integrações:** API PIX, Jakarta Mail.
- [cite_start]**DevOps:** Docker, Docker Compose.

## 📦 Como Rodar o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados.
- Arquivo `.env` configurado na raiz (veja o modelo abaixo).

### Configuração do Ambiente (.env)
Crie um arquivo `.env` baseado nas seguintes variáveis:
```env
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
EMAIL_USER=seu_email@gmail.com
EMAIL_PASSWORD=sua_senha_de_app
PIX_CLIENT_ID=seu_id
PIX_CLIENT_SECRET=seu_secret
