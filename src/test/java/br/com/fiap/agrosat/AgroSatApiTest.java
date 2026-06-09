package br.com.fiap.agrosat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AgroSatApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("CT01 - Cadastro de usuário")
    void ct01_cadastroUsuario() throws Exception {

        var body = Map.of(
                "nome", "Teste API",
                "email", "teste" + System.currentTimeMillis() + "@fiap.com",
                "senha", "Senha@123",
                "role", "PRODUTOR"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("CT02 - Login válido")
    void ct02_loginValido() throws Exception {

        var body = Map.of(
                "email", "teste@fiap.com",
                "senha", "Senha@123"
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CT03 - Login inválido")
    void ct03_loginInvalido() throws Exception {

        var body = Map.of(
                "email", "teste@fiap.com",
                "senha", "senhaErrada"
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("CT04 - Acesso sem JWT")
    void ct04_semToken() throws Exception {

        mockMvc.perform(get("/api/v1/propriedades"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CT05 - SQL Injection")
    void ct05_sqlInjection() throws Exception {

        var body = Map.of(
                "email", "' OR '1'='1'; DROP TABLE usuario; --",
                "senha", "qualquer"
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest()); // 400
    }
}