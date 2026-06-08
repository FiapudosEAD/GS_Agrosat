# 🚀 Setup Local Swagger - GS_Agrosat

Documentação interativa da API no seu ambiente local.

---

## ✅ O Que Foi Configurado

- ✅ Dependência Springdoc OpenAPI adicionada ao `pom.xml`
- ✅ Anotações `@Operation` adicionadas aos controllers
- ✅ Configurações Swagger adicionadas ao `application.yaml`
- ✅ Pronto para usar!

---

## 🚀 Como Usar

### 1. Build do Projeto

```bash
cd GS_Agrosat
mvn clean install
```

### 2. Iniciar Aplicação

```bash
mvn spring-boot:run
```

Ou via IDE, clique em **Run** → **AgrosatApplication**

### 3. Acessar Swagger UI

Abra no navegador:
```
http://localhost:8080/swagger-ui.html
```

### 4. Pronto!

Você verá a documentação interativa com todos os endpoints.

---

## 📚 Usando a Interface

### Testar um Endpoint

1. **Clique em um endpoint** (ex: POST `/auth/login`)
2. **Clique em "Try it out"**
3. **Preencha os dados** (JSON)
4. **Clique em "Execute"**
5. **Veja a resposta** em tempo real!

### Adicionar Token JWT

1. **Clique no botão "Authorize"** (canto superior direito)
2. **Faça login** via POST `/auth/login`
3. **Copie o token** da resposta
4. **Cole em "Bearer token"**:
   ```
   eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
5. **Clique em "Authorize"**
6. Todos os endpoints protegidos usarão o token automaticamente

---

## 📖 Dokumentação Automática

O Springdoc gera automaticamente:
- ✅ Todos os endpoints
- ✅ Parâmetros (path, query, body)
- ✅ Request/Response schemas
- ✅ Status codes
- ✅ Autenticação

### Melhorar Documentação Adicionando Anotações

```java
@PostMapping("/propriedades")
@Operation(
    summary = "Criar propriedade",
    description = "Criar uma nova propriedade agrícola"
)
@ApiResponse(
    responseCode = "201",
    description = "Propriedade criada com sucesso"
)
public String criar(
    @RequestHeader("Authorization") String authHeader,
    @RequestBody CriarPropriedadeRequest request
) {
    // ...
}
```

**Onde adicionar:**
- Todos os controllers em `src/main/java/br/com/fiap/agrosat/controller/`

---

## 🔄 Atualizar Documentação

### Automaticamente (Melhor)

Conforme você adiciona `@Operation` nos controllers, a documentação se atualiza automaticamente quando você reinicia a app.

### Manualmente

Se quiser exportar o OpenAPI JSON:

```bash
curl http://localhost:8080/v3/api-docs > openapi.json
```

---

## 🛠️ Troubleshooting

### "Swagger UI não aparece"

1. Verifique se a app iniciou sem erros
2. Verifique se a porta 8080 está livre
3. Verifique se `springdoc` está no `pom.xml`
4. Execute `mvn clean install` novamente

### "Endpoints não aparecem"

1. Certifique-se que os controllers têm `@RestController` e `@RequestMapping`
2. Reinicie a aplicação
3. Aguarde alguns segundos e recarregue o navegador

### "Token não funciona"

1. Faça login via POST `/auth/login` primeiro
2. Copie o **token completo** (sem aspas)
3. Clique em "Authorize" (não em um endpoint)
4. Cole o token em "Bearer token"

---

## 📊 Endpoints Disponíveis

Visite http://localhost:8080/swagger-ui.html para ver:

| Tag | Endpoints |
|-----|-----------|
| **Auth** | register, login |
| **Propriedades** | criar, listar, buscar, atualizar, deletar |
| **Talhões** | criar, atualizar, deletar |
| **Alertas** | listar, marcar como lido |
| **Leituras** | listar com filtros |
| **Satélite** | listar NDVI |

---

## 🎯 Próximas Etapas

1. ✅ Swagger configurado localmente
2. → Adicionar mais anotações `@Operation` nos outros controllers
3. → Testar todos os endpoints via Swagger UI
4. → Gerar OpenAPI JSON para documentação
5. → Usar para testes automáticos

---

## 📝 Adicionar Anotações em Todos os Controllers

### Template para cada controller:

```java
@RestController
@RequestMapping("/api/v1/RECURSO")
@RequiredArgsConstructor
@Tag(name = "Nome", description = "Descrição")
public class RecursoController {

    @GetMapping
    @Operation(summary = "Listar", description = "Listar todos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public List<Response> listar() {
        // ...
    }

    @PostMapping
    @Operation(summary = "Criar", description = "Criar novo")
    @ApiResponse(responseCode = "201", description = "Criado")
    public String criar(@RequestBody Request request) {
        // ...
    }
}
```

---

## 🔗 Integração com Frontend

Use o endpoint `/v3/api-docs` para gerar cliente automático:

```bash
# Gerar cliente OpenAPI em TypeScript
npx openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-fetch \
  -o ./generated-api-client
```

---

## ✨ Recursos Adicionais

- **Documentação Oficial**: https://springdoc.org/
- **OpenAPI Spec**: https://spec.openapis.org/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/

---

## 🎉 Pronto!

Sua API agora tem documentação interativa automática!

Acesse: **http://localhost:8080/swagger-ui.html**
