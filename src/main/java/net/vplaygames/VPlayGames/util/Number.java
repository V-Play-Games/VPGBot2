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

public class Number
{
    public static String toStringWithCommas(java.lang.Number num) {
        String origin = new StringBuilder(num.toString()).reverse().toString();
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < origin.length(); ) {
            tor.append(origin.charAt(i));
            if (++i % 3 == 0 && i != origin.length())
                tor.append(',');
        }
        return tor.reverse().toString();
    }

    public static String toStringOfLength(int len, java.lang.Number num) {
        String tor = num.toString();
        tor=tor.substring(0,Math.min(tor.length(),len));
        while (tor.length()<len) {
            tor="0"+tor;
        }
        return tor;
    }

    public static long roundInRange(long val, long start, long end) {
        return Math.min(Math.max(val,start),end);
    }

    public static boolean isBetween(int n, int lowerValue,int upperValue) {
        return n>=lowerValue&&n<=upperValue;
    }

    public static boolean isBetween(String n, int lowerValue,int upperValue) {
        return isBetween(Strings.toInt(n),lowerValue,upperValue);
    }
}
