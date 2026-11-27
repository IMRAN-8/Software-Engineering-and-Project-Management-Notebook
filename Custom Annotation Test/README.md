# CampusBoot --- Custom Annotation Framework

A small Java project demonstrating custom annotations and Java Reflection. It implements a basic dependency-injection container, field validation, and method routing.

## Student Information

  ------------------------------------------------------------------------------------------------
  Field                               Details
  ----------------------------------- ------------------------------------------------------------
  Name                                Md. Imran Hosen

  Student ID                          IT-24060

  University                          Mawlana Bhashani Science and Technology University (MBSTU)

  Department                          Information and Communication Technology (ICT)

  Course Title                        Software Engineering Project Management

  Course Code                         ICT 3107

  Assignment                          Build "CampusBoot" with Custom Annotations
  ------------------------------------------------------------------------------------------------

------------------------------------------------------------------------

## 1. Project Overview

The objective is to build a miniature annotation-driven framework called **CampusBoot** using Java annotations and Reflection.

The project defines four annotations:

  --------------------------------------------------------------------------------------------------
  Annotation              Target                  Purpose
  ----------------------- ----------------------- --------------------------------------------------
  `@Component`            Class                   Marks a class for registration in the container.

  `@Value`                Field                   Injects a value from a configuration map.

  `@Range`                Field                   Defines an allowed minimum and maximum value.

  `@Route`                Method                  Maps a path to a method.
  --------------------------------------------------------------------------------------------------

It also includes three helper components:

-   **Container:** Registers annotated classes, creates objects, and injects configuration values.
-   **Validator:** Checks fields annotated with `@Range`.
-   **Router:** Finds and invokes methods annotated with `@Route`.

## 2. Learning Outcomes

By completing this project, the student demonstrates the ability to:

1.  Define custom Java annotations with suitable targets and retention policies.
2.  Inspect annotations at runtime using Java Reflection.
3.  Create objects dynamically and inject configuration values.
4.  Validate object fields using annotation metadata.
5.  Find and invoke methods based on annotation-defined paths.

## 3. Technologies Used

-   **Language:** Java
-   **Minimum version:** Java 11
-   **Tested runtime:** OpenJDK 21
-   **IDE:** IntelliJ IDEA
-   **Libraries:** Java standard library only
-   **External dependencies:** None

The source code is compatible with Java 15.

## 4. Project Structure

Keep all Java files in the same source directory:

``` text
CampusBoot/
├── Component.java
├── Value.java
├── Range.java
├── Route.java
├── Container.java
├── GradeController.java
├── ReportPrinter.java
├── Student.java
├── Validator.java
├── Router.java
├── Main.java
└── answers.txt
```

## 5. Implementation Details

### Part A --- Custom Annotations

All four annotations use `@Retention(RetentionPolicy.RUNTIME)` so Reflection can read them while the program is running.

#### `Component.java`

``` java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    String value() default "";
}
```

Marks a class for registration in the container. Its optional value defines the bean name.

#### `Value.java`

``` java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {
    String value();
}
```

Specifies the configuration key whose value should be injected into the annotated field.

#### `Range.java`

``` java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
    int min();
    int max();
    String message() default "Value out of range";
}
```

Defines the permitted integer range and an optional validation message.

#### `Route.java`

``` java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String path();
}
```

Associates a path with a method that the Router can invoke.

### Part B --- Container and Value Injection

`Container.java` performs component registration and configuration injection.

1.  `scan()` checks supplied classes for `@Component`.
2.  It creates an object for each annotated class using its no-argument constructor.
3.  It stores each object in a map using the annotation value as its bean name.
4.  `injectValues()` checks registered objects for fields annotated with `@Value`.
5.  It reads the matching configuration value and assigns it to the field using Reflection.

The implementation supports `String` and `int` fields.

`GradeController.java` is the registered component. It receives `app.name` and `app.passMark` from the configuration map.

`ReportPrinter.java` intentionally has no `@Component` annotation, so the container skips it.

### Part C --- Field Validation

`Student.java` contains two fields annotated with `@Range`:

-   `score`: allowed range is 0 to 100.
-   `semester`: allowed range is 1 to 12.

`Validator.java` reads the annotation values using Reflection. If a field value is outside its permitted range, the validator adds an error message to a list.

### Part D --- Method Routing

`Router.java` searches registered components for methods annotated with `@Route`.

-   If the requested path matches an annotated method, the Router invokes that method.
-   If no path matches, it returns a `404 Not Found` message.

`GradeController.java` provides these routes:

  Path        Method       Expected response
  ----------- ------------ -------------------------
  `/hello`    `hello()`    `Welcome to CampusBoot`
  `/status`   `status()`   `Pass mark is 40`

### Part E --- Main Program

`Main.java` demonstrates the complete workflow:

1.  Creates the configuration map.
2.  Registers `GradeController` and attempts to register `ReportPrinter`.
3.  Injects configuration values.
4.  Validates three `Student` objects.
5.  Tests `/hello`, `/status`, and `/unknown`.

## 6. Configuration

The main program uses this configuration:

``` java
Map<String, String> config = new HashMap<>();

config.put("app.name", "CampusBoot");
config.put("app.passMark", "40");
```

These values are injected into the corresponding fields in `GradeController`.

## 7. How to Compile and Run

### IntelliJ IDEA

1.  Open IntelliJ IDEA.
2.  Create or open the `CampusBoot` Java project.
3.  Place all `.java` files in the same source directory.
4.  Select JDK 11 or newer (Java 15 is suitable).
5.  Open `Main.java`.
6.  Run the `main()` method.

### Command Line

Open a terminal in the directory containing the Java files.

Compile:

``` bash
javac *.java
```

Run:

``` bash
java Main
```

No external JAR files or libraries are required.

## 8. Sample Output

``` text
[Container] Registered: gradeController (GradeController)
[Container] Skipped: ReportPrinter (no @Component)
[Container] Injected: app.name -> GradeController.appName
[Container] Injected: app.passMark -> GradeController.passMark
--- Validation ---
VALIDATION: score: Score must be between 0 and 100 (actual: 120)
OK
VALIDATION: semester: Value out of range (actual: 15)
--- Routing ---
[Route] /hello -> Welcome to CampusBoot
[Route] /status -> Pass mark is 40
[Route] /unknown -> 404 Not Found: /unknown
```

The output demonstrates component registration, configuration injection, validation, and route dispatching.

## 9. Reflection Questions

### 1. What happens if `@Retention(RetentionPolicy.RUNTIME)` is forgotten? Why?

Without runtime retention, an annotation uses the default `CLASS` retention. It is stored in the class file but is not available to normal runtime Reflection. Therefore, the framework cannot find it while the program is running.

### 2. Map each CampusBoot annotation to its Spring equivalent.

  CampusBoot annotation   Related Spring annotation
  ----------------------- ---------------------------------------------
  `@Component`            `@Component`
  `@Value`                `@Value`
  `@Range`                `@Min` and `@Max`, or a validation `@Range`
  `@Route`                `@RequestMapping` or `@GetMapping`

These are conceptual equivalents. Spring provides a complete framework with additional features for component scanning, dependency injection, validation, and web request handling.

### 3. Why is `setAccessible(true)` both powerful and risky?

It allows Reflection to access private fields or methods that normal Java access rules would otherwise restrict. This is useful for framework features such as field injection. However, it can bypass encapsulation and access restrictions, so it should be used carefully.

## 10. Assignment Checklist

-   [x] Defined `@Component`.
-   [x] Defined `@Value`.
-   [x] Defined `@Range`.
-   [x] Defined `@Route`.
-   [x] Implemented component registration.
-   [x] Implemented configuration value injection.
-   [x] Implemented range validation.
-   [x] Implemented method routing.
-   [x] Added reflection question answers.

## 11. Submission Information

**Student:** Md. Imran Hosen\
**Student ID:** IT-24060\
**Course:** Software Engineering Project Management\
**Course Code:** ICT 3107\
**Project:** CampusBoot --- Custom Annotation Framework

------------------------------------------------------------------------
