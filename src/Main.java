import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int[] ROME_VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] ROME_SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final String[] TEN_ROME_SYMBOLS = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static void main(String[] args) {
        Scanner inputLine = new Scanner(System.in);
        inputLine.useLocale(Locale.US);
        System.out.println("Введите выражение: ");
        String expression = inputLine.nextLine();

        try {
            String result = calc(expression);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String calc(String input) {
        String[] splittedExpression = input.split(" ");
        String operator = splittedExpression[1];
        String preFirstNum = splittedExpression[0];
        String preSecondNum = splittedExpression[2];

        if (isArabicExpression(splittedExpression)) {
            int firstNum = Integer.parseInt(splittedExpression[0]);
            int secondNum = Integer.parseInt(splittedExpression[2]);

            if (isValidArabicInput(firstNum, secondNum)) {
                return String.valueOf(calculation(firstNum, secondNum, operator));
            }

        } else if (isRomeExpression(splittedExpression)) {
            if (isValidRomeInput(preFirstNum, preSecondNum)) {
                int firstNumIndex = Arrays.asList(TEN_ROME_SYMBOLS).indexOf(preFirstNum) + 1;
                int secondNumIndex = Arrays.asList(TEN_ROME_SYMBOLS).indexOf(preSecondNum) + 1;
                int romePreResult = calculation(firstNumIndex, secondNumIndex, operator);
                return romeConversion(romePreResult);
            }
        }

        throw new IllegalArgumentException("Введено некорректное значение");
    }

    private static boolean isArabicExpression(String[] expression) {
        String arabicRegex = "\\d{1,2} [+-/*] \\d{1,2}";
        Pattern arabicPattern = Pattern.compile(arabicRegex);
        Matcher arabicMatcher = arabicPattern.matcher(String.join(" ", expression));
        return arabicMatcher.matches();
    }

    private static boolean isRomeExpression(String[] expression) {
        String romeRegex = "[IVX]{1,4} [+-/*] [IVX]{1,4}";
        Pattern romePattern = Pattern.compile(romeRegex);
        Matcher romeMatcher = romePattern.matcher(String.join(" ", expression));
        return romeMatcher.matches();
    }

    private static boolean isValidArabicInput(int firstNum, int secondNum) {
        return firstNum > 0 && firstNum <= 10 && secondNum > 0 && secondNum <= 10;
    }

    private static boolean isValidRomeInput(String preFirstNum, String preSecondNum) {
        return Arrays.asList(TEN_ROME_SYMBOLS).contains(preFirstNum) && Arrays.asList(TEN_ROME_SYMBOLS).contains(preSecondNum);
    }

    public static Integer calculation(int firstNum, int secondNum, String operator) {
        return switch (operator) {
            case "+" -> firstNum + secondNum;
            case "-" -> firstNum - secondNum;
            case "/" -> firstNum / secondNum;
            case "*" -> firstNum * secondNum;
            default -> null;
        };
    }

    public static String romeConversion(int romePreResult) {
        StringBuilder romeResult = new StringBuilder();
        for (int i = 0; i < ROME_VALUES.length; i++) {
            while (romePreResult >= ROME_VALUES[i]) {
                romePreResult -= ROME_VALUES[i];
                romeResult.append(ROME_SYMBOLS[i]);
            }
        }
        return romeResult.toString();
    }
}
