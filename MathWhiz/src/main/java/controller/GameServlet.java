package controller;

import dao.ScoreDAO;
import model.GameScore;
import model.QuestionResult;
import model.Quiz;
import service.MathService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller of the game.
 *   POST /game  action=start   -> create four questions, show game.jsp
 *   POST /game  action=submit  -> check answers, save the score, show result.jsp
 *   GET  /leaderboard          -> show the top 10 scores in leaderboard.jsp
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/game", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private static final int LEADERBOARD_SIZE = 10;
    private static final int MAX_NAME_LENGTH = 100;

    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("/leaderboard".equals(req.getServletPath())) {
            showLeaderboard(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("start".equals(action)) {
            startGame(req, resp);
        } else if ("submit".equals(action)) {
            submitAnswers(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    // ---------------------------------------------------------------- start game
    private void startGame(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("playerName");
        name = (name == null) ? "" : name.trim();

        if (name.isEmpty()) {
            req.setAttribute("error", "Please enter your name to start the game.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        if (name.length() > MAX_NAME_LENGTH) {
            req.setAttribute("error", "Your name must be at most " + MAX_NAME_LENGTH + " characters.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        // Keep the four questions in the session (only the questions, not the answers)
        HttpSession session = req.getSession(true);
        session.setAttribute("quiz", Quiz.generate(name));

        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    // ------------------------------------------------------------ submit answers
    private void submitAnswers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Quiz quiz = (session == null) ? null : (Quiz) session.getAttribute("quiz");
        if (quiz == null) {
            // No running game (for example the page was refreshed after finishing)
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String primeText = clean(req.getParameter("primeAnswer"));
        String modText = clean(req.getParameter("modAnswer"));
        String trig1Text = clean(req.getParameter("trig1Answer"));
        String trig2Text = clean(req.getParameter("trig2Answer"));

        // ---- validate the input
        int primeAnswer;
        int modAnswer;
        double trig1Answer;
        double trig2Answer;
        try {
            primeAnswer = Integer.parseInt(primeText);
            modAnswer = Integer.parseInt(modText);
            trig1Answer = parseDecimal(trig1Text);
            trig2Answer = parseDecimal(trig2Text);
        } catch (NumberFormatException e) {
            showGameWithError(req, resp, "Please answer all four questions with numbers. "
                    + "Questions 1 and 2 need whole numbers.");
            return;
        }
        if (primeAnswer != 0 && primeAnswer != 1) {
            showGameWithError(req, resp, "Question 1: enter 1 if the number is prime, or 0 if it is not.");
            return;
        }

        // ---- calculate the correct answers with the Java methods (nothing is stored)
        int primeExpected = MathService.isPrime(quiz.getPrimeNumber());
        int modExpected = MathService.calculateMod(quiz.getModA(), quiz.getModB());
        double trig1Expected = MathService.calculateTrig(quiz.getTrig1Fn(), quiz.getTrig1Angle());
        double trig2Expected = MathService.calculateTrig(quiz.getTrig2Fn(), quiz.getTrig2Angle());

        boolean primeCorrect = (primeAnswer == primeExpected);
        boolean modCorrect = (modAnswer == modExpected);
        boolean trig1Correct = MathService.isCorrect(trig1Expected, trig1Answer);
        boolean trig2Correct = MathService.isCorrect(trig2Expected, trig2Answer);

        int totalScore = MathService.calculateTotalScore(primeCorrect, modCorrect, trig1Correct, trig2Correct);

        // ---- build the result rows for result.jsp
        List<QuestionResult> results = new ArrayList<QuestionResult>();
        results.add(new QuestionResult(
                "Is " + quiz.getPrimeNumber() + " a prime number? (1 = prime, 0 = not prime)",
                primeText, String.valueOf(primeExpected), primeCorrect, MathService.MARKS_PRIME));
        results.add(new QuestionResult(
                "What is " + quiz.getModA() + " % " + quiz.getModB() + " ?",
                modText, String.valueOf(modExpected), modCorrect, MathService.MARKS_MOD));
        results.add(new QuestionResult(
                "Calculate " + quiz.getTrig1Text() + " (2 decimal places)",
                trig1Text, formatDecimal(trig1Expected), trig1Correct, MathService.MARKS_TRIG_1));
        results.add(new QuestionResult(
                "Calculate " + quiz.getTrig2Text() + " (2 decimal places)",
                trig2Text, formatDecimal(trig2Expected), trig2Correct, MathService.MARKS_TRIG_2));

        // ---- save the score in the database (a database problem must not hide the result)
        boolean saved = true;
        try {
            scoreDAO.saveScore(new GameScore(quiz.getPlayerName(), totalScore, Quiz.DIFFICULTY));
        } catch (SQLException e) {
            saved = false;
            log("Could not save the score", e);
        }

        // The game is finished: remove the questions so a refresh cannot save the score twice
        session.removeAttribute("quiz");

        req.setAttribute("playerName", quiz.getPlayerName());
        req.setAttribute("score", totalScore);
        req.setAttribute("maxScore", MathService.MAX_SCORE);
        req.setAttribute("difficulty", Quiz.DIFFICULTY);
        req.setAttribute("results", results);
        req.setAttribute("saved", saved);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
    }

    // ------------------------------------------------------------- leaderboard
    private void showLeaderboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            req.setAttribute("scores", scoreDAO.getTopScores(LEADERBOARD_SIZE));
        } catch (SQLException e) {
            log("Could not read the leaderboard", e);
            req.setAttribute("dbError",
                    "The leaderboard could not be loaded. Check that MySQL is running and the "
                            + "database settings in ScoreDAO.java are correct.");
        }
        req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
    }

    // ----------------------------------------------------------------- helpers
    private void showGameWithError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    private static String clean(String text) {
        return text == null ? "" : text.trim();
    }

    /** Reads a decimal number; a comma is accepted as the decimal separator. */
    private static double parseDecimal(String text) {
        double value = Double.parseDouble(text.replace(',', '.'));
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new NumberFormatException("Not a finite number");
        }
        return value;
    }

    private static String formatDecimal(double value) {
        return Double.isNaN(value) ? "undefined" : String.format(Locale.US, "%.4f", value);
    }
}
