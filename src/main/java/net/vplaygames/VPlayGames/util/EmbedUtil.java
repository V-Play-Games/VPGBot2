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

import com.vplaygames.PM4J.caches.MoveDataCache;
import com.vplaygames.PM4J.caches.SkillDataCache;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.*;
import com.vplaygames.PM4J.json.JSONArray;
import net.dv8tion.jda.api.EmbedBuilder;
import net.vplaygames.VPlayGames.core.Bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.function.Supplier;

import static net.vplaygames.VPlayGames.core.Bot.PREFIX;

public class EmbedUtil {
    public static String[] breakTillValid(String[] a, String delimiter) {
        ArrayList<String> tor = new ArrayList<>();
        int j = 0;
        outer:
        for (int i = 0; i < 6 && j < a.length; i++) {
            tor.add("");
            while (tor.get(i).length() < 1000 && j < a.length) {
                String s = tor.get(i) + a[j];
                if (s.length() > 1024) {
                    tor.add(i, tor.get(i).substring(0, tor.get(i).length() - delimiter.length()));
                    continue outer;
                }
                tor.set(i, s + delimiter);
                j++;
            }
            tor.set(i, tor.get(i).substring(0, tor.get(i).length() - delimiter.length()));
            break;
        }
        return tor.toArray(new String[0]);
    }

    public static void addFieldSafely(String title, Collection<Pokemon> value, EmbedBuilder eb) {
        addFieldSafely(title,
            value.stream()
            .map(u -> u.syncPair)
            .collect((Supplier<ArrayList<String>>) ArrayList::new,
                ArrayList::add,
                ArrayList::addAll)
            .toArray(new String[0]),
            eb);
    }

    public static void addFieldSafely(String title, String[] value, EmbedBuilder eb) {
        addFieldSafely(title, value, false, ",\n", ",", "None", eb);
    }

    public static void addFieldSafely(String title, String[] value, boolean inline, String delimiter1, String delimiter2, String def, EmbedBuilder eb) {
        if (value.length == 0){ eb.addField(title, def, inline); return;}
        String[] array = breakTillValid(value, delimiter1);
        for (int i = 0; i < array.length; i++) {
            eb.addField(i == 0 ? title : "", array[i] + (i != array.length - 1 ? delimiter2 : ""), inline);
        }
    }

    public static EmbedBuilder prepareEmbed(Trainer t) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(t.name + "'s Data");
        eb.addField("Details",
            "**Name:** " + t.name +
                "\n**Pokemons:** " + Array.toString(", ", t.pokemon, ""),
            false);
        eb.setImage(t.img);
        eb.setThumbnail(Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        return eb;
    }

