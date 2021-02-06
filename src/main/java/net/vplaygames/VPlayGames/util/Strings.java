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

import static net.vplaygames.VPlayGames.data.Bot.BASE64;

public class Strings {
    public static String valueOf(Throwable t) {
        StringJoiner tor = new StringJoiner("\n").add(t.toString());
        StackTraceElement[] stack = t.getStackTrace();
        for (StackTraceElement ste : stack) {
            tor.add("\tat " + ste);
        }
        Throwable cause = t.getCause();
        if (cause!=null) {
            tor.add("Caused by: "+valueOf(cause));
        }
        return tor.toString();
    }

    public static String toProperCase(String a) {
        String[] b = a.split(" ");
        for (int i = 0; i < b.length; i++)
            b[i] = b[i].toUpperCase().charAt(0) + b[i].substring(1).toLowerCase();
        return String.join(" ", b);
    }

    public static int toInt(String a) {
        int rtrn = 0;
        for (int i = 0; i < a.length(); i++)
            rtrn = rtrn * 10 + ((MiscUtil.charToInt(a.charAt(i)) == 10) ? -rtrn * 9 : MiscUtil.charToInt(a.charAt(i)));
        return rtrn;
    }

    public static boolean equalsAny(String b, String... a) {
        boolean bool = false;
        for (String s : a) bool = bool || (b.equals(s));
        return bool;
    }

    public static boolean equalsAnyIgnoreCase(String b, String... a) {
        boolean bool = false;
        for (String s : a) bool = bool || (b.equalsIgnoreCase(s));
        return bool;
    }

    public static int[] toSingleIntArray(String s) {
        return Array.stringToInt(s.split(","));
    }

    public static int[][] toDoubleIntArray(String s) {
        String[] elements = s.substring(1, s.length() - 1).split("-");
        int[][] tor = new int[elements.length][];
        for (int i = 0; i < tor.length; i++)
            tor[i] = toSingleIntArray(elements[i]);
        return tor;
    }

    public static String reduceToAlphanumeric(String s) {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i<s.length(); i++) {
            if (BASE64.substring(0,62).contains(Character.toString(s.charAt(i)))) {
                tor.append(s.charAt(i));
            }
        }
        return tor.toString();
    }

    public static String reduceToAlphabets(String s) {
        StringBuilder tor = new StringBuilder();
        s = reduceToAlphanumeric(s);
        for (int i = 0; i<s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                tor.append(s.charAt(i));
            }
        }
        return tor.toString();
    }
}