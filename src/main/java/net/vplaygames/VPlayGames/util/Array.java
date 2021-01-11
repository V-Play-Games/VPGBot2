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

public class Array
{
    public static int returnID(String[] a, String b) {
        for (int i = 0; i<a.length; i++)
            if (b.equalsIgnoreCase(a[i]))
                return i;
        return a.length-1;
    }

    public static int returnID(long[] a, long b) {
        for (int i = 0; i<a.length; i++)
            if (b==a[i])
                return i;
        return a.length-1;
    }

    public static int returnID(int[] a, int b) {
        for (int i = 0; i<a.length; i++)
            if (b==a[i])
                return i;
        return a.length-1;
    }
}