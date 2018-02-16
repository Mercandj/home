package com.mercandalli.core.main;

import java.io.Closeable;

public class Closer {

    public static void closeSilently(Closeable... xs) {
        for (Closeable x : xs) {
            if (x != null) {
                try {
                    x.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
