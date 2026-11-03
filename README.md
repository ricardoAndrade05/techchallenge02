
# 🍴 Tech Challenge 2 - Restaurante API

API RESTful desenvolvida em **Spring Boot** para gerenciamento de restaurantes e cardápios, utilizando **PostgreSQL** e containerizado e aplicando práticas de **Clean Architecture**

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21+
* **Framework:** Spring Boot 4.x
  * Spring Data JPA
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker & Docker Compose
* **Documentação:** OpenAPI 3 / Swagger UI
* **Build Tool:** Maven

---

## 📋 Pré-requisitos

Antes de começar, garanta que você possui instalado na sua máquina:

* [Git](https://git-scm.com/)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) 
* [JDK 21+](https://www.oracle.com/java/technologies/downloads/)

## 🚀 Como Executar a Aplicação

⚠️ **Aviso de Projeto Acadêmico**  
> 💡 **Nota:** Por se tratar de um ambiente acadêmico e para facilitar a execução rápida, todas as variáveis de ambiente (portas, credenciais e conexões) já estão pré-configuradas diretamente no arquivo `docker-compose.yml`, eliminando a necessidade de criar um arquivo `.env`.

### 1. Execução Completa via Docker Compose (Recomendado)

Para subir o banco de dados PostgreSQL e a aplicação Spring Boot simultaneamente:

# 1. Clone o repositório
git clone git@github.com:ricardoAndrade05/techchallenge02   
cd techchallenge02

# 2. Suba todos os serviços containerizados
docker compose up -d --build

# 3. Acompanhe os logs da aplicação
docker compose logs -f app