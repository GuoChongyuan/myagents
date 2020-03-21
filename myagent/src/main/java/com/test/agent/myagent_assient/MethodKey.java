package com.test.agent.myagent_assient;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

@Data
@Builder
public class MethodKey {
    private String className;
    private String methodName;
    private String[] paramTypes;

    public MethodKey(String className, String methodName, String[] paramTypes) {
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodKey methodKey = (MethodKey) o;
        return Objects.equals(className, methodKey.className) &&
                Objects.equals(methodName, methodKey.methodName) &&
                Arrays.equals(paramTypes, methodKey.paramTypes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(className, methodName);
        result = 31 * result + Arrays.hashCode(paramTypes);
        return result;
    }
}
