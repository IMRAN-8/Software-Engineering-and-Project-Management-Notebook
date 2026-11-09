<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List, java.text.SimpleDateFormat, model.GameScore, util.Html" %>
<%
    @SuppressWarnings("unchecked")
    List<GameScore> scores = (List<GameScore>) request.getAttribute("scores");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
%>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

    <main class="card">
        <h2>Top 10 Leaderboard</h2>

        <% if (request.getAttribute("dbError") != null) { %>
            <div class="alert">${dbError}</div>
        <% } else if (scores == null || scores.isEmpty()) { %>
            <p>No scores yet. Be the first to play!</p>
        <% } else { %>
            <div class="scroll">
            <table class="grid">
                <tr><th>Rank</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Date / Time</th></tr>
                <% int rank = 1; for (GameScore gs : scores) { %>
                <tr>
                    <td><%= rank++ %></td>
                    <td><%= Html.escape(gs.getPlayerName()) %></td>
                    <td><b><%= gs.getScore() %></b> / 10</td>
                    <td><%= Html.escape(gs.getDifficulty()) %></td>
                    <td><%= gs.getPlayedAt() == null ? "" : dateFormat.format(gs.getPlayedAt()) %></td>
                </tr>
                <% } %>
            </table>
            </div>
        <% } %>

        <div class="actions">
            <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Game</a>
        </div>
    </main>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
