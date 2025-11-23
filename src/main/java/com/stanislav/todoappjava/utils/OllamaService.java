package com.stanislav.todoappjava.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class OllamaService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "llama3.1"; // Используйте корректное имя модели
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static CompletableFuture<String> summarize(String text) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String requestBody = String.format(
                        "{\"model\": \"%s\", \"prompt\": \"Summarize the following text: %s\", \"stream\": false}",
                        MODEL_NAME, escapeJson(text)
                );

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(OLLAMA_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return parseOllamaResponse(response.body());
                } else {
                    throw new RuntimeException("Ollama API error: " + response.statusCode() + " - " + response.body());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return text; // fallback: возвращаем исходный текст
            }
        });
    }

    private static String parseOllamaResponse(String jsonResponse) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonResponse);
            JsonNode responseNode = root.path("response");
            if (!responseNode.isMissingNode()) {
                return responseNode.asText().replace("\\n", "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Summary not available";
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
