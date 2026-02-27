package com.SmartNote.demo.service;

import com.SmartNote.demo.model.CategoryType;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;
    private final String model;

    public GeminiService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model}") String model) {
        this.client = new Client.Builder().apiKey(apiKey).build();
        this.model = model;
    }

    public CategoryType classifyCategory(String title, String content) {
        String prompt = String.format(
            "Classify the following note into exactly one of these categories: " +
            "SHOPPING, HEALTH, SPORT, TASK, COOKING, UNCATEGORIZED.\n" +
            "Respond with ONLY the category name, nothing else.\n\n" +
            "Title: %s\nContent: %s",
            title != null ? title : "",
            content != null ? content : ""
        );

        try {
            GenerateContentResponse response = client.models.generateContent(model, prompt, null);
            String result = response.text().trim().toUpperCase().replaceAll("[^A-Z_]", "");
            return CategoryType.valueOf(result);
        } catch (Exception e) {
            return CategoryType.UNCATEGORIZED;
        }
    }

    public String generateTitle(String content) {
        String prompt = String.format(
            "Generate a short, concise title (3 to 6 words) for the following note. " +
            "Respond with ONLY the title, no quotes, no punctuation at the end.\n\n%s",
            content != null ? content : ""
        );

        try {
            GenerateContentResponse response = client.models.generateContent(model, prompt, null);
            return response.text().trim();
        } catch (Exception e) {
            return "Untitled Note";
        }
    }
}
