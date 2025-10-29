package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String DEFAULT_CONFIG_PATH = "config.json";

    public static void main(String[] args) {
        try {
            System.out.println("Dependency Analyzer");

            // Определяем путь к конфигурационному файлу
            String configPath = getConfigPath(args);
            System.out.println("Используется конфигурационный файл: " + configPath);

            // Загружаем конфигурацию
            Config config = loadConfiguration(configPath);

            // Выводим все параметры в формате ключ-значение
            printConfiguration(config);

            // Создаем анализатор и запускаем его
            DependencyAnalyzer analyzer = new DependencyAnalyzer(config);
            analyzer.analyze();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            printUsage();
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
            Path path = Paths.get(configPath);

            // Проверяем существование файла
            if (!Files.exists(path)) {
                // Создаем пример конфигурационного файла, если он не существует
                if (configPath.equals(DEFAULT_CONFIG_PATH)) {
                    System.out.println(" Создан файл конфигурации по умолчанию: " + DEFAULT_CONFIG_PATH);
                    System.out.println(" Пожалуйста, настройте параметры в config.json и запустите приложение снова");
                    System.exit(0);
                } else {
                    throw new IllegalArgumentException("Конфигурационный файл не найден: " + configPath);
                }
            }

            // Проверяем, что это файл, а не директория
            if (!Files.isRegularFile(path)) {
                throw new IllegalArgumentException("Указанный путь не является файлом: " + configPath);
            }

            // Проверяем, что файл не пустой
            if (Files.size(path) == 0) {
                throw new IllegalArgumentException("Конфигурационный файл пуст: " + configPath);
            }

            // Загружаем конфигурацию
            return Config.loadFromFile(configPath);

        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }
    }


    private static void printConfiguration(Config config) {
        System.out.println("\nКонфигурационные параметры");
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
    }

    private static String validateAndFormatValue(Object value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException("Параметр '" + paramName + "' не задан в конфигурации");
        }
        return String.valueOf(value);
    }

    private static void printUsage() {
        System.out.println("\\nИспользование:");
        System.out.println("  java -jar dependency-analyzer.jar [config-file]");
        System.out.println("\\nПримеры:");
        System.out.println("  java -jar dependency-analyzer.jar                    # использует config.json");
        System.out.println("  java -jar dependency-analyzer.jar my-config.json     # использует my-config.json");
        System.out.println("\\nЕсли config.json не существует, он будет создан автоматически.");
    }
}