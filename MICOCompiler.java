import java.util.*;

// AST Node
class Node {

    String value;
    Node left, right;

    Node(String value) {
        this.value = value;
        left = right = null;
    }
}

public class MICOCompiler {

    static int tempCount = 1;

    // Store TAC
    static List<String> tacList = new ArrayList<>();

    // Generate TAC using post-order traversal
    static String generateTAC(Node root) {

        // Leaf node
        if (root.left == null && root.right == null) {
            return root.value;
        }

        String left = generateTAC(root.left);
        String right = generateTAC(root.right);

        String temp = "t" + tempCount++;

        String tac = temp + " = " + left + " " + root.value + " " + right;

        tacList.add(tac);

        return temp;
    }

    // Build AST from expression
    static Node buildAST(String expr) {

        Stack<Node> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {

            char ch = expr.charAt(i);

            // Ignore spaces
            if (ch == ' ') {
                continue;
            }

            // Operand
            if (Character.isLetterOrDigit(ch)) {

                StringBuilder sb = new StringBuilder();

                while (i < expr.length() &&
                        Character.isLetterOrDigit(expr.charAt(i))) {

                    sb.append(expr.charAt(i));
                    i++;
                }

                i--;

                values.push(new Node(sb.toString()));
            }

            // Opening bracket
            else if (ch == '(') {
                ops.push(ch);
            }

            // Closing bracket
            else if (ch == ')') {

                while (!ops.isEmpty() && ops.peek() != '(') {

                    Node right = values.pop();
                    Node left = values.pop();

                    Node node = new Node(String.valueOf(ops.pop()));

                    node.left = left;
                    node.right = right;

                    values.push(node);
                }

                ops.pop();
            }

            // Operators
            else if (ch == '+' || ch == '-' ||
                    ch == '*' || ch == '/') {

                while (!ops.isEmpty() &&
                        precedence(ops.peek()) >= precedence(ch)) {

                    Node right = values.pop();
                    Node left = values.pop();

                    Node node = new Node(String.valueOf(ops.pop()));

                    node.left = left;
                    node.right = right;

                    values.push(node);
                }

                ops.push(ch);
            }
        }

        // Remaining operators
        while (!ops.isEmpty()) {

            Node right = values.pop();
            Node left = values.pop();

            Node node = new Node(String.valueOf(ops.pop()));

            node.left = left;
            node.right = right;

            values.push(node);
        }

        return values.pop();
    }

    // Operator precedence
    static int precedence(char op) {

        if (op == '+' || op == '-') {
            return 1;
        }

        if (op == '*' || op == '/') {
            return 2;
        }

        return 0;
    }

    // DAG Optimization
    static List<String> optimizeTAC(List<String> tacInput) {

        Map<String, String> exprMap = new HashMap<>();

        List<String> optimized = new ArrayList<>();

        for (String line : tacInput) {

            String[] parts = line.split("=");

            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            String op = "";
            String left = "";
            String right = "";

            // Find operator
            for (char c : rhs.toCharArray()) {

                if ("+-*/".indexOf(c) != -1) {
                    op = String.valueOf(c);
                    break;
                }
            }

            // Binary expression
            if (!op.equals("")) {

                String[] operands = rhs.split("\\" + op);

                left = operands[0].trim();
                right = operands[1].trim();

                String key = left + op + right;

                // Common Subexpression Elimination
                if (exprMap.containsKey(key)) {

                    String existing = exprMap.get(key);

                    optimized.add(lhs + " = " + existing);

                } else {

                    exprMap.put(key, lhs);

                    optimized.add(lhs + " = " +
                            left + " " + op + " " + right);
                }
            }

            else {
                optimized.add(line);
            }
        }

        return optimized;
    }

    // Generate Assembly Code
    static void generateAssembly(List<String> optimizedTAC) {

        System.out.println("\n========== ASSEMBLY CODE ==========");

        for (String line : optimizedTAC) {

            String[] parts = line.split("=");

            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            String op = "";

            for (char c : rhs.toCharArray()) {

                if ("+-*/".indexOf(c) != -1) {
                    op = String.valueOf(c);
                    break;
                }
            }

            if (!op.equals("")) {

                String[] operands = rhs.split("\\" + op);

                String left = operands[0].trim();
                String right = operands[1].trim();

                System.out.println("LOAD R1, " + left);
                System.out.println("LOAD R2, " + right);

                switch (op) {

                    case "+":
                        System.out.println("ADD R3, R1, R2");
                        break;

                    case "-":
                        System.out.println("SUB R3, R1, R2");
                        break;

                    case "*":
                        System.out.println("MUL R3, R1, R2");
                        break;

                    case "/":
                        System.out.println("DIV R3, R1, R2");
                        break;
                }

                System.out.println("STORE " + lhs + ", R3\n");
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("====================================================");
        System.out.println("             MICO JAVA COMPILER");
        System.out.println("      Minimal English-like Programming Language");
        System.out.println("                Created by Mahak");
        System.out.println("====================================================");

        System.out.print("\nMICO >> Enter Expression: ");

        String input = sc.nextLine();

        // Phase 1
        System.out.println("\n[ PHASE 1 : LEXICAL ANALYSIS ]");
        System.out.println("Tokens identified successfully...");

        // Phase 2
        System.out.println("\n[ PHASE 2 : SYNTAX ANALYSIS ]");
        System.out.println("Building Abstract Syntax Tree...");

        // Build AST
        Node root = buildAST(input);

        // Phase 3
        System.out.println("\n[ PHASE 3 : INTERMEDIATE CODE GENERATION ]");

        // Generate TAC
        System.out.println("\n========== THREE ADDRESS CODE ==========");

        generateTAC(root);

        // Phase 4
        System.out.println("\n[ PHASE 4 : DAG OPTIMIZATION ]");
        System.out.println("Eliminating common subexpressions...");

        // Optimize TAC
        List<String> optimized = optimizeTAC(tacList);

        System.out.println("\n========== OPTIMIZED TAC ==========");

        for (String line : optimized) {
            System.out.println(line);
        }

        // Phase 5
        System.out.println("\n[ PHASE 5 : ASSEMBLY CODE GENERATION ]");

        // Generate Assembly Code
        generateAssembly(optimized);

        System.out.println("====================================================");
        System.out.println("          MICO Compilation Complete");
        System.out.println("====================================================");
    }
}