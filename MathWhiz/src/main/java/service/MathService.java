package service;

/**
 * All the maths of the game lives here. The game never stores answers:
 * every correct answer is calculated by one of these methods.
 */
public class MathService {

    /** Allowed difference between the player's decimal answer and the real value. */
    public static final double TOLERANCE = 0.01;

    // Marks for each question (total = 10)
    public static final int MARKS_PRIME = 2;
    public static final int MARKS_MOD = 2;
    public static final int MARKS_TRIG_1 = 3;
    public static final int MARKS_TRIG_2 = 3;
    public static final int MAX_SCORE = MARKS_PRIME + MARKS_MOD + MARKS_TRIG_1 + MARKS_TRIG_2;

    /** Returns 1 if the number is prime, otherwise 0. Numbers below 2 are not prime. */
    public static int isPrime(int number) {
        if (number < 2) {
            return 0;
        }
        for (int i = 2; (long) i * i <= number; i++) {
            if (number % i == 0) {
                return 0;
            }
        }
        return 1;
    }

    /** Returns the remainder of a / b (Java % rules). The divisor must not be zero. */
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Divisor must not be zero");
        }
        return a % b;
    }

    /** Sine of an angle given in degrees. */
    public static double calculateSin(double degree) {
        return clean(Math.sin(Math.toRadians(degree)));
    }

    /** Cosine of an angle given in degrees. */
    public static double calculateCos(double degree) {
        return clean(Math.cos(Math.toRadians(degree)));
    }

    /** Tangent of an angle given in degrees. Returns NaN where tangent is undefined (90, 270, ...). */
    public static double calculateTan(double degree) {
        double normalized = ((degree % 180) + 180) % 180;   // 0 <= normalized < 180
        if (Math.abs(normalized - 90) < 1e-9) {
            return Double.NaN;                               // cos = 0, tangent undefined
        }
        return clean(Math.tan(Math.toRadians(degree)));
    }

    /** Calculates "sin", "cos" or "tan" for an angle in degrees. */
    public static double calculateTrig(String function, double degree) {
        switch (function) {
            case "sin":
                return calculateSin(degree);
            case "cos":
                return calculateCos(degree);
            case "tan":
                return calculateTan(degree);
            default:
                throw new IllegalArgumentException("Unknown trigonometric function: " + function);
        }
    }

    /** True when the player's decimal answer is close enough to the expected value. */
    public static boolean isCorrect(double expected, double actual) {
        if (Double.isNaN(expected) || Double.isNaN(actual)) {
            return false;
        }
        return Math.abs(expected - actual) <= TOLERANCE + 1e-9;
    }

    /** Adds up the marks of the correct answers (maximum 10). */
    public static int calculateTotalScore(boolean primeCorrect, boolean modCorrect,
                                          boolean trig1Correct, boolean trig2Correct) {
        int totalScore = 0;

        if (primeCorrect) totalScore += MARKS_PRIME;
        if (modCorrect)   totalScore += MARKS_MOD;
        if (trig1Correct) totalScore += MARKS_TRIG_1;
        if (trig2Correct) totalScore += MARKS_TRIG_2;

        return totalScore;
    }

    /** Rounds tiny floating point noise away (0.49999999999999994 -> 0.5, -1.2e-16 -> 0.0). */
    private static double clean(double value) {
        double rounded = Math.round(value * 1e10) / 1e10;
        return rounded == 0 ? 0.0 : rounded;   // also turns -0.0 into 0.0
    }
}
