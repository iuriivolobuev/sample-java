package util.german;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Trainer {
    public static void main(String[] args) {
        Map<String, String> pairs = new LinkedHashMap<>();
        pairs.put("dog", "der Hund");
        pairs.put("We have a dog", "Wir haben einen Hund");

        Scanner scanner = new Scanner(System.in);
        for (Map.Entry<String, String> next : pairs.entrySet()) {
            System.out.println("--------------------------------------------------");
            System.out.println(next.getKey());
            String answer = next.getValue();
            while (true) {
                String input = scanner.nextLine();
                if (answer.equals(input)) {
                    System.out.println("Correct!");
                    break;
                }
                StringBuilder sb = new StringBuilder(answer.length());
                for (int i = 0; i < answer.length(); i++) {
                    char ch = answer.charAt(i);
                    if (ch == ' ' || ch == '.' || sameChar(input, i, ch)) {
                        sb.append(ch);
                    } else {
                        sb.append("*");
                    }
                }
                System.out.println(sb);
            }
        }
    }

    private static boolean sameChar(String input, int index, char ch) {
        return index < input.length() && input.charAt(index) == ch;
    }
}
