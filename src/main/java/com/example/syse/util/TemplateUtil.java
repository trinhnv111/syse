package com.example.syse.util;

import java.util.Map;

public class TemplateUtil {
    /**
     * Render template content by replacing {{key}} with value from placeholders.
     * @param content HTML/text content with {{key}}
     * @param placeholders Map of key-value pairs
     * @return Rendered content
     */
    public static String renderTemplate(String content, Map<String, String> placeholders) {
        if (content == null || placeholders == null) return content;
        String rendered = content;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            rendered = rendered.replace("{{" + key + "}}", value != null ? value : "");
        }
        return rendered;
    }
} 