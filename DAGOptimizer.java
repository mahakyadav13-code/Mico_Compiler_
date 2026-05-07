import java.util.*;

class Expr {
    String op, left, right;

    Expr(String op, String left, String right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}

public class DAGOptimizer {

    static int tempCount = 1;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of TAC lines:");
        int n = sc.nextInt();
        sc.nextLine();

        List<String> input = new ArrayList<>();

        System.out.println("Enter TAC statements (e.g., t1=a*b):");
        for (int i = 0; i < n; i++) {
            input.add(sc.nextLine().replaceAll(" ", ""));
        }

        Map<String, String> exprMap = new HashMap<>();
        List<String> optimized = new ArrayList<>();

        for (String line : input) {

            // Split t1=a*b
            String parts[] = line.split("=");
            String lhs = parts[0];
            String rhs = parts[1];

            // Check if binary operation
            String op = "", left = "", right = "";

            for (char c : rhs.toCharArray()) {
                if ("+-*/".indexOf(c) != -1) {
                    op = String.valueOf(c);
                }
            }

            if (!op.equals("")) {
                String operands[] = rhs.split("\\" + op);
                left = operands[0];
                right = operands[1];

                String key = left + op + right;

                // Check for common subexpression
                if (exprMap.containsKey(key)) {
                    String existing = exprMap.get(key);
                    optimized.add(lhs + " = " + existing);
                } else {
                    exprMap.put(key, lhs);
                    optimized.add(lhs + " = " + left + " " + op + " " + right);
                }
            } else {
                // Simple assignment
                optimized.add(line);
            }
        }

        System.out.println("\nOptimized TAC:");
        for (String line : optimized) {
            System.out.println(line);
        }
    }
}