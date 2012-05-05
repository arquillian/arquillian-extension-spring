/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.spring.utils;

import java.lang.reflect.Field;

/**
 * <p>A helper class used for testing.</p>
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class TestReflectionHelper {

    /**
     * <p>Creates new instance of {@link TestReflectionHelper} class.</p>
     *
     * <p>The private constructor prevents from instantiation outside this class.</p>
     */
    private TestReflectionHelper() {
        // empty constructor
    }

    /**
     * <p>Sets the field value for the given object instance.</p>
     *
     * @param obj       the object instance
     * @param fieldName the field name
     * @param value     the value to set
     *
     * @throws IllegalAccessException if any error occurs when setting the field value
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException,
            NoSuchFieldException {

        Field field = getField(obj, fieldName);
        field.setAccessible(true);

        field.set(obj, value);
    }

    /**
     * <p>Retrieves the field from class by it's name.</p>
     *
     * @param obj       the object instance
     * @param fieldName the name of the field
     *
     * @return the class field
     */
    private static Field getField(Object obj, String fieldName) throws NoSuchFieldException {

        Class<?> tmpClass = obj.getClass();
        do {
            for (Field field : tmpClass.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {

                    return field;
                }
            }
            tmpClass = tmpClass.getSuperclass();
        } while (tmpClass != null);

        throw new NoSuchFieldException("The field with name " + fieldName + " hasn't been found.");
    }
}
