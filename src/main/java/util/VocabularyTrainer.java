package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * To train learned words and phrases from another language.
 *
 * <p>Usage:
 * <ol>
 *     <li>cd src/main/java</li>
 *     <li>javac util/VocabularyTrainer.java</li>
 *     <li>java -cp . util.VocabularyTrainer words.csv</li>
 * </ol>
 * words.csv might be something like this:
 * <pre>
 * Es ist ein Hund.|It is a dog.
 * Er heißt Buddy.|His name is Buddy.
 * </pre>
 * Use "rlwrap" utility for better interaction during training (i.e. the left & right arrows to move the cursor, and the
 * up arrow to get previous input): {@code rlwrap java -cp . util.VocabularyTrainer words.csv}.
 */
class VocabularyTrainer {
    private static final String CSV_DELIMITER = "\\|";
    private static final String GET_ANSWER_COMMAND = "?";

    public static void main(String[] args) {
        if (args.length == 0) {
            printText("Path should be specified as an argument.");
            return;
        }

        List<Translation> translations;
        String csvPath = args[0];
        try {
            translations = readTranslationsFromCsv(csvPath);
        } catch (IOException e) {
            printText("Couldn't get translations from '%s'.".formatted(csvPath));
            return;
        }
        Collections.shuffle(translations);

        printSeparationLine();
        int size = translations.size();
        printText("Number of translations to train: [%d]. Type '%s' to get an answer.".formatted(size, GET_ANSWER_COMMAND));
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < size; i++) {
            printSeparationLine();
            Translation translation = translations.get(i);
            System.out.println(translation.from);
            String answer = translation.to;
            boolean answered = process(scanner, answer);
            printProgress(answer, answered, size, i + 1);
        }
        printSeparationLine();
        printText("Completed!");
        printSeparationLine();
    }

    private static List<Translation> readTranslationsFromCsv(String path) throws IOException {
        List<Translation> translations = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            for (String line : lines.toList()) {
                String[] parts = line.split(CSV_DELIMITER);
                translations.add(new Translation(parts[1], parts[0]));
            }
        }
        return translations;
    }

    private static boolean process(Scanner scanner, String answer) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (answer.equals(input)) {
                return true;
            }
            if (GET_ANSWER_COMMAND.equals(input)) {
                return false;
            }
            StringBuilder hint = new StringBuilder(answer.length());
            for (int j = 0; j < answer.length(); j++) {
                char ch = answer.charAt(j);
                if (ch == ' ' || ch == '.'  || ch == ',' || sameChar(input, j, ch)) {
                    hint.append(ch);
                } else {
                    hint.append("*");
                }
            }
            printText(hint.toString());
        }
    }

    private static boolean sameChar(String input, int index, char ch) {
        return index < input.length() && input.charAt(index) == ch;
    }

    private static void printText(String text) {
        System.out.println(text);
    }

    private static void printSeparationLine() {
        System.out.println("-".repeat(60));
    }

    private static void printProgress(String answer, boolean answered, int size, int completed) {
        if (!answered) {
            System.out.println(answer);
        }
        //prints in this format: "✅ [===  ] 60%" or "❗ [===  ] 60%"
        System.out.printf("%s [%s%s] %d%%%n", answered ? '✅' : '❗', "=".repeat(completed),
                " ".repeat(size - completed), completed * 100 / size);
    }

    private static class Translation {
        private final String from;
        private final String to;

        public Translation(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    @SuppressWarnings("unused")//txt format is needed for learning before training
    private static class CsvToTxtConverter {
        public static void main(String[] args) throws IOException {
            if (args.length == 0) {
                printText("Path should be specified as an argument.");
                return;
            }

            List<Translation> translations;
            String csvPath = args[0];
            try {
                translations = readTranslationsFromCsv(csvPath);
            } catch (IOException e) {
                printText("Couldn't get translations from '%s'.".formatted(csvPath));
                return;
            }

            if (!translations.isEmpty()) {
                try (PrintWriter writer = new PrintWriter("translations.txt", StandardCharsets.UTF_8)) {
                    for (Translation translation : translations) {
                        writer.println(translation.to);
                        writer.println(translation.from);
                        writer.println();
                    }
                }
            }
        }
    }
}
