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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import com.vplaygames.PM4J.caches.MoveDataCache;
import com.vplaygames.PM4J.caches.PokemonDataCache;
import com.vplaygames.PM4J.caches.SkillDataCache;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.caches.framework.Cache.Type;
import com.vplaygames.PM4J.entities.*;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.core.UserEmbedInfo;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.EmbedUtil;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.*;

import static com.vplaygames.PM4J.caches.framework.Cache.Type.*;
import static net.vplaygames.VPlayGames.data.Bot.PREFIX;

public class SearchCommand extends Command {
    static HashMap<Long, UserEmbedInfo> embedInfo = new HashMap<>();

    static TrainerDataCache tdc;
    static MoveDataCache mdc;
    static SkillDataCache sdc;
    static PokemonDataCache pdc;

    public SearchCommand() {
        super("search");
        tdc = TrainerDataCache.getInstance();
        mdc = MoveDataCache.getInstance();
        sdc = SkillDataCache.getInstance();
        pdc = PokemonDataCache.getInstance();
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String data = e.getMessage().getContentRaw().substring(e.getMessage().getContentRaw().split(" ")[0].length() + 1);
        Optional<Type> first = Arrays.stream(values()).filter(t -> t != UNKNOWN && t.getCache().containsKey(data)).findFirst();
        if (!first.isPresent()) {
            MiscUtil.send(e, "\"" + data + "\" not found.", true);
            return;
        }
        Type type = first.get();
        EmbedBuilder eb;
        String toSend;
        if (isTurnable(type)) {
            int limit;
            if (type == TRAINER) {
                Trainer t = tdc.get(data);
                eb = prepareEmbed(t);
                limit = t.getPokemon().length;
            } else {
                ArrayList<Pokemon> pd = pdc.get(data);
                eb = prepareEmbed(pd);
                limit = pd.size();
            }
            eb.setFooter(getFooter(1, limit + 1)+" | Requested by "+e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(e.getMessage().getTimeCreated());
            e.getChannel().sendMessage(eb.build()).queue(response ->
                embedInfo.put(e.getAuthor().getIdLong(),
                    new UserEmbedInfo(e.getAuthor().getIdLong(), response.getIdLong(), data, limit, type)));
            toSend = e.getAuthor().getAsMention() + ", You have opened a new " + type.toString().toLowerCase() + " search query!\n" +
                "Any previous queries of yours will not be used to turn pages with `" + PREFIX + "next` & `" + PREFIX + "back`";
            e.getChannel().sendMessage(toSend).queue();
        } else {
            e.getChannel().sendMessage(
                (type == MOVE ? prepareEmbed(mdc.get(data)) : prepareEmbed(sdc.get(data)))
                    .setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                    .setFooter("Requested by: " + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(e.getMessage().getTimeCreated())
                    .build()).queue();
            toSend = type.toString() + " Data for " + data;
        }
        Response.get(Bot.messagesProcessed).responded(toSend);
    }


    public static EmbedBuilder prepareEmbed(Trainer td) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(td.name + "'s Data");
        eb.addField("Details",
            "**Name:** " + td.name +
                "\n**Rarity:** " + td.rarity + " Stars" +
                "\n**Pokemons:** " + Array.toString(", ", td.getPokemon(), ""),
            false);
        eb.setImage(td.img);
        eb.setThumbnail(Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        return eb;
    }

    public static EmbedBuilder prepareEmbed(Pokemon pokemon) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(pokemon.name + "'s Data");
        eb.setDescription("Info about " + pokemon.trainer + "'s " + pokemon.name);
        eb.addField("Basic Info",
            "**Name:** " + pokemon.name +
                "\n**Typing:** " + Array.toString(", ", pokemon.getTyping(), "N/A") +
                "\n**Weakness:** " + pokemon.weakness +
                "\n**Role:** " + pokemon.role +
                "\n**Rarity:** " + pokemon.rarity + " Stars" +
                "\n**Gender:** " + pokemon.gender +
                "\n**Other Forms:** " + Array.toString(", ", pokemon.getOtherForms(), "N/A"),
            false);
        eb.addField("Advance Info",
            "\n**Stats:**\n```\n" + getStatsAsSimpleString(pokemon.stats) + "\n```", false);
        eb.addField("", "**Moves:**\n" + getMovesAsSimpleString(pokemon.moves), false);
        eb.addField("", "\n**Sync Move:** " + getSyncMoveAsSimpleString(pokemon.syncMove) +
                "\n**Passive Skills:**\n" + getPassivesAsSimpleString(pokemon.passives) +
                "\n***Sync Grid:** Feature Coming Soon*",
            false);
        eb.setThumbnail(tdc.get(pokemon.trainer).img);
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
            Array.toString(",\n", node.getUsers(), "None"),
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
        EmbedUtil.addFieldSafely("It is present as the default passive skills of:", node.getInbuilt(), false, ",\n", ",", "None", eb);
        EmbedUtil.addFieldSafely("It is present on the grid of:", node.getInGrid(), false, ",\n", ",", "None", eb);
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
            "**Typing:** " + Array.toString(", ", first.getTyping(), "N/A") +
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
        StringJoiner tor = new StringJoiner("\n");
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            tor.add("Move " + i + ": " + move.name + (move.type.isEmpty() ? "" : " (" + move.type + " Type ") +
                ")\nPower: " + (move.minPower == 0 ? "--" : move.minPower + "->" + Math.round(Math.floor(move.minPower * 1.2))) +
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
            tor.add(i + ". " + passives.get(i).name + ": " + passives.get(i).description);
        }
        return tor.toString();
    }

    public static String getFooter(int a, int b) {
        return String.format("Showing page [%d/%d] | Use " + PREFIX + "next to go forward or " + PREFIX + "back to go backward", a, b);
    }

    public static boolean isTurnable(Type type) {
        return type == POKEMON || type == TRAINER;
    }

    public static class NavigationCommands extends Command {
        public NavigationCommands() {
            super("next", "n", "back", "back");
        }

        @Override
        public void onCommandRun(GuildMessageReceivedEvent e) {
            boolean next = e.getMessage().getContentRaw().startsWith(PREFIX + "n");
            UserEmbedInfo uei = embedInfo.get(e.getAuthor().getIdLong());
            if (uei == null) return;
            if (uei.proceed(next)) {
                e.getChannel().retrieveMessageById(uei.embedId).queue(m -> {
                    int prog = uei.progress;
                    boolean start = prog == 0;
                    EmbedBuilder eb;
                    if (uei.type == POKEMON) {
                        ArrayList<Pokemon> pokes = pdc.get(uei.dataTitle);
                        if (start) {
                            eb = prepareEmbed(pokes);
                        } else {
                            eb = prepareEmbed(pokes.get(prog - 1));
                        }
                    } else {
                        Trainer t = tdc.get(uei.dataTitle);
                        if (start) {
                            eb = prepareEmbed(t);
                        } else {
                            eb = prepareEmbed(t.pokemonData.get(prog - 1));
                        }
                    }
                    eb.setFooter(getFooter((start ? 0 : prog) + 1, uei.limit + 1)+" | Requested by "+e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
                    m.editMessage(eb.build()).queue();
                    MiscUtil.send(e, "Changed the page from " + (prog + (next ? 0 : 1)) + " to " + (prog + (next ? 1 : 0)) + "\nJump to the message from the link: " + m.getJumpUrl(), true);
                }, exc -> {
                    uei.proceed(!next);
                    MiscUtil.send(e, "An error occurred while turning the page! Maybe the message was deleted.", true);
                    EventHandler.getInstance().process(exc);
                });
            } else {
                MiscUtil.send(e, "You're at the " + (next ? "end" : "start") + " of the query result. Can't turn the page any further", true);
            }
        }
    }
}