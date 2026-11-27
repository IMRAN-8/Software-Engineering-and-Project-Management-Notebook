import java.lang.reflect.*;

public class Router {
    public String dispatch(Container container, String path) {
        for (Object obj : container.getBeans()) {
            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Route.class)) {
                    continue;
                }

                Route route = method.getAnnotation(Route.class);

                if (route.path().equals(path)) {
                    try {
                        method.setAccessible(true);
                        return (String) method.invoke(obj);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return "404 Not Found: " + path;
    }
}
