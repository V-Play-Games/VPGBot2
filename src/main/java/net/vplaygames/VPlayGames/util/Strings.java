/*
 * Copyright 2020-2021 Vaibhav Nargwani
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

public class Strings {
    public static String toProperCase(String a) {
        String[] b = a.split(" ");
        for (int i = 0; i < b.length; i++)
            b[i] = b[i].toUpperCase().charAt(0) + b[i].substring(1).toLowerCase();
        return String.join(" ", b);
    }

    public static int toInt(String a) {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < a.length(); i++)
            tor.append(Character.isDigit(a.charAt(i)) ? a.charAt(i) : "");
        return Integer.parseInt(tor.toString());
    }

    public static boolean equalsAnyIgnoreCase(String b, String... a) {
        for (String s : a) if (b.equalsIgnoreCase(s)) return true;
        return false;
    }

    public static String reduceToAlphanumeric(String s) {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            tor.append(Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)) ? s.charAt(i) : "");
        return tor.toString();
    }

    public static String reduceToAlphabets(String s) {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            tor.append(Character.isAlphabetic(s.charAt(i)) ? s.charAt(i) : "");
        return tor.toString();
    }
}
