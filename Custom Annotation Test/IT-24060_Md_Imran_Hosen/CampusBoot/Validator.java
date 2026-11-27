import java.lang.reflect.*;
import java.util.*;

public class Validator {
    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();

        for (Field f : obj.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Range.class)) {
                continue;
            }

            try {
                f.setAccessible(true);
                int value = f.getInt(obj);
                Range range = f.getAnnotation(Range.class);

                if (value < range.min() || value > range.max()) {
                    errors.add(f.getName() + ": " + range.message()
                        + " (actual: " + value + ")");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return errors;
    }
}
