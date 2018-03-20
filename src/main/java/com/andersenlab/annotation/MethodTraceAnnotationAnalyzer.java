package com.andersenlab.annotation;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MethodTraceAnnotationAnalyzer {
    private static final Logger logger = LogManager.getLogger(MethodTraceAnnotationAnalyzer.class);

    public <T> void analyze(Class<T> clazz) {
        Method[] methods = clazz.getMethods();

        for (Method analyzedMethod: methods) {
            if (analyzedMethod.isAnnotationPresent(MethodTrace.class)) {
                MethodTrace annotation = analyzedMethod.getAnnotation(MethodTrace.class);
                MethodTrace.Level level = annotation.level();
                try {
                    Object[] params = prepareMethodParams(analyzedMethod);
                    long startTime = System.currentTimeMillis();
                    Object result = analyzedMethod.invoke(null, params);
                    long finishTime = System.currentTimeMillis();
                    String message = prepareMethodString(analyzedMethod, finishTime - startTime)
                            + prepareParamsString(params)
                            + prepareResultString(result);
                    printMessage(message, level);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private Object[] prepareMethodParams(Method method) throws Exception {
        int paramCount = method.getParameterCount();
        Object[] params = new Object[paramCount];
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramCount; i++) {
            params[i] = ParameterGenerator.prepareParamByType(paramTypes[i]);
        }

        return params;
    }

    private String prepareMethodString(Method method, long spentTime) {
        return "\n\tMethod: " + method.getName() + "\n"
            + "\tSpent time: " + spentTime + "\n";
    }

    private String prepareParamsString(Object[] params) {
        String result = "";
        for (Object param: params) {
            result += "\tParameter: " + param.getClass().getSimpleName() + " " + param.toString() + "\n";
        }
        return result;
    }

    private String prepareResultString(Object result) {
        return result == null
            ? "\tResult: null\n"
            : "\tResult: " + result.getClass().getSimpleName() + " " + result.toString() + "\n";
    }

    private boolean printMessage(String message, MethodTrace.Level level) {
        Method loggerMethod = getLoggerMethod(level);
        if (loggerMethod != null) {
            try {
                loggerMethod.invoke(logger, message);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    private Method getLoggerMethod(MethodTrace.Level level) {
        try {
            return logger.getClass().getMethod(level.name().toLowerCase(), Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static class ParameterGenerator {
        private static <T> Object prepareParamByType(Class<T> clazz) throws Exception {
            if (clazz.isPrimitive()) {
                return ParameterGenerator.getWrappedPrimitive(clazz);
            } else {
                return clazz.newInstance();
            }
        }

        private static Object getWrappedPrimitive(Class<?> clazz) {
            switch (clazz.getSimpleName()) {
                case "byte":
                    return new Byte("");
                case "short":
                case "int":
                case "long":
                    return 0;
                case "float":
                case "double":
                    return 0.0;
                case "boolean":
                    return false;
                case "char":
                    return 0;
                default:
                    return false;
            }
        }
    }
}
