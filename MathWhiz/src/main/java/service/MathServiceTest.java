package service;

/**
 * Simple test runner for Task 09. Run the main method and take a screenshot of the output.
 */
public class MathServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        if (ok) {
            passed++;
        } else {
            failed++;
        }
        System.out.printf("%-34s expected: %-22s actual: %-22s %s%n",
                name, expected, actual, ok ? "PASS" : "FAIL");
    }

    private static void checkClose(String name, double expected, double actual) {
        check(name, String.valueOf(expected), String.valueOf(actual));
    }

    public static void main(String[] args) {
        System.out.println("=== Prime number ===");
        check("isPrime(7)", "1", String.valueOf(MathService.isPrime(7)));
        check("isPrime(8)", "0", String.valueOf(MathService.isPrime(8)));
        check("isPrime(1)", "0", String.valueOf(MathService.isPrime(1)));
        check("isPrime(2)", "1", String.valueOf(MathService.isPrime(2)));
        check("isPrime(0)", "0", String.valueOf(MathService.isPrime(0)));
        check("isPrime(-5)", "0", String.valueOf(MathService.isPrime(-5)));
        check("isPrime(97)", "1", String.valueOf(MathService.isPrime(97)));
        check("isPrime(100)", "0", String.valueOf(MathService.isPrime(100)));

        System.out.println("\n=== Modulus ===");
        check("calculateMod(17, 5)", "2", String.valueOf(MathService.calculateMod(17, 5)));
        check("calculateMod(20, 5)", "0", String.valueOf(MathService.calculateMod(20, 5)));
        check("calculateMod(-17, 5)", "-2", String.valueOf(MathService.calculateMod(-17, 5)));
        check("calculateMod(17, -5)", "2", String.valueOf(MathService.calculateMod(17, -5)));
        check("calculateMod(0, 7)", "0", String.valueOf(MathService.calculateMod(0, 7)));
        String zeroResult;
        try {
            MathService.calculateMod(17, 0);
            zeroResult = "no error";
        } catch (ArithmeticException e) {
            zeroResult = "Validation error";
        }
        check("calculateMod(17, 0)", "Validation error", zeroResult);

        System.out.println("\n=== Trigonometry (degrees) ===");
        checkClose("calculateSin(30)", 0.5, MathService.calculateSin(30));
        checkClose("calculateSin(90)", 1.0, MathService.calculateSin(90));
        checkClose("calculateSin(180)", 0.0, MathService.calculateSin(180));
        checkClose("calculateCos(60)", 0.5, MathService.calculateCos(60));
        checkClose("calculateCos(0)", 1.0, MathService.calculateCos(0));
        checkClose("calculateCos(90)", 0.0, MathService.calculateCos(90));
        checkClose("calculateTan(45)", 1.0, MathService.calculateTan(45));
        checkClose("calculateTan(0)", 0.0, MathService.calculateTan(0));
        check("calculateTan(90) undefined", "NaN", String.valueOf(MathService.calculateTan(90)));
        check("calculateTan(270) undefined", "NaN", String.valueOf(MathService.calculateTan(270)));

        System.out.println("\n=== Answer checking and score ===");
        check("isCorrect(0.7071, 0.71)", "true", String.valueOf(MathService.isCorrect(0.7071, 0.71)));
        check("isCorrect(0.5, 0.6)", "false", String.valueOf(MathService.isCorrect(0.5, 0.6)));
        check("All answers correct", "10", String.valueOf(MathService.calculateTotalScore(true, true, true, true)));
        check("All answers wrong", "0", String.valueOf(MathService.calculateTotalScore(false, false, false, false)));
        check("Only prime + mod correct", "4", String.valueOf(MathService.calculateTotalScore(true, true, false, false)));
        check("Only the two trig correct", "6", String.valueOf(MathService.calculateTotalScore(false, false, true, true)));

        System.out.println("\nPassed: " + passed + "   Failed: " + failed);
    }
}
