import java.util.*;

public class Main {

    static String input;
    static int pos = 0;

    // Get current character
    static char currentChar() {
        if (pos < input.length())
            return input.charAt(pos);
        return '\0';
    }

    // Move to next character
    static void next() {
        pos++;
    }

    // F -> (E) | digit
    static int F() {

        if (currentChar() == '(') {
            next(); // skip '('
            int val = E();
            
            next(); // skip ')'
            return val;
        }

        // only single digit allowed
        int num = currentChar() - '0';
        next();
        return num;
    }

    // T -> F * T | F
    static int T() {

        int val = F();

        if (currentChar() == '*') {
            next(); // skip '*'
            val = val * T(); // recursive call
        }

        return val;
    }

    // E -> T + E | T
    static int E() {

        int val = T();

        if (currentChar() == '+') {
            next(); // skip '+'
            val = val + E(); // recursive call
        }

        return val;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter expression (e.g., 2*3+4):");
        input = sc.nextLine().replaceAll(" ", "");

        int result = E();

        System.out.println("Result: " + result);

        sc.close();
    }
}