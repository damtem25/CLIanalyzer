package analyzer;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DependencyAnalyzer {
    private final Config config;

    public DependencyAnalyzer(Config config) {
        this.config = config;
        validateConfiguration();
    }

    private void validateConfiguration() {
        // Проверка обязательных параметров
        if (config.getPackageName() == null || config.getPackageName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пакета должно быть задано");
        }

        if (config.getRepositoryUrl() == null || config.getRepositoryUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("URL репозитория должен быть задан");
        }

        // Проверка режима репозитория
        if (config.getRepositoryMode() == null) {
            throw new IllegalArgumentException("Режим репозитория должен быть задан");
        }

        String[] validModes = {"local", "remote", "test"};
        if (!Arrays.asList(validModes).contains(config.getRepositoryMode())) {
            throw new IllegalArgumentException("Недопустимый режим репозитория: " + config.getRepositoryMode() +
                    ". Допустимые значения: " + Arrays.toString(validModes));
        }

        // Проверка максимальной глубины
        if (config.getMaxDepth() == null) {
            throw new IllegalArgumentException("Максимальная глубина должна быть задана");
        }

        if (config.getMaxDepth() <= 0) {
            throw new IllegalArgumentException("Максимальная глубина должна быть положительным числом");
        }

        if (config.getMaxDepth() > 100) {
            throw new IllegalArgumentException("Максимальная глубина не может превышать 100");
        }

        // Проверка имени файла изображения
        if (config.getOutputImage() == null || config.getOutputImage().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя файла изображения должно быть задано");
        }

        if (!config.getOutputImage().toLowerCase().endsWith(".png") &&
                !config.getOutputImage().toLowerCase().endsWith(".jpg")) {
            throw new IllegalArgumentException("Файл изображения должен иметь расширение .png или .jpg");
        }

        // Проверка режима ASCII-дерева
        if (config.getAsciiTreeOutput() == null) {
            throw new IllegalArgumentException("Режим ASCII-дерева должен быть задан");
        }
    }

    public void analyze() {
        System.out.println("Запуск анализа зависимостей для пакета: " + config.getPackageName());
        System.out.println("Режим работы: " + config.getRepositoryMode());
        System.out.println("Максимальная глубина анализа: " + config.getMaxDepth());

        // Здесь будет реализована основная логика анализа зависимостей
        // На данном этапе просто демонстрируем успешный запуск

        System.out.println("Анализ завершен успешно!");
        System.out.println("Результаты будут сохранены в: " + config.getOutputImage());

        if (config.getAsciiTreeOutput()) {
            System.out.println("ASCII-дерево зависимостей будет сгенерировано");
        }
    }
}