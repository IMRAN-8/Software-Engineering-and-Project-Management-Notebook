# Lab Practice #1: JDBC-Servlet Recalling

**Full Name:** MD. IMRAN HOSEN

- **Student ID:** IT_24060
- **Course:** ICT 3108 - Software Engineering Project Management (SEPM), Department of ICT, MBSTU
- **Date:** November 1, 2026

This repository contains four tasks:

| Task | Topic | Technology |
|------|-------|------------|
| Task 1 | Install IntelliJ IDEA Ultimate | IntelliJ IDEA Ultimate, JetBrains Student Licence |
| Task 2 | Find the nth prime and store it in a database | Java, JDBC, MySQL |
| Task 3 | Configure Apache Tomcat in IntelliJ | Apache Tomcat 10.1, IntelliJ IDEA |
| Task 4 | Primality checker web page | Java Servlet, Tomcat |

---

## Task 1: Install IntelliJ IDEA Ultimate

### Description
IntelliJ IDEA Ultimate is the JetBrains IDE used for all the tasks in this lab. The Ultimate edition is required because it includes application-server (Tomcat) support and the Database tool window.

### Steps
1. Downloaded the Windows installer from `https://www.jetbrains.com/idea/download/` and installed it with the default options (desktop shortcut and PATH launcher enabled).
2. Applied for the **JetBrains Student Licence** at `https://www.jetbrains.com/community/education/#students` using my university email address.
3. Confirmed the licence from the verification email and activated it by signing in with my JetBrains account in IntelliJ (**Help > Manage Licenses**).
4. Verified the setup by opening **Help > About**, which shows *IntelliJ IDEA Ultimate* with the educational licence, and by running a test class.

### Screenshots
![Screenshot 1](./Screenshot%202026-11-01%20104402.png)

---

## Task 2: JDBC - Nth Prime Stored in MySQL

### Description
A console program that reads a positive integer `n`, finds the **nth prime number**, inserts it into the MySQL table `nickname_name_prime` using JDBC, and then retrieves and displays the stored records to verify the insertion.

For example, `n = 10` gives `29` (the primes are 2, 3, 5, 7, 11, 13, 17, 19, 23, 29).

### Requirements
- JDK 21
- MySQL Server 8.0 running on `localhost:3306`
- MySQL Connector/J (JDBC driver)

### Database Setup
```sql
CREATE DATABASE IF NOT EXISTS lab_db;
USE lab_db;

CREATE TABLE nickname_name_prime (
    id INT AUTO_INCREMENT PRIMARY KEY,
    n INT NOT NULL,
    nth_prime INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### JDBC Driver (Maven `pom.xml`)
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
```

### Code (`NthPrimeJDBC.java`)
```java
import java.sql.*;
import java.util.Scanner;

public class NthPrimeJDBC {

    // ---- Database configuration ----
    static final String URL   = "jdbc:mysql://localhost:3306/lab_db";
    static final String USER  = "root";
    static final String PASS  = "YOUR_MYSQL_PASSWORD";
    static final String TABLE = "nickname_name_prime";

    // Returns true if num is prime (trial division up to sqrt(num))
    static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Finds the n-th prime number
    static int nthPrime(int n) {
        int count = 0;
        int candidate = 1;
        while (count < n) {
            candidate++;
            if (isPrime(candidate)) {
                count++;
            }
        }
        return candidate;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter n: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("Enter a positive number!");
            return;
        }

        int prime = nthPrime(n);
        System.out.println("The " + n + "-th prime is " + prime);

        // try-with-resources closes the connection automatically
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.println("Connected to database.");

            // Insert the result using a PreparedStatement
            String insertSql = "INSERT INTO " + TABLE + " (n, nth_prime) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, n);
                ps.setInt(2, prime);
                int rows = ps.executeUpdate();
                System.out.println(rows + " row inserted.");
            }

            // Retrieve and display the stored records to verify the insertion
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE)) {

                System.out.println("ID | n | nth_prime | created_at");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " | "
                            + rs.getInt("n") + " | "
                            + rs.getInt("nth_prime") + " | "
                            + rs.getTimestamp("created_at"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        sc.close();
    }
}
```

### Code Explanation
- **`isPrime`** returns `false` for numbers below 2, then tests divisors from 2 up to the square root of the number. If none divides it evenly, the number is prime.
- **`nthPrime`** counts primes one by one until it reaches the nth, then returns it.
- **`DriverManager.getConnection`** opens the JDBC connection to the `lab_db` database.
- **`PreparedStatement`** runs `INSERT INTO nickname_name_prime (n, nth_prime) VALUES (?, ?)`. The `?` placeholders are filled with `setInt`, which is safe against SQL injection.
- **`SELECT * FROM ...`** reads the table back and prints every stored record, which verifies that the insertion worked.

### Sample Output (n = 10)
```
Enter n: 10
The 10-th prime is 29
Connected to database.
1 row inserted.
ID | n | nth_prime | created_at
1 | 10 | 29 | <timestamp>
```

### Database Output
```sql
SELECT * FROM nickname_name_prime;
```

---

## Task 3: Configure Apache Tomcat in IntelliJ

### Description
Apache Tomcat is the application server that runs the Java servlets. In this task Tomcat was installed and connected to IntelliJ IDEA Ultimate as a local application server.

### Configuration Details
| Setting | Value |
|---------|-------|
| Server | Apache Tomcat 10.1.x (uses `jakarta.servlet` packages) |
| Tomcat Home | `C:\tomcat\apache-tomcat-10.1.x` |
| HTTP Port | 8080 |
| Deployment artifact | `<ProjectName>:war exploded` |
| Application context | `/<ProjectName>_war_exploded` |

### Steps
1. Downloaded the Tomcat 10.1 **zip** (Core) from `https://tomcat.apache.org/` and extracted it to `C:\tomcat`.
2. In IntelliJ, opened **Run > Edit Configurations > + > Tomcat Server > Local**.
3. Clicked **Configure...** next to *Application server* and selected the Tomcat home folder.
4. Kept the HTTP port at **8080** in the *Server* tab.
5. In the *Deployment* tab, clicked **+ > Artifact** and chose the `war exploded` artifact.
6. Clicked **Run** to start the server.

### Verification
- The IntelliJ Run window shows `Server startup in [xxx] milliseconds`, which confirms Tomcat started successfully.
- The deployed application opens in the browser at `http://localhost:8080/<ProjectName>_war_exploded/`.


---

## Task 4: Primality Checker Servlet

### Description
A Java servlet (`PrimeServlet`) mapped to `/prime`. It shows a form where the user enters an integer, then reports whether the number is prime. The page is deployed on Apache Tomcat and opened through a web browser.

### Code (`PrimeServlet.java`)
```java
package com.example.primelab;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Servlet mapping: the servlet is reachable at <context-path>/prime
@WebServlet(name = "PrimeServlet", urlPatterns = {"/prime"})
public class PrimeServlet extends HttpServlet {

    // Trial division primality test
    private boolean isPrime(long num) {
        if (num < 2) {
            return false;
        }
        for (long i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // Read the number typed by the user (null on the first visit)
        String param = req.getParameter("number");

        out.println("<html><body style='font-family:Arial;margin:40px'>");
        out.println("<h2>Prime Number Checker</h2>");

        // Input form
        out.println("<form method='get' action='prime'>"
                + "<input type='number' name='number' placeholder='Enter an integer' required> "
                + "<button type='submit'>Check</button></form><br>");

        // Show the result if a number was submitted
        if (param != null && !param.isBlank()) {
            try {
                long number = Long.parseLong(param.trim());
                boolean prime = isPrime(number);
                out.println("<h3 style='color:" + (prime ? "green" : "red") + "'>"
                        + number + " is " + (prime ? "a PRIME" : "NOT a prime") + " number.</h3>");
            } catch (NumberFormatException e) {
                out.println("<h3 style='color:orange'>Invalid input. Please enter an integer.</h3>");
            }
        }

        out.println("</body></html>");
    }
}
```

### Servlet Mapping
The mapping is done with the `@WebServlet("/prime")` annotation above. The equivalent `web.xml` configuration would be:

```xml
<servlet>
    <servlet-name>PrimeServlet</servlet-name>
    <servlet-class>com.example.primelab.PrimeServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>PrimeServlet</servlet-name>
    <url-pattern>/prime</url-pattern>
</servlet-mapping>
```

### Code Explanation
- **`@WebServlet`** registers the servlet at the URL path `/prime`.
- **`doGet`** runs when the browser requests the page. It writes an HTML form, then reads the `number` parameter.
- **`isPrime`** returns `false` for numbers below 2 and otherwise checks divisors up to the square root.
- Valid input is shown in green (prime) or red (not prime). Text that is not a number triggers `NumberFormatException`, and the page shows an *Invalid input* message.

### How to Run
1. Deploy the project on Tomcat as in Task 3.
2. Open `http://localhost:8080/<ProjectName>_war_exploded/prime` in a browser.
3. Enter a number and click **Check**.

### Browser Test Results
| Input | Expected Result |
|-------|-----------------|
| 17 | 17 is a PRIME number. |
| 97 | 97 is a PRIME number. |
| 100 | 100 is NOT a prime number. |
| 1 | 1 is NOT a prime number. |
| abc | Invalid input (the browser blocks non-numeric text in a number field) |

---

## Author
MD. Imran Hosen 
- Student ID: IT_24060
