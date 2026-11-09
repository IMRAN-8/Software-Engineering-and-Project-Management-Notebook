<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" import="model.Quiz, util.Html" %>
<%
    // No running game? Go back to the welcome page.
    Quiz quiz = (Quiz) session.getAttribute("quiz");
    if (quiz == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

    <main class="card">
        <h2>Good luck, <%= Html.escape(quiz.getPlayerName()) %>!</h2>
        <p>Difficulty: <span class="badge">${quiz.difficulty}</span></p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert">${error}</div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/game">
            <input type="hidden" name="action" value="submit">

            <div class="question">
                <div class="qhead"><span>Question 1 &middot; Prime number</span><span class="marks">2 marks</span></div>
                <p>Is <b>${quiz.primeNumber}</b> a prime number?</p>
                <input type="number" name="primeAnswer" min="0" max="1" step="1"
                       placeholder="1 = prime, 0 = not prime" required>
            </div>

            <div class="question">
                <div class="qhead"><span>Question 2 &middot; Modulus</span><span class="marks">2 marks</span></div>
                <p>What is the remainder of <b>${quiz.modA} % ${quiz.modB}</b> ?</p>
                <input type="number" name="modAnswer" step="1" placeholder="Whole number" required>
            </div>

            <div class="question">
                <div class="qhead"><span>Question 3 &middot; Trigonometry</span><span class="marks">3 marks</span></div>
                <p>Calculate <b>${quiz.trig1Text}</b> (angle in degrees, 2 decimal places).</p>
                <input type="number" name="trig1Answer" step="any" placeholder="e.g. 0.5" required>
            </div>

            <div class="question">
                <div class="qhead"><span>Question 4 &middot; Trigonometry</span><span class="marks">3 marks</span></div>
                <p>Calculate <b>${quiz.trig2Text}</b> (angle in degrees, 2 decimal places).</p>
                <input type="number" name="trig2Answer" step="any" placeholder="e.g. 0.87" required>
            </div>

            <button type="submit" class="btn">Submit Answers</button>
        </form>
    </main>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
