package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    private static final String DEFAULT_CONFIG_PATH = "config.json";

    public static void main(String[] args) {
        try {
            // Определяем путь к конфигурационному файлу
            String configPath = getConfigPath(args);

            // Загружаем конфигурацию
            Config config = loadConfiguration(configPath);

            // Выводим все параметры в формате ключ-значение
            printConfiguration(config);

            // Создаем анализатор и запускаем его
            DependencyAnalyzer analyzer = new DependencyAnalyzer(config);
            analyzer.analyze();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String getConfigPath(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        return DEFAULT_CONFIG_PATH;
    }

    private static Config loadConfiguration(String configPath) {
        try {
            // Проверяем существование файла
            if (!Files.exists(Paths.get(configPath))) {
                throw new IllegalArgumentException("Конфигурационный файл не найден: " + configPath);
            }

            // Загружаем конфигурацию
            return Config.loadFromFile(configPath);

        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }
    }

    private static void printConfiguration(Config config) {
        System.out.println("=== Конфигурационные параметры ===");
        System.out.printf("packageName: %s%n",
                validateAndFormatValue(config.getPackageName(), "Имя пакета"));
        System.out.printf("repositoryUrl: %s%n",
                validateAndFormatValue(config.getRepositoryUrl(), "URL репозитория"));
        System.out.printf("repositoryMode: %s%n",
                validateAndFormatValue(config.getRepositoryMode(), "Режим репозитория"));
        System.out.printf("outputImage: %s%n",
                validateAndFormatValue(config.getOutputImage(), "Файл изображения"));
        System.out.printf("asciiTreeOutput: %s%n",
                validateAndFormatValue(config.getAsciiTreeOutput(), "Режим ASCII-дерева"));
        System.out.printf("maxDepth: %s%n",
                validateAndFormatValue(config.getMaxDepth(), "Максимальная глубина"));
        System.out.println("===================================");
    }

    private static String validateAndFormatValue(Object value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException("Параметр '" + paramName + "' не задан в конфигурации");
        }
        return String.valueOf(value);
    }
}