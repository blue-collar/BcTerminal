package com.telewave.lib.base.util;

import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

public final class Preconditions {
    private Preconditions() {
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(
            T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    public static String lenientFormat(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"

        if (args == null) {
            args = new Object[]{"(Object[])null"};
        } else {
            for (int i = 0; i < args.length; i++) {
                args[i] = lenientToString(args[i]);
            }
        }

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    private static String lenientToString(Object o) {
        try {
            return String.valueOf(o);
        } catch (Exception e) {
            // Default toString() behavior - see Object.toString()
            String objectToString =
                    o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
            // Logger is created inline with fixed name to avoid forcing Proguard to create another class.
            Logger.getLogger("com.google.common.base.Strings")
                    .log(WARNING, "Exception during lenientFormat for " + objectToString, e);
            return "<" + objectToString + " threw " + e.getClass().getName() + ">";
        }
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }


    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj, String errorMessageTemplate, Object p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj, String errorMessageTemplate, Object p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj, String errorMessageTemplate, Object p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj,
            String errorMessageTemplate,
            Object p1,
            Object p2) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj,
            String errorMessageTemplate,
            Object p1,
            Object p2,
            Object p3) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2, p3));
        }
        return obj;
    }

    public static <T> T checkNotNull(
            T obj,
            String errorMessageTemplate,
            Object p1,
            Object p2,
            Object p3,
            Object p4) {
        if (obj == null) {
            throw new NullPointerException(lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
        }
        return obj;
    }

    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    public static int checkElementIndex(int index, int size, String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return lenientFormat("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index >= size
            return lenientFormat("%s (%s) must be less than size (%s)", desc, index, size);
        }
    }

    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }

    public static int checkPositionIndex(int index, int size, String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return lenientFormat("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index > size
            return lenientFormat("%s (%s) must not be greater than size (%s)", desc, index, size);
        }
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        // end < start
        return lenientFormat("end index (%s) must not be less than start index (%s)", end, start);
    }
}
