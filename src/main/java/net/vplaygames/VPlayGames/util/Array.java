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

import static java.util.Arrays.fill;

public class Array
{
    public static long[] clearValues(long[] a, long value)
    {
        long[] b = new long[a.length];
        fill(b,0);
        for (int i=0,j=0; i<a.length; i++)
            if (a[i]!=value)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static long[] clearValuesAt(long[] a, int index)
    {
        long[] b = new long[a.length];
        fill(b,0);
        for (int i=0,j=0; i<a.length; i++)
            if (i!=index)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static int[] clearValuesAt(int[] a, int index)
    {
        int[] b = new int[a.length];
        fill(b,0);
        for (int i=0,j=0; i<a.length; i++)
            if (i!=index)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static String[] clearValuesAt(String[] a, int index)
    {
        String[] b = new String[a.length];
        fill(b,"");
        for (int i=0,j=0; i<a.length; i++)
            if (i!=index)
            {
                b[j] = a[i];
                j++;
            }
        return b;
    }
    public static int nearestFreeIndex(long[] a)
    {
        for (int i=0; i<a.length; i++)
            if (a[i]==0)
                return i;
        return a.length;
    }
    public static int nearestFreeIndex(long[] a, long freeval)
    {
        for (int i=0; i<a.length; i++)
            if (a[i]==freeval)
                return i;
        return a.length;
    }
    public static String[] toLowerCase(String[] a)
    {
        String[] b = new String[a.length];
        for (int i = 0; i<a.length; i++)
            b[i] = a[i].toLowerCase();
        return b;
    }
    public static String[] toUpperCase(String[] a)
    {
        String[] b = new String[a.length];
        for (int i = 0; i<a.length; i++)
            b[i] = a[i].toUpperCase();
        return b;
    }
    public static int returnID(String[] a, String b)
    {
        for (int i = 0; i<a.length; i++)
        {
            if (b.equalsIgnoreCase(a[i]))
                return i;
        }
        return a.length-1;
    }
    public static int returnID(int[] a, int b)
    {
        for (int i = 0; i<a.length; i++)
        {
            if (b==a[i])
                return i;
        }
        return a.length-1;
    }
    public static int returnID(long[] a, long b)
    {
        for (int i = 0; i<a.length; i++)
        {
            if (b==a[i])
                return i;
        }
        return a.length-1;
    }
    public static byte[] subarray(byte[] a, int x, int y)
    {
        byte[] b = new byte[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static int[] subarray(int[] a, int x, int y)
    {
        int[] b = new int[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static long[] subarray(long[] a, int x, int y)
    {
        long[] b = new long[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static char[] subarray(char[] a, int x, int y)
    {
        char[] b = new char[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static float[] subarray(float[] a, int x, int y)
    {
        float[] b = new float[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static double[] subarray(double[] a, int x, int y)
    {
        double[] b = new double[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static String[] subarray(String[] a, int x, int y)
    {
        String[] b = new String[y-x];
        int j=0;
        while (x<y)
        {
            b[j]=a[x];
            j++;
            x++;
        }
        return b;
    }
    public static int sumAll(int[] a)
    {
        int b=0;
        for (int value : a) b += value;
        return b;
    }
    public static int sumAllPositive(int[] a)
    {
        int b=0;
        for (int i=0; i<a.length&&a[i]>0; i++)
            b+=a[i];
        return b;
    }
    public static int sumAllNegative(int[] a)
    {
        int b=0;
        for (int i=0; i<a.length&&a[i]<0; i++)
            b+=a[i];
        return b;
    }
    public static int[] stringToIntArray(String[] a)
    {
        int[] b = new int[a.length];
        for (int i=0; i<a.length; i++)
            b[i]=Integer.parseInt(a[i]);
        return b;
    }
}