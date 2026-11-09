<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

    <main class="card">
        <h2>Welcome!</h2>
        <p>Answer four maths questions and see how high you can climb on the leaderboard.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert">${error}</div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/game">
            <input type="hidden" name="action" value="start">
            <label for="playerName">Player name</label>
            <input type="text" id="playerName" name="playerName" maxlength="100"
                   placeholder="Enter your name" required autofocus>
            <button type="submit" class="btn">Start Game</button>
        </form>
    </main>

    <section class="card">
        <h3>How to play</h3>
        <table class="grid">
            <tr><th>Question</th><th>Topic</th><th>Marks</th></tr>
            <tr><td>Q1</td><td>Prime number (enter 1 = prime, 0 = not prime)</td><td>2</td></tr>
            <tr><td>Q2</td><td>Modulus (remainder of a whole-number division)</td><td>2</td></tr>
            <tr><td>Q3</td><td>Trigonometry: sin, cos or tan of an angle in degrees</td><td>3</td></tr>
            <tr><td>Q4</td><td>Trigonometry: sin, cos or tan of an angle in degrees</td><td>3</td></tr>
            <tr><td colspan="2"><b>Total</b></td><td><b>10</b></td></tr>
        </table>
        <p class="note">Difficulty: <span class="badge">NORMAL</span>
            &nbsp;Decimal answers may be off by up to 0.01.</p>
    </section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
