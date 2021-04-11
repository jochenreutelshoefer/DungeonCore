package de.jdungeon.util;

public class Clazz {

    /**
     * Emulates Class#isAssignableFrom() to have a workaround for GWT.
     * Warning: Does not Consider interfaces !!
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean isAssignableFrom(Class<?> c1, Class<?> c2) {
        while (c2 != null) {
            if (c2.equals(c1)) {
                return true;
            }
            c2 = c2.getSuperclass();
        }
        return false;
    }
}
