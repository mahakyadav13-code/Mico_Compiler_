import java.util.*;

public class MICO_LA {

    // Keywords of MICO
    static String[] keywords = {
        "int","float","if","else","main",
        "take","display","add","sub","mul","div",
        "repeat","until","return"
    };

    // Check if word is keyword
    static boolean isKeyword(String word) {
        for(String k : keywords) { 
            if(k.equals(word))
                return true;
        }
        return false;
    }

    // Lexical Analyzer Function
    static void lexicalAnalyzer(String input) {

        StringTokenizer st = new StringTokenizer(input, " \n\t(){};=+-*/<>!&|", true);

        while(st.hasMoreTokens()) {

            String token = st.nextToken();

            if(token.trim().isEmpty())
                continue;

            if(isKeyword(token))
                System.out.println("Token: " + token + " -> KEYWORD");

            else if(token.matches("-?\\d+(\\.\\d+)?"))
                System.out.println("Token: " + token + " -> NUMBER");

            else if(token.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
                System.out.println("Token: " + token + " -> IDENTIFIER");

            else
                System.out.println("Token: " + token + " -> OPERATOR");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StringBuilder code = new StringBuilder();

        System.out.println("Enter MICO program (type END to stop):");

        while(true) {
            String line = sc.nextLine();
            if(line.equals("END"))
                break;
            code.append(line).append("\n");
        }
        lexicalAnalyzer(code.toString());
        sc.close();
    }
}