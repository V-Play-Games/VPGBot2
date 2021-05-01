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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;
import java.util.TimeZone;

public class MiscUtil {
    public static String msToString(long ms) {
        if (ms < 0) throw new IllegalArgumentException("ms cannot be in negative!");
        if (ms < 1000) return ms + "ms";
        String tor = ms % 1000 + "ms";
        ms /= 1000;
        if (ms < 60) return ms + "s " + tor;
        tor = ms % 60 + "s " + tor;
        ms /= 60;
        if (ms < 60) return ms + "m " + tor;
        tor = ms % 60 + "m " + tor;
        ms /= 60;
        if (ms < 24) return ms + "h " + tor;
        tor = ms % 24 + "h " + tor;
        ms /= 24;
        return ms < 7 ? ms + "d " + tor : ms / 7 + "w " + ms % 7 + "d " + tor;
    }

    public static String space(int count) {
        StringJoiner tor = new StringJoiner(" ");
        while (count-- > 0) tor.add("");
        return tor.toString();
    }

    public static String dateTimeNow() {
        String lt = LocalTime.now().toString();
        lt = lt.substring(0, lt.length() - 4);
        if (Strings.toInt(lt.substring(0, 2)) > 12)
            lt = Strings.toInt(lt.substring(0, 2)) - 12 + lt.substring(2) + " PM";
        else
            lt += " AM";
        return "on " + LocalDate.now().toString() + " at " + lt + " (" + TimeZone.getDefault().getDisplayName(false, 0) + ")";
    }

    public static File makeFileOf(Object toBeWritten, String fileName) {
        File tor = new File(fileName);
        try (PrintStream stream = new PrintStream(tor)) {
            stream.println(toBeWritten);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tor;
    }

    public static int isSpecial(String special) {
        return special.equalsIgnoreCase("special")?1:0;
    }

    public static int returnTypeId(String type) {
        switch (Strings.reduceToAlphabets(type).toLowerCase()) {
            case "Bug":      return 1;
            case "Dark":     return 2;
            case "Dragon":   return 3;
            case "Electric": return 4;
            case "Fairy":    return 5;
            case "Fighting": return 6;
            case "Fire":     return 7;
            case "Flying":   return 8;
            case "Ghost":    return 9;
            case "Grass":    return 10;
            case "Ground":   return 11;
            case "Ice":      return 12;
            case "Normal":   return 13;
            case "Poison":   return 14;
            case "Psychic":  return 15;
            case "Rock":     return 16;
            case "Steel":    return 17;
            default:         return 18; // Water
        }
    }
}
