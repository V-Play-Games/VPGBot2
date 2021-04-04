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

import java.util.Arrays;
import java.util.StringJoiner;

public class Array {
    public static boolean contains(long a, long[] b) {
        for (long l : b) if (a == l) return true;
        return false;
    }

    public static boolean contains(String a, String[] b) {
        for (String s : b) if (a.equals(s)) return true;
        return false;
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

    public static String toString(String delimiter, Object[] a, String def) {
        return (a == null || a.length == 0) ? def : Arrays.stream(a).collect(() -> new StringJoiner(delimiter), (sj, o) -> sj.add(o.toString()), StringJoiner::merge).toString();
    }
}
