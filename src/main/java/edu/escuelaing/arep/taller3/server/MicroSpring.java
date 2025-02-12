package edu.escuelaing.arep.taller3.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.escuelaing.arep.taller3.http.HttpRequest;
import edu.escuelaing.arep.taller3.server.annotations.GetMapping;
import edu.escuelaing.arep.taller3.server.annotations.RestController;


public class MicroSpring {

    private static Map<String, Method> services = new HashMap<>();
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        ClassFileScanner.listClasses(); 
        loadMethods();
    }

    private static void loadMethods() {
        List<String> classPaths = ClassFileScanner.getClassPaths();
        for (String classPath : classPaths) {
            processClassPath(classPath);
        }
    }

    private static void processClassPath(String classPath) {
        boolean isRestController = true;
        while (isRestController) {
            try {
                Class<?> c = Class.forName(classPath);
                if (!c.isAnnotationPresent(RestController.class)) {
                    isRestController = false;
                }
                processMethods(c);
                isRestController = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                isRestController = false;
            }
        }
    }

    private static void processMethods(Class<?> c) {
        for (Method m : c.getDeclaredMethods()) {
            String path = "";
            if (m.isAnnotationPresent(GetMapping.class)) {
                path = m.getAnnotation(GetMapping.class).value();
                services.put(path, m);
            }
        }
    }

    public static String callMicroSpringService(HttpRequest req) {
        StringBuilder response = new StringBuilder();
        try{
            System.out.println(req.getPath());
            Method m = services.get(req.getPath());
            String result =  "{ \"name\": "  + "\"" + m.invoke(null, "pedro") + "\" " + "}";
            response.append("HTTP/1.1 200 OK\r\n");
            response.append("Content-Type: application/json\r\n");
            response.append("\r\n");
            response.append(result);
            return response.toString();
        } catch (Exception e) {
            response.append("HTTP/1.1 400 Bad Request\r\n");
            response.append("Content-Type: application/json\r\n");
            response.append("\r\n");
            response.append("{ \"error\": " + "\"" + "Invalid POST request" + "\"}");
            return response.toString();
        }
    }

}