    public static EmbedBuilder prepareEmbed(Pokemon pokemon) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(pokemon.name + "'s Data");
        eb.setDescription("Info about " + pokemon.trainer + "'s " + pokemon.name);
        eb.addField("Basic Info",
            "**Name:** " + pokemon.name +
                "\n**Typing:** " + Array.toString(", ", pokemon.typing, "N/A") +
                "\n**Weakness:** " + pokemon.weakness +
                "\n**Role:** " + pokemon.role +
                "\n**Rarity:** " + pokemon.rarity + " Stars" +
                "\n**Gender:** " + pokemon.gender +
                "\n**Other Forms:** " + Array.toString(", ", pokemon.otherForms, "N/A"),
            false);
        eb.addField("Advance Info",
            "\n**Stats:**\n```\n" + getStatsAsSimpleString(pokemon.stats) + "\n```", false);
        eb.addField("", "**Moves:**\n" + getMovesAsSimpleString(pokemon.moves), false);
        eb.addField("", "\n**Sync Move:** " + getSyncMoveAsSimpleString(pokemon.syncMove) +
                "\n**Passive Skills:**\n" + getPassivesAsSimpleString(pokemon.passives) +
                "\n***Sync Grid:** Feature Coming Soon*",
            false);
        eb.setThumbnail(TrainerDataCache.getInstance().get(pokemon.trainer).img);
        return eb;
    }

    public static EmbedBuilder prepareEmbed(MoveDataCache.Node node) {
        Move m = node.move;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(m.name + "'s Data");
        eb.setDescription("Info about the move: " + m.name);
        eb.addField("__Basic Info__",
            "**Name:** " + m.name +
                "\n**Type:** " + m.type +
                "\n**Category:** " + m.category +
                "\n**Targets:** " + (m.target.equals("Self") ? "The User" : Strings.toProperCase(m.target)) +
                "\n**Cost (MG):** " + m.cost + " Gauge" + (m.cost > 1 ? "s\n" : "\n") +
                (m.accuracy == 0 ? "**Never Misses.**" : "**Accuracy:** " + m.accuracy + "%") +
                (m.minPower == 0
                    ? "\n**It does not deal any direct damage.**"
                    : "\n**Power:** " + m.minPower + "->" + Math.floor(m.minPower * 1.2)),
            false);
        eb.addField("Effect",
            m.effect,
            false);
        eb.addField("Usable by:",
            node.users.stream()
                .map(p -> p.syncPair)
                .collect(() -> new StringJoiner(",\n").setEmptyValue("None"),
                    StringJoiner::add,
                    StringJoiner::merge)
                .toString(),
            false);
        return eb;
    }

    public static EmbedBuilder prepareEmbed(SkillDataCache.Node node) {
        Passive p = node.skill;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(p.name + "'s Data");
        eb.setDescription("Info about the skill: " + p.name);
        eb.addField("__Basic Info__",
            "**Name:** " + p.name +
                "\n**Description:** " + p.description,
            false);
        EmbedUtil.addFieldSafely("It is present as the default passive skills of:", node.inbuilt, eb);
        EmbedUtil.addFieldSafely("It is present on the grid of:", node.inGrid, eb);
        return eb;
    }

    public static EmbedBuilder prepareEmbed(ArrayList<Pokemon> node) {
        EmbedBuilder eb = new EmbedBuilder();
        Pokemon first = node.get(0);
        eb.setTitle(first.name + "'s Data");
        eb.setDescription("Info about the Pokemon: " + first.name);
        StringJoiner nameList = new StringJoiner(",\n");
        int i = 0;
        while (i < node.size()) {
            nameList.add((i + 1) + ". " + node.get(i).trainer + "'s " + node.get(i++).name);
        }
        eb.addField("List of all Pokemon with the name \"" + first.name + "\"",
            nameList.toString(),
            false);
        eb.addField("General Info",
            "**Typing:** " + Array.toString(", ", first.typing, "N/A") +
                "\n**Weakness:** " + first.weakness,
            false);
        return eb;
    }

    public static String getStatsAsSimpleString(StatRange stats) {
        return String.format("      |  HP | Atk | Def | SpA | SpD | Spe\nBASE: | %s | %s | %s | %s | %s | %s\nMAX:  | %s | %s | %s | %s | %s | %s\nMax Bulk: %d", getWithSpaces(stats.base.hp), getWithSpaces(stats.base.atk), getWithSpaces(stats.base.def), getWithSpaces(stats.base.spAtk), getWithSpaces(stats.base.spDef), getWithSpaces(stats.base.speed), getWithSpaces(stats.max.hp), getWithSpaces(stats.max.atk), getWithSpaces(stats.max.def), getWithSpaces(stats.max.spAtk), getWithSpaces(stats.max.spDef), getWithSpaces(stats.max.speed), stats.max.bulk);
    }

    public static String getWithSpaces(int a) {
        return MiscUtil.space(3 - Integer.toString(a).length()) + a;
    }

    public static String getMovesAsSimpleString(JSONArray<Move> moves) {
        StringJoiner tor = new StringJoiner("\n\n");
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            tor.add("Move " + (i+1) + ": " + move.name + (move.type.isEmpty() ? "" : " (" + move.type + " Type)") +
                "\nPower: " + (move.minPower == 0 ? "--" : move.minPower + "->" + Math.round(Math.floor(move.minPower * 1.2))) +
                " | Accuracy: " + (move.accuracy == 0 ? "--" : move.accuracy + "%") +
                " | Gauge: " + (move.cost == 0 ? "--" : move.cost) +
                "\nTarget: " + move.target +
                " | Max uses: " + (move.uses == 0 ? "--" : move.uses) +
                " | Category: " + move.category + "\n" + move.effect);
        }
        return tor.toString();
    }

    public static String getSyncMoveAsSimpleString(SyncMove move) {
        return String.format("__%s__ (%s Type %s Sync Move)\nPower: %s\nTarget: %s\n%s", move.name, move.type, move.category, move.minPower == 0 ? "--" : move.minPower + "->" + Math.round(Math.floor(move.minPower * 1.2)), move.target, move.description);
    }

    public static String getPassivesAsSimpleString(JSONArray<Passive> passives) {
        StringJoiner tor = new StringJoiner("\n");
        for (int i = 0; i < passives.size(); i++) {
            tor.add((i+1) + ". " + passives.get(i).name + ": " + passives.get(i).description);
        }
        return tor.toString();
    }

    public static String getFooter(int a, int b) {
        return String.format("Showing page [%d/%d] | Use " + PREFIX + "next to go forward or " + PREFIX + "back to go backward", a, b);
    }
}
