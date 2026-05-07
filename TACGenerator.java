import java.util.*;

// Node class for AST
class Node {
    String value;
    Node left, right;

    Node(String value) {
        this.value = value;
        this.left = this.right = null;
    }
}

public class TACGenerator {

    static int tempCount = 1;

    // Generate TAC using post-order traversal
    static String generateTAC(Node root) {
        if (root.left == null && root.right == null) {
            return root.value;
        }

        String left = generateTAC(root.left);
        String right = generateTAC(root.right);

        String temp = "t" + tempCount++;
        System.out.println(temp + " = " + left + " " + root.value + " " + right);

        return temp;
    }

    // Build AST from infix expression
    static Node buildAST(String expr) {
        Stack<Node> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {

            char ch = expr.charAt(i);

            if (ch == ' ') continue;

            // Operand (numbers or variables)
            if (Character.isLetterOrDigit(ch)) {
                StringBuilder sb = new StringBuilder();

                while (i < expr.length() && Character.isLetterOrDigit(expr.charAt(i))) {
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

            // Operator
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {

                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
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
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter expression: ");
        String input = sc.nextLine();

        Node root = buildAST(input);

        // Reset temp counter for clean output
        tempCount = 1;

        System.out.println("\nThree Address Code:");
        generateTAC(root);
    }
}