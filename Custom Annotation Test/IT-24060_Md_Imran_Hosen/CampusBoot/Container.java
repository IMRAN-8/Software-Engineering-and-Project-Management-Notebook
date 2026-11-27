import java.lang.reflect.*;
import java.util.*;

public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> c : classes) {
            if (!c.isAnnotationPresent(Component.class)) {
                System.out.println("[Container] Skipped: " +
                    c.getSimpleName() + " (no @Component)");
                continue;
            }

            try {
                Component comp = c.getAnnotation(Component.class);
                String name = comp.value();

                if (name.isEmpty()) {
                    name = Character.toLowerCase(c.getSimpleName().charAt(0))
                        + c.getSimpleName().substring(1);
                }

                Object obj = c.getDeclaredConstructor().newInstance();
                beans.put(name, obj);

                System.out.println("[Container] Registered: " + name +
                    " (" + c.getSimpleName() + ")");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object obj : beans.values()) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                if (!f.isAnnotationPresent(Value.class)) {
                    continue;
                }

                try {
                    Value annotation = f.getAnnotation(Value.class);
                    String value = config.get(annotation.value());

                    if (value == null) {
                        throw new IllegalArgumentException(
                            "Missing configuration key: " + annotation.value());
                    }

                    f.setAccessible(true);

                    if (f.getType() == String.class) {
                        f.set(obj, value);
                    } else if (f.getType() == int.class) {
                        f.setInt(obj, Integer.parseInt(value));
                    } else {
                        throw new IllegalArgumentException(
                            "Unsupported @Value field type: " + f.getType());
                    }

                    System.out.println("[Container] Injected: " +
                        annotation.value() + " -> " +
                        obj.getClass().getSimpleName() + "." + f.getName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    // Used by Router to search all registered components.
    public Collection<Object> getBeans() {
        return beans.values();
    }
}
