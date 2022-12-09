package lesson7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        start(TestClass1.class);
        System.out.println("\n\n");
        start(TestClass4.class);
        System.out.println("\n\n");

        /*Exception because no BeforeSuite annotation*/
//        start(TestClass2.class);
//        System.out.println("\n\n");

        /*Exception because no AfterSuite annotation */
        start(TestClass3.class);
    }

    public static void start(Class testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        int counterBeforeSuite = 0;
        int counterAfterSuite = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                counterBeforeSuite++;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                counterAfterSuite++;
            }
            if (counterBeforeSuite > 1) {
                throw new RuntimeException("Theres many BeforeSuite annotation");
            } else if (counterAfterSuite > 1) {
                throw new RuntimeException("Theres many AfterSuite annotation");
            }
        }
        if (counterBeforeSuite < 1) {
            throw new RuntimeException("Theres no BeforeSuite annotation");
        } else if (counterAfterSuite < 1) {
            throw new RuntimeException("Theres no AfterSuite annotation");
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                try {
                    method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        Map<Method, Integer> map = new HashMap<>();
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(Test.class)) {
//                int priority = method.getAnnotation(Test.class).priority();
//                if (priority > 0 && priority <= 10) {
//                    map.put(method, priority);
//                }
//            }
//        }
//        for (int i = 10; i > 0; i--) {
//            for (Map.Entry<Method, Integer> m : map.entrySet()) {
//                if (i == m.getValue()) {
//                    try {
//                        System.out.println("Priority: " + m.getValue());
//                        m.getKey().invoke(null);
//                    } catch (IllegalAccessException | InvocationTargetException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
        Map<Integer, ArrayList<Method>> map = new TreeMap<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                ArrayList<Method> list = new ArrayList<>();
                int priority = method.getAnnotation(Test.class).priority();
                if (map.get(priority) != null) {
                    list = map.getOrDefault(priority, null);
                }
                if (priority > 0 && priority <= 10) {
                    list.add(method);
                }
                map.put(priority, list);
            }
        }
        for (int i = 10; i > 0; i--) {
            if (map.get(i) != null) {
                ArrayList<Method> list = map.get(i);
                for (Method method : list) {
                    try {
                        System.out.println("Priority: " + i);
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(AfterSuite.class)) {
                try {
                    method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
