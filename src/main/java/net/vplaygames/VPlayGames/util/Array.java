/*
 * Copyright 2020 Vaibhav Nargwani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.vplaygames.VPlayGames.util;

import java.util.StringJoiner;

public class Array {
    public static boolean contains(String a, String[] b) {
        return Strings.equalsAnyIgnoreCase(a, b);
    }

    public static boolean contains(long a, long[] b) {
        boolean bool = false;
        for (long l : b) bool = bool || a == l;
        return bool;
    }

    public static int returnID(String[] a, String b) {
        for (int i = 0; i < a.length; i++)
            if (b.equalsIgnoreCase(a[i]))
                return i;
        return a.length - 1;
    }

    public static int returnID(char[] a, char b) {
        for (int i = 0; i < a.length; i++)
            if (b == a[i])
                return i;
        return a.length - 1;
    }

    public static int sumAll(int[] a) {
        int b = 0;
        for (int i : a) {
            b += i;
        }
        return b;
    }

    public static int sumAll(Integer[] a) {
        int b = 0;
        for (int i : a) {
            b += i;
        }
        return b;
    }

    public static String toString(String delimiter, Object[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringBuilder b = new StringBuilder().append(a[0]);
        for (int i = 1; i < a.length; i++)
            b.append(delimiter).append(a[i]);
        return b.toString();
    }

    public static String toString(String delimiter, int[] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringBuilder b = new StringBuilder().append(a[0]);
        for (int i = 1; i < a.length; i++)
            b.append(delimiter).append(a[i]);
        return b.toString();
    }

    public static String toString(String delimiter1, String delimiter2, String startWith, String endWith, int[][] a, String def) {
        if (a == null) return def;
        if (a.length == 0) return def;
        StringBuilder b = new StringBuilder().append(startWith).append(toString(delimiter2, a[0], def)).append(endWith);
        for (int i = 1; i < a.length; i++)
            b.append(delimiter1).append(startWith).append(toString(delimiter2, a[i], def)).append(endWith);
        return b.toString();
    }

    public static String manyToString(int[]... a) {
        StringJoiner s = new StringJoiner(";");
        for (int[] b : a) s.add(toString(b));
        return s.toString();
    }

    public static String manyToString(int[][]... a) {
        StringJoiner s = new StringJoiner(";");
        for (int[][] b : a) s.add(toString(b));
        return s.toString();
    }

    public static String toString(int[] a) {
        return "[" + Array.toString(",", a, "") + "]";
    }

    public static String toString(int[][] a) {
        return "[" + Array.toString("-", ",", "[", "]", a, "") + "]";
    }

    public static int[] stringToInt(String[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++)
            b[i] = Strings.toInt(a[i]);
        return b;
    }
}