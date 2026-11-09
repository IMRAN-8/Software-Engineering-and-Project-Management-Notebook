package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The four questions of one game (what is asked, never the answers).
 * It is kept in the player's session; the answers are calculated by MathService on submit.
 */
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Only one difficulty exists for now, but it is stored in the database. */
    public static final String DIFFICULTY = "NORMAL";

    // Angles (degrees) that give clean values. Tangent avoids 90 and 270 (undefined).
    private static final int[] SIN_COS_ANGLES =
            {0, 30, 45, 60, 90, 120, 135, 150, 180, 210, 225, 240, 270, 300, 315, 330, 360};
    private static final int[] TAN_ANGLES =
            {0, 15, 30, 45, 60, 75, 105, 120, 135, 150, 165, 180};

    private final String playerName;
    private final int primeNumber;   // Q1: is this number prime?
    private final int modA;          // Q2: modA % modB
    private final int modB;
    private final String trig1Fn;    // Q3: sin / cos / tan
    private final int trig1Angle;
    private final String trig2Fn;    // Q4: sin / cos / tan (a different function from Q3)
    private final int trig2Angle;

    private Quiz(String playerName, int primeNumber, int modA, int modB,
                 String trig1Fn, int trig1Angle, String trig2Fn, int trig2Angle) {
        this.playerName = playerName;
        this.primeNumber = primeNumber;
        this.modA = modA;
        this.modB = modB;
        this.trig1Fn = trig1Fn;
        this.trig1Angle = trig1Angle;
        this.trig2Fn = trig2Fn;
        this.trig2Angle = trig2Angle;
    }

    /** Creates four random questions: one prime, one modulus and two trigonometric. */
    public static Quiz generate(String playerName) {
        Random random = new Random();

        int primeNumber = 1 + random.nextInt(60);   // 1 .. 60
        int modB = 2 + random.nextInt(8);            // 2 .. 9  (never zero)
        int modA = 10 + random.nextInt(90);          // 10 .. 99

        List<String> functions = new ArrayList<String>(Arrays.asList("sin", "cos", "tan"));
        Collections.shuffle(functions, random);
        String fn1 = functions.get(0);
        String fn2 = functions.get(1);

        return new Quiz(playerName, primeNumber, modA, modB,
                fn1, pickAngle(fn1, random), fn2, pickAngle(fn2, random));
    }

    private static int pickAngle(String function, Random random) {
        int[] angles = "tan".equals(function) ? TAN_ANGLES : SIN_COS_ANGLES;
        return angles[random.nextInt(angles.length)];
    }

    /** Text like "sin(30°)". */
    public static String trigText(String function, int angle) {
        return function + "(" + angle + "\u00b0)";
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPrimeNumber() {
        return primeNumber;
    }

    public int getModA() {
        return modA;
    }

    public int getModB() {
        return modB;
    }

    public String getTrig1Fn() {
        return trig1Fn;
    }

    public int getTrig1Angle() {
        return trig1Angle;
    }

    public String getTrig2Fn() {
        return trig2Fn;
    }

    public int getTrig2Angle() {
        return trig2Angle;
    }

    public String getTrig1Text() {
        return trigText(trig1Fn, trig1Angle);
    }

    public String getTrig2Text() {
        return trigText(trig2Fn, trig2Angle);
    }

    public String getDifficulty() {
        return DIFFICULTY;
    }
}
