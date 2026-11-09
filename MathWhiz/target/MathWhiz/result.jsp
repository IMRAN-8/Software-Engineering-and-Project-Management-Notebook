<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List, model.QuestionResult, util.Html" %>
<%
    // Opened directly without finishing a game? Go back to the welcome page.
    @SuppressWarnings("unchecked")
    List<QuestionResult> results = (List<QuestionResult>) request.getAttribute("results");
    if (results == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    int score = (Integer) request.getAttribute("score");
    int maxScore = (Integer) request.getAttribute("maxScore");
    boolean saved = Boolean.TRUE.equals(request.getAttribute("saved"));
%>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

    <main class="card center">
        <h2>Result</h2>
        <p class="player">Player: <b><%= Html.escape((String) request.getAttribute("playerName")) %></b></p>
        <div class="score"><%= score %> <span>/ <%= maxScore %></span></div>
        <p>Difficulty: <span class="badge">${difficulty}</span></p>
        <% if (score == maxScore) { %>
            <p class="good">Perfect score! Well done!</p>
        <% } else if (score >= 6) { %>
            <p class="good">Great job!</p>
        <% } else { %>
            <p>Keep practising &ndash; you can do better next time!</p>
        <% } %>

        <% if (saved) { %>
            <p class="note">Your score was saved to the leaderboard database.</p>
        <% } else { %>
            <div class="alert">Your score could not be saved to the database.
                Check that MySQL is running and the settings in ScoreDAO.java are correct.</div>
        <% } %>
    </main>

    <section class="card">
        <h3>Your answers</h3>
        <div class="scroll">
        <table class="grid">
            <tr><th>Question</th><th>Your answer</th><th>Correct answer</th><th>Marks</th></tr>
            <% for (QuestionResult r : results) { %>
            <tr class="<%= r.isCorrect() ? "ok" : "bad" %>">
                <td><%= Html.escape(r.getQuestion()) %></td>
                <td><%= Html.escape(r.getYourAnswer()) %></td>
                <td><%= Html.escape(r.getCorrectAnswer()) %></td>
                <td><%= r.getMarks() %> / <%= r.getMaxMarks() %></td>
            </tr>
            <% } %>
        </table>
        </div>
        <div class="actions">
            <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Again</a>
            <a class="btn alt" href="${pageContext.request.contextPath}/leaderboard">View Leaderboard</a>
        </div>
    </section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
