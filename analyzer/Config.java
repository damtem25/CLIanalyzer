package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    private String packageName;
    private String repositoryUrl;
    private String repositoryMode;
    private String outputImage;
    private Boolean asciiTreeOutput;
    private Integer maxDepth;

    // Конструкторы
    public Config() {}

    public Config(String packageName, String repositoryUrl, String repositoryMode,
                  String outputImage, Boolean asciiTreeOutput, Integer maxDepth) {
        this.packageName = packageName;
        this.repositoryUrl = repositoryUrl;
        this.repositoryMode = repositoryMode;
        this.outputImage = outputImage;
        this.asciiTreeOutput = asciiTreeOutput;
        this.maxDepth = maxDepth;
    }

    // Геттеры и сеттеры
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public String getRepositoryMode() { return repositoryMode; }
    public void setRepositoryMode(String repositoryMode) { this.repositoryMode = repositoryMode; }

    public String getOutputImage() { return outputImage; }
    public void setOutputImage(String outputImage) { this.outputImage = outputImage; }

    public Boolean getAsciiTreeOutput() { return asciiTreeOutput; }
    public void setAsciiTreeOutput(Boolean asciiTreeOutput) { this.asciiTreeOutput = asciiTreeOutput; }

    public Integer getMaxDepth() { return maxDepth; }
    public void setMaxDepth(Integer maxDepth) { this.maxDepth = maxDepth; }

    @Override
    public String toString() {
        return String.format(
                "Config{packageName='%s', repositoryUrl='%s', repositoryMode='%s', " +
                        "outputImage='%s', asciiTreeOutput=%s, maxDepth=%d}",
                packageName, repositoryUrl, repositoryMode, outputImage, asciiTreeOutput, maxDepth
        );
    }

    // Метод для загрузки конфигурации из файла
    public static Config loadFromFile(String configPath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(configPath)));
        return parseConfig(content);
    }

    // Улучшенный парсинг JSON
    private static Config parseConfig(String jsonContent) {
        Config config = new Config();

        try {
            // Удаляем пробелы и переносы строк для упрощения парсинга
            jsonContent = jsonContent.replaceAll("\\s+", "");

            // Проверяем базовую структуру JSON
            if (!jsonContent.startsWith("{") || !jsonContent.endsWith("}")) {
                throw new IllegalArgumentException("Неверный формат JSON: должен начинаться с { и заканчиваться }");
            }

            // Удаляем внешние фигурные скобки
            jsonContent = jsonContent.substring(1, jsonContent.length() - 1);

            // Парсим каждое поле с улучшенной обработкой
            config.packageName = extractStringValue(jsonContent, "packageName");
            config.repositoryUrl = extractStringValue(jsonContent, "repositoryUrl");
            config.repositoryMode = extractStringValue(jsonContent, "repositoryMode");
            config.outputImage = extractStringValue(jsonContent, "outputImage");
            config.asciiTreeOutput = extractBooleanValue(jsonContent, "asciiTreeOutput");
            config.maxDepth = extractIntValue(jsonContent, "maxDepth");

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга JSON конфигурации: " + e.getMessage());
        }

        return config;
    }

    private static String extractStringValue(String json, String key) {
        String pattern = "\"" + key + "\":\"([^\"]*)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            String value = m.group(1);
            if (value.isEmpty()) {
                throw new IllegalArgumentException("Значение для ключа '" + key + "' не может быть пустым");
            }
            return value;
        }
        throw new IllegalArgumentException("Не найдено значение для обязательного ключа: " + key);
    }

    private static Boolean extractBooleanValue(String json, String key) {
        String pattern = "\"" + key + "\":(true|false)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Boolean.parseBoolean(m.group(1));
        }
        throw new IllegalArgumentException("Не найдено булево значение для ключа: " + key);
    }

    private static Integer extractIntValue(String json, String key) {
        String pattern = "\"" + key + "\":(\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Неверный числовой формат для ключа: " + key);
            }
        }
        throw new IllegalArgumentException("Не найдено числовое значение для ключа: " + key);
    }
}