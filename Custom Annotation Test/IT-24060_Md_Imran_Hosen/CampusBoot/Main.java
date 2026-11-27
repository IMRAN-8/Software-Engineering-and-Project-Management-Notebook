import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, String> config = new HashMap<>();
        config.put("app.name", "CampusBoot");
        config.put("app.passMark", "40");

        Container container = new Container();
        container.scan(GradeController.class, ReportPrinter.class);
        container.injectValues(config);

        System.out.println("--- Validation ---");

        for (Student s : Arrays.asList(
                new Student(120, 5),
                new Student(85, 5),
                new Student(70, 15))) {

            List<String> errors = Validator.validate(s);

            if (errors.isEmpty()) {
                System.out.println("OK");
            } else {
                for (String error : errors) {
                    System.out.println("VALIDATION: " + error);
                }
            }
        }

        System.out.println("--- Routing ---");

        Router router = new Router();

        for (String path : new String[]{
                "/hello", "/status", "/unknown"}) {
            System.out.println("[Route] " + path + " -> "
                + router.dispatch(container, path));
        }
    }
}
