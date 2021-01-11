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

import java.util.Arrays;

import static java.util.Arrays.fill;

public class MDArray
{
    public static int[][][] clearValuesAt(int[][][] a, int index)
    {
        int[][][] b = setAll(new int[a.length][][],0);
        for (int i=0,j=0; i<a.length; i++)
            if (i!=index)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static int[][] clearValuesAt(int[][] a, int index)
    {
        int[][] b = setAll(new int[a.length][],0);
        for (int i=0,j=0; i<a.length; i++)
            if (i!=index)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static int maxLength(int[][] a)
    {
        int b=0,i=0;
        while(i<a.length){b=Math.max(b,a[i].length);i++;}
        return b;
    }
    public static int[][] setAll(int[][] a, int b)
    {
        for (int[] ints : a) fill(ints, b);
        return a;
    }
    public static int maxLength(String[][] a)
    {
        int b=0;
        for (String[] strings : a) b = Math.max(b, strings.length);
        return b;
    }
    public static String[][] setAll(String[][] a, String b)
    {
        String[][] c = new String[a.length][];
        for (String[] strings : a) Arrays.fill(strings, b);
        return c;
    }
    public static int maxLength(int[][][] a)
    {
        int b=0;
        for (int[][] i:a) b = Math.max(b, i.length);
        return b;
    }
    public static int[][][] setAll(int[][][] a, int b)
    {
        int[][][] c = new int[a.length][][];
        for (int i=0; i<a.length; i++)
            c[i]=setAll(a[i],b);
        return c;
    }
    public static int maxLength(String[][][] a)
    {
        int b=0;
        for (String[][] i:a) b = Math.max(b, i.length);
        return b;
    }
    public static String[][][] setAll(String[][][] a, String b)
    {
        String[][][] c = new String[a.length][][];
        for (int i=0; i<a.length; i++)
            c[i]=setAll(a[i],b);
        return c;
    }
}