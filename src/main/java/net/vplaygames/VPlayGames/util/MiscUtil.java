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

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.Damage.Status;
import net.vplaygames.VPlayGames.core.Damage.Weather;
import net.vplaygames.VPlayGames.core.Passive;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
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
        } catch (Exception ignored) {
        }
        return tor;
    }

    public static int isSpecial(String special) {
        return special.equals("Special") ? 1 : 0;
    }

    public static int returnTypeId(String type) {
        switch (type.toLowerCase()) {
            case "bug":      return 1;
            case "dark":     return 2;
            case "dragon":   return 3;
            case "electric": return 4;
            case "fairy":    return 5;
            case "fighting": return 6;
            case "fire":     return 7;
            case "flying":   return 8;
            case "ghost":    return 9;
            case "grass":    return 10;
            case "ground":   return 11;
            case "ice":      return 12;
            case "normal":   return 13;
            case "poison":   return 14;
            case "psychic":  return 15;
            case "rock":     return 16;
            case "steel":    return 17;
            default:         return 18; // Water
        }
    }

    public static boolean isActive(Damage d, Passive.Skill skill) {
        if ((skill.modifies == 1 && d.attack.isSync) || (skill.modifies == 2 && !d.attack.isSync)) {
            return false;
        }
        int t = skill.checks, b;
        boolean check;
        boolean j = skill.condition.endsWith("-");
        switch (skill.condition.replaceAll("[\\-+]", "")) {
            case "always":
            case "ub":   check = true; break;
            case "wtr":  check =!d.weather.equals(Weather.NORMAL); break;
            case "sun":  check = d.weather.equals(Weather.SUN); break;
            case "rain": check = d.weather.equals(Weather.RAIN); break;
            case "sand": check = d.weather.equals(Weather.SANDSTORM); break;
            case "hail": check = d.weather.equals(Weather.HAIL); break;
            case "stt":  check =!d.getStatus(t == 1).equals(Status.NORMAL); break;
            case "par":  check = d.getStatus(t == 1).equals(Status.PARALYZE); break;
            case "slp":  check = d.getStatus(t == 1).equals(Status.SLEEP); break;
            case "psn":  check = d.getStatus(t == 1).equals(Status.POISON); break;
            case "brn":  check = d.getStatus(t == 1).equals(Status.BURN); break;
            case "frz":  check = d.getStatus(t == 1).equals(Status.FREEZE); break;
            case "int":  check = Arrays.stream(d.interference[t]).sum() != 0; break;
            case "fln":  check = d.interference[t][0] == 1; break;
            case "cf":   check = d.interference[t][1] == 1; break;
            case "trp":  check = d.interference[t][2] == 1; break;
            case "ch":   check = d.mod[0] == 1; break;
            case "se":   check = d.mod[1] == 1; break;
            case "mg":   check = d.gauge >= 1; break;
            case "atk": b = d.buffs[t][0]; check = j?b<0:b>0; break;
            case "spa": b = d.buffs[t][1]; check = j?b<0:b>0; break;
            case "def": b = d.buffs[t][2]; check = j?b<0:b>0; break;
            case "spd": b = d.buffs[t][3]; check = j?b<0:b>0; break;
            case "spe": b = d.buffs[t][4]; check = j?b<0:b>0; break;
            case "acc": b = d.buffs[t][5]; check = j?b<0:b>0; break;
            case "eva": b = d.buffs[t][6]; check = j?b<0:b>0; break;
            case "hp": check = d.hp[t] > 0; break;
            case "stat": check = Arrays.stream(d.buffs[t]).filter(i -> j ? i < 0 : i > 0).sum() != 0; break;
            default: check = false;
        }
        return check;
    }

    public static double getMultiplier(Damage d, Passive passive) {
        switch (passive.skill.condition) {
            case "mg": return d.gauge * passive.intensity / 100.0;
            case "hp": return d.hp[passive.skill.checks] * passive.intensity / 100.0;
            default: return (passive.skill.intensive ? passive.intensity : Math.abs(getStatDependency(d, passive.skill) / 2.0)) / 10.0;
        }
    }

    public static int getStatDependency(Damage d, Passive.Skill skill) {
        int[] t = d.buffs[skill.checks];
        int toCheck;
        switch (skill.condition.replaceAll("[\\-+]", "")) {
            case "atk": toCheck = t[0]; break;
            case "spa": toCheck = t[1]; break;
            case "def": toCheck = t[2]; break;
            case "spd": toCheck = t[3]; break;
            case "spe": toCheck = t[4]; break;
            case "acc": toCheck = t[5]; break;
            case "eva": toCheck = t[6]; break;
            default: return Arrays.stream(t).filter(i -> skill.condition.endsWith("-") ? i < 0 : i > 0).sum();
        }
        return skill.condition.endsWith("-") ? Math.min(toCheck, 0) : Math.max(toCheck, 0);
    }
}
