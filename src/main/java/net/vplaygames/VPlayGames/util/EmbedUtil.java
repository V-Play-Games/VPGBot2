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

import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;

public class EmbedUtil {
    public static String[] breakTillValid(String[] a, String delimiter) {
        ArrayList<String> tor = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 6 && j != a.length; i++) {
            tor.set(i, "");
            while (tor.get(i).length() < 1000 && j != a.length) {
                String s = tor.get(i) + a[j] + delimiter;
                if (s.length() > 1024) break;
                tor.add(i, s);
                j++;
            }
        }
        return tor.toArray(new String[0]);
    }

    public static EmbedBuilder addFieldSafely(String title, String[] value, boolean inline, String delimiter1, String delimiter2, String def, EmbedBuilder eb) {
        String[] array = breakTillValid(value, delimiter1);
        for (int i = 0; i<array.length; i++) {
            eb.addField(i==0?title:"", array[i]+delimiter2, inline);
        }
        return eb;
    }
}
