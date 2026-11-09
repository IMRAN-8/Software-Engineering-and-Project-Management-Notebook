package dao;

import model.GameScore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Saves and reads game scores using JDBC. */
public class ScoreDAO {

    // ---- Database configuration: change the password to your MySQL root password ----
    private static final String URL = "jdbc:mysql://localhost:3306/math_whiz_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka";
    private static final String USER = "root";
    private static final String PASSWORD = "PASSWORD";

    // Inside Tomcat the driver must be loaded by name, otherwise "No suitable driver" can appear
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** INSERT one finished game (player name, score, difficulty and date/time). */
    public void saveScore(GameScore gameScore) throws SQLException {
        String sql = "INSERT INTO game_scores (player_name, score, difficulty, played_at) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, gameScore.getPlayerName());
            ps.setInt(2, gameScore.getScore());
            ps.setString(3, gameScore.getDifficulty());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    /** SELECT the best scores: highest first, earliest time first when scores are equal. */
    public List<GameScore> getTopScores(int limit) throws SQLException {
        String sql = "SELECT id, player_name, score, difficulty, played_at "
                + "FROM game_scores "
                + "ORDER BY score DESC, played_at ASC "
                + "LIMIT ?";

        List<GameScore> scores = new ArrayList<GameScore>();

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GameScore gs = new GameScore();
                    gs.setId(rs.getInt("id"));
                    gs.setPlayerName(rs.getString("player_name"));
                    gs.setScore(rs.getInt("score"));
                    gs.setDifficulty(rs.getString("difficulty"));
                    gs.setPlayedAt(rs.getTimestamp("played_at"));
                    scores.add(gs);
                }
            }
        }
        return scores;
    }
}
