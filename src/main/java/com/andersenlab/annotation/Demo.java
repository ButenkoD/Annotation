package com.andersenlab.annotation;

import java.util.TreeMap;

public class Demo {
    public static void main(String[] args) {
        MethodTraceAnnotationAnalyzer analyzer = new MethodTraceAnnotationAnalyzer();
        try {
            analyzer.analyze(TestClass.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static class TestClass {
        private static TreeMap<Integer, String> storage = new TreeMap<>();
        @MethodTrace
        public static void add(int key, String data) {
            storage.put(key, data);
        }

        @MethodTrace(level = MethodTrace.Level.DEBUG)
        public static boolean deleteData(int key) {
            if (storage.containsKey(key)) {
                storage.remove(key);
                return true;
            }
            return false;
        }
    }
}
