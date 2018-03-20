package com.andersenlab.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodTrace {
    enum Level {DEBUG, ERROR, INFO, TRACE, WARN}
    Level level() default Level.INFO;
    class MethodData {
        String methodName;
        long spentTime;
        Object[] params;
        Object result;

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public long getSpentTime() {
            return spentTime;
        }

        public void setSpentTime(long spentTime) {
            this.spentTime = spentTime;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

//        @Override
//        public String toString() {
//            return "Method: " + methodName + "\n"
//                    + "Params: " +
//        }
    }
}
