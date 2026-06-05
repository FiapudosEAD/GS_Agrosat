# Deploy do Backend AgroSat no Render.com

## 📋 Pré-requisitos

1. Conta no GitHub (você já tem)
2. Conta no Render.com (criar em https://render.com)
3. Variáveis de ambiente configuradas

## 🚀 Passo a Passo

### 1. Criar Conta no Render

- Acesse https://render.com
- Clique em "Sign up"
- Login com GitHub
- Autorize o Render a acessar seus repositórios

### 2. Fazer Push dos Arquivos

Certifique-se de que o Dockerfile foi criado:
```bash
cd C:\Users\pedro\OneDrive\Documentos\FIAP\FiapudosGithub
git add GS_Agrosat/Dockerfile
git commit -m "Add Docker configuration for Render deployment"
git push origin main
```

### 3. Deploy no Render

1. Acesse https://dashboard.render.com
2. Clique em "New +"
3. Selecione "Web Service"
4. Conecte seu repositório GitHub
5. Configure:
   - **Name**: agrosat-backend
   - **Repository**: FiapudosGithub
   - **Branch**: main
   - **Root Directory**: GS_Agrosat
   - **Environment**: Docker
   - **Plan**: Free (ou Starter $7/mês)
6. Clique em "Create Web Service"

### 4. Configurar Variáveis de Ambiente

No dashboard do Render:
1. Vá até seu serviço "agrosat-backend"
2. Clique em "Environment"
3. Adicione variáveis:

```
MQTT_BROKER_URL=tcp://seu-mqtt.up.railway.app:1883
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@seu-oracle-host:1521:xe
SPRING_DATASOURCE_USERNAME=agrosat
SPRING_DATASOURCE_PASSWORD=agrosat
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.OracleDialect
```

### 5. Conectar ao Oracle (Banco de Dados)

**Opção A: Usar Oracle Cloud (Gratuito)**
1. Crie conta em https://www.oracle.com/cloud/free/
2. Implante Oracle XE
3. Use o hostname na variável SPRING_DATASOURCE_URL

**Opção B: Usar Railway para Oracle**
1. No Railway, crie um novo serviço: PostgreSQL
2. Atualize o application.yaml:

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

**Opção C: Usar banco local (desenvolvimento)**
Configure variáveis apontando para seu PC

### 6. Conectar ao MQTT Simulator

No arquivo `application.yaml`, configure:

```yaml
mqtt:
  broker:
    url: tcp://seu-mqtt.up.railway.app:1883
  client:
    id: agrosat-backend
  topic:
    filter: agrosat/+/+/+
  qos: 1
```

Onde `seu-mqtt.up.railway.app` é o domínio gerado pelo Railway.

### 7. Verificar Deploy

- Acesse o dashboard do Render
- Veja os logs em tempo real
- Procure por: "Started AgrosatApplication"
- URL pública será exibida (ex: agrosat-backend.onrender.com)

## 💰 Custo

- **Free**: Gratuito, mas dorme após 15 min sem requisições
- **Starter**: $7/mês, sempre ativo
- **Standard**: $12/mês+, mais recursos

## 🛠️ Troubleshooting

### Erro: "Cannot connect to MQTT broker"
- Verifique se o Railway MQTT está rodando
- Verifique a variável MQTT_BROKER_URL
- Verifique firewall

### Erro: "Cannot connect to database"
- Verifique credenciais do banco
- Verifique se banco está acessível
- Verifique URL do banco

### Erro: "Build failed"
- Verifique se pom.xml está correto
- Verifique Java version (deve ser 17)
- Veja logs de build completos

## 📝 Configuração Completa do application.yaml

```yaml
spring:
  application:
    name: agrosat
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:oracle:thin:@localhost:1521:xe}
    username: ${SPRING_DATASOURCE_USERNAME:agrosat}
    password: ${SPRING_DATASOURCE_PASSWORD:agrosat}
    driver-class-name: oracle.jdbc.OracleDriver
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.OracleDialect
    show-sql: false

mqtt:
  broker:
    url: ${MQTT_BROKER_URL:tcp://localhost:1883}
  client:
    id: agrosat-backend
  topic:
    filter: agrosat/+/+/+
  qos: 1

server:
  port: ${PORT:8080}
  servlet:
    context-path: /api
```

## ✓ Pronto!

Seu Backend AgroSat estará rodando 24/7 na internet! 🎉

Para mais informações: https://render.com/docs
