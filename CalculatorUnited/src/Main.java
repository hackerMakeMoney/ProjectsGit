import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) throws WrongPattern, NegativeValue{
        Scanner in = new Scanner(System.in);
        while (true) {
        System.out.println("Введите пример");
        String input = in.nextLine();
        try {
            Calculator.calc(input);
        }
        catch (WrongPattern e) {
            System.out.println("Введите в правильном формате");
        }
        try {
            Calculator.calc(input);
        }
        catch (NegativeValue e) {
            System.out.println("Результат должен быть положительным в случае с римкими цифрами");
        }
            System.out.println("Ответ\n" + Calculator.calc(input));
        }
    }

    static class NegativeValue extends Exception{
        NegativeValue () {

        }
    }
    static class WrongPattern extends Exception{
        WrongPattern() {

        }
    }
    static  class Calculator {
        static int a;
        static int b;
        static class PatternChecker {
            static boolean arabPatternChecker(String input) {
                Pattern arabCyfres = Pattern.compile("(10|[0-9]) [+*/-] (10|[0-9])");
                Matcher checkExcWrongPatternArabCyfres = arabCyfres.matcher(input);
                boolean checkArabPattern = checkExcWrongPatternArabCyfres.matches();
                return checkArabPattern;
            }
            static boolean romePatternChecker(String input) {
                String romeCyfresArr2 = "(I|II|III|IV|V|VI|VII|VIII|IX|X)";
                Pattern romeCyfres = Pattern.compile(romeCyfresArr2 + " [+*/-] " + romeCyfresArr2);
                Matcher checkExcWrongPatternRomeCyfres = romeCyfres.matcher(input);
                boolean checkRomePattern = checkExcWrongPatternRomeCyfres.matches();
                return checkRomePattern;
            }
        }
        public static String calc(String input) throws WrongPattern, NegativeValue{
            String[] variables = input.split(" ");
            boolean stringIsArab = PatternChecker.arabPatternChecker(input);
            boolean stringIsRome = PatternChecker.romePatternChecker(input);
            if (stringIsArab == false && stringIsRome == false) {
                throw new WrongPattern();
            }

            if (stringIsArab == true) {
                a = Integer.parseInt(variables[0]);
                b = Integer.parseInt(variables[2]);
            }

            if (stringIsRome == true) {
                a = Converter.ToArab.convert(variables[0]);
                b = Converter.ToArab.convert(variables[2]);
            }

            String operation = variables[1];
            int resultInInt = 0;
            switch (operation) {
                case ("+"):
                    resultInInt = a + b;
                    break;
                case ("-"):
                    resultInInt = a - b;
                    break;
                case ("*"):
                    resultInInt = a * b;
                    break;
                case ("/"):
                    resultInInt = a / b;
                    break;
            }
            String resultInString = null;
            if (stringIsArab == true) {
                resultInString = Integer.toString(resultInInt);
            }
            if (stringIsRome == true) {
                if (resultInInt<=0) {
                    throw new NegativeValue();
                }
                resultInString = Converter.ToRome.convert(resultInInt);
            }
            return resultInString;
        }
    }

     static class Converter {
         static class ToRome {
             static String convert(int number) {
                if (number >= 4000 || number <= 0)
                    return null;
                StringBuilder result = new StringBuilder();
                for(Integer key : units.descendingKeySet()) {
                    while (number >= key) {
                        number -= key;
                        result.append(units.get(key));
                    }
                }
                return result.toString();
            }
            static final NavigableMap<Integer, String> units;
            static {
                NavigableMap<Integer, String> initMap = new TreeMap<>();
                initMap.put(1000, "M");
                initMap.put(900, "CM");
                initMap.put(500, "D");
                initMap.put(400, "CD");
                initMap.put(100, "C");
                initMap.put(90, "XC");
                initMap.put(50, "L");
                initMap.put(40, "XL");
                initMap.put(10, "X");
                initMap.put(9, "IX");
                initMap.put(5, "V");
                initMap.put(4, "IV");
                initMap.put(1, "I");
                units = Collections.unmodifiableNavigableMap(initMap);
            }
        }
        static class ToArab {
            static int convert(String roman) {
                int[] arabicValues = new int[roman.length()];
                for (int i = 0; i < roman.length(); i++) {
                    arabicValues[i] = romanDigitToArabicValue(roman.charAt(i));
                }
                int result = 0;
                for (int i = 0; i < arabicValues.length - 1; i++) {
                    if (arabicValues[i] < arabicValues[i + 1]) {
                        result -= arabicValues[i];
                    } else {
                        result += arabicValues[i];
                    }
                }
                result += arabicValues[arabicValues.length - 1];
                return result;
            }
            static int romanDigitToArabicValue(char digit) {
                switch (digit) {
                    case 'I':
                        return 1;
                    case 'V':
                        return 5;
                    case 'X':
                        return 10;
                    case 'L':
                        return 50;
                    case 'C':
                        return 100;
                    case 'D':
                        return 500;
                    case 'M':
                        return 1000;
                    default:
                        throw new IllegalArgumentException("Invalid Roman Digit!");
                }
            }
        }
    }
}
