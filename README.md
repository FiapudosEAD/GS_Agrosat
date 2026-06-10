# 🌱 AgroSat Backend

Sistema backend desenvolvido em Spring Boot para monitoramento agrícola através de dados de sensores IoT, informações de satélite e geração de alertas para produtores rurais.

## 📋 Sobre o Projeto

O AgroSat é uma API REST responsável por:

- Autenticação de usuários via JWT
- Cadastro e gerenciamento de propriedades rurais
- Cadastro e gerenciamento de talhões
- Recebimento de dados via MQTT
- Monitoramento de leituras ambientais
- Geração e gerenciamento de alertas
- Consulta de dados de satélite (NDVI)
- Documentação automática via Swagger/OpenAPI

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- Oracle Database
- JWT Authentication
- MQTT (Eclipse Paho)
- Maven
- Swagger/OpenAPI

---

## 📁 Estrutura do Projeto

```text
src
├── main
│   ├── java
│   │   └── br.com.fiap.agrosat
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── model
│   │       ├── security
│   │       └── config
│   └── resources
│       └── application.yaml
└── test
```

---

## ✅ Pré-requisitos

Antes de executar o projeto, certifique-se de possuir instalado:

- Java 17 ou superior
- Maven 3.9+
- Oracle Database
- Broker MQTT (Mosquitto recomendado)
- Git

Verifique as versões:

```bash
java -version
mvn -version
```

---

## ⚙️ Configuração do Banco de Dados

No arquivo:

```yaml
src/main/resources/application.yaml
```

configure suas credenciais Oracle:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
    username: SEU_USUARIO
    password: SUA_SENHA
```

---

## 🔑 Configuração JWT

Defina uma chave secreta para assinatura dos tokens:

```yaml
jwt:
  secret: SUA_CHAVE_SECRETA
  expiration: 3600000
```

### Gerando uma chave JWT

Você pode gerar uma chave segura utilizando:

```bash
openssl rand -hex 32
```

Exemplo:

```text
eff6d494bd125addc10eee441a8e54140e5f4e856f9ad42e91bfd7370e4f245b
```

---

## 📡 Configuração MQTT

Configure o broker MQTT:

```yaml
mqtt:
  broker:
    url: tcp://localhost:1883
```

### Instalação rápida do Mosquitto

Docker:

```bash
docker run -d \
  --name mosquitto \
  -p 1883:1883 \
  eclipse-mosquitto
```

---

## 🚀 Executando o Projeto

### 1. Clonar o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd GS_Agrosat
```

### 2. Instalar dependências

```bash
mvn clean install
```

### 3. Executar a aplicação

Utilizando Maven:

```bash
mvn spring-boot:run
```

Ou executando pela IDE:

```text
AgrosatApplication.java
```

---

## 🐳 Executando com Docker

### Build da imagem

```bash
docker build -t agrosat .
```

### Executar container

```bash
docker run -p 8080:8080 agrosat
```

---

## 📖 Swagger/OpenAPI

Após iniciar a aplicação:

```text
http://localhost:8080/swagger-ui.html
```

Documentação OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

---

## 🔐 Fluxo de Autenticação

### Registrar usuário

```http
POST /api/v1/auth/register
```

### Login

```http
POST /api/v1/auth/login
```

A resposta retornará um token JWT.

Exemplo:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Utilização do token

Adicionar no header:

```http
Authorization: Bearer SEU_TOKEN
```

---

## 📌 Principais Endpoints

### Auth

```http
POST /api/v1/auth/register
POST /api/v1/auth/login
```

### Propriedades

```http
POST   /api/v1/propriedades
GET    /api/v1/propriedades
GET    /api/v1/propriedades/{id}
PUT    /api/v1/propriedades/{id}
DELETE /api/v1/propriedades/{id}
```

### Talhões

```http
POST   /api/v1/talhoes
GET    /api/v1/talhoes/{id}
PUT    /api/v1/talhoes/{id}
DELETE /api/v1/talhoes/{id}
```

### Alertas

```http
PUT /api/v1/alertas/{id}/marcar-lido
```

---

## 🧪 Executando Testes

```bash
mvn test
```

---

## 📦 Gerando o JAR

```bash
mvn clean package
```

O artefato será gerado em:

```text
target/agrosat-0.0.1-SNAPSHOT.jar
```

Executar:

```bash
java -jar target/agrosat-0.0.1-SNAPSHOT.jar
```

---

## 🔧 Variáveis de Ambiente (Produção)

Recomendado utilizar variáveis de ambiente:

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

JWT_SECRET=

MQTT_BROKER_URL=
```

---

## 👥 Equipe

Projeto desenvolvido para o Global Solution FIAP.

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais.
