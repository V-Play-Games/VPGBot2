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

import com.vplaygames.PM4J.caches.*;
import com.vplaygames.PM4J.caches.framework.Cache.Type;
import com.vplaygames.PM4J.entities.*;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import static com.vplaygames.PM4J.caches.framework.Cache.Type.*;
import static net.vplaygames.VPlayGames.data.Bot.PREFIX;

public class SearchCommand extends Command {
    private static HashMap<Long, UserEmbedInfo> dataMap = new HashMap<>();

    static TrainerDataCache tdc;
    static MoveDataCache mdc;
    static SkillDataCache sdc;
    static PokemonDataCache pdc;

    public SearchCommand() {
        super("search");
        if (PokemasDBCache.getInstance()==null) {
            throw new IllegalStateException("Data from pokemasdb.com has not been processed yet.");
        }
        tdc = TrainerDataCache.getInstance();
        mdc = MoveDataCache.getInstance();
        sdc = SkillDataCache.getInstance();
        pdc = PokemonDataCache.getInstance();
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length < 2) {
            MiscUtil.send(e, Bot.INVALID_INPUTS, true);
            return;
        }
        String data;
        Type tempType = Type.parseType(msg[1]);
        if (tempType != UNKNOWN) {
            data = e.getMessage().getContentRaw().substring(msg[0].length() + msg[1].length() + 2);
        } else {
            data = e.getMessage().getContentRaw().substring(msg[0].length() + 1);
            for (Type t : Type.values()) {
                if (t != UNKNOWN && t.getCache().containsKey(data)) {
                    tempType = t;
                }
            }
        }
        Type dataType = tempType;
        if (!dataType.getCache().containsKey(data)) {
            MiscUtil.send(e, dataType.toString() + " \"" + data + "\" not found.", true);
            return;
        }
        EmbedBuilder eb;
        String toSend;
        if (isTurnable(dataType)) {
            int limit;
            if (dataType == TRAINER) {
                Trainer t = tdc.get(data);
                eb = prepareEmbed(t, e.getAuthor().getName());
                limit = t.getPokemon().length;
            } else {
                ArrayList<Pokemon> pd = pdc.get(data);
                eb = prepareEmbed(pd);
                limit = pd.size();
            }
            eb.setFooter(getFooter(1, limit + 1));
            e.getChannel().sendMessage(eb.build()).queue(response ->
                    dataMap.put(e.getAuthor().getIdLong(),
                            new UserEmbedInfo(e.getAuthor().getIdLong(),
                                    response.getIdLong(),
                                    data,
                                    limit,
                                    dataType)));
            toSend = e.getAuthor().getAsMention() + ", You have opened a new " + dataType.toString().toLowerCase() + " search query!\n" +
                    "Any previous queries of yours will not be used to turn pages with `" + PREFIX + "next` & `" + PREFIX + "back`";
            e.getChannel().sendMessage(toSend).queue();
        } else {
            if (dataType == MOVE) {
                eb = prepareEmbed(mdc.get(data));
            } else {
                eb = prepareEmbed(sdc.get(data));
            }
            eb.setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                    .setFooter("Requested by: " + e.getAuthor().getAsTag() +" "+ MiscUtil.dateTimeNow(), e.getAuthor().getEffectiveAvatarUrl());
            e.getChannel().sendMessage(eb.build()).queue();
            toSend = dataType.toString() + " Data for " + data;
        }
        Response.get(Bot.messagesProcessed).responded(toSend);
    }


    public static EmbedBuilder prepareEmbed(Trainer td, String name) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(td.name + "'s Data");
        eb.addField("Details",
                "**Name:** " + td.name +
                        "\n**Rarity:** " + td.rarity + " Stars" +
                        "\n**Pokemons:** " + Array.toString(", ", (Object[]) td.getPokemon(), ""),
                false);
        eb.setImage(td.img);
        eb.setThumbnail(Bot.jda.getSelfUser().getEffectiveAvatarUrl());
        if (!"".equals(name) && name != null)
            eb.addField("Requested by", name, false);
        return eb;
    }

    public static EmbedBuilder prepareEmbed(Pokemon pokemon) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(pokemon.name + "'s Data");
        eb.setDescription("Info about " + pokemon.trainer + "'s " + pokemon.name);
        eb.addField("Basic Info",
                "**Name:** " + pokemon.name +
                        "\n**Typing:** " + Array.toString(", ", (Object[]) pokemon.getTyping(), "N/A") +
                        "\n**Weakness:** " + pokemon.weakness +
                        "\n**Role:** " + pokemon.role +
                        "\n**Rarity:** " + pokemon.rarity + " Stars" +
                        "\n**Gender:** " + pokemon.gender +
                        "\n**Other Forms:** " + Array.toString(", ", (Object[]) pokemon.getOtherForms(), "N/A"),
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
        eb.setTitle(node.move.name + "'s Data");
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
                Array.toString(",\n",node.getUsers(),"None"),
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
        EmbedUtil.addFieldSafely("It is present as the default passive skills of:",node.getInbuilt(),false,",\n",",","None",eb);
        EmbedUtil.addFieldSafely("It is present on the grid of:",node.getInGrid(),false,",\n",",","None",eb);
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
                "**Typing:** " + Array.toString(", ", (Object[]) first.getTyping(), "N/A") +
                        "\n**Weakness:** " + first.weakness,
                false);
        return eb;
    }


    private static String getStatsAsSimpleString(StatRange stats) {
        return "      |  HP | Atk | Def | SpA | SpD | Spe" +
               "\nBASE: | " + getWithSpaces(stats.base.hp) + " | " + getWithSpaces(stats.base.atk) + " | " + getWithSpaces(stats.base.def) + " | " + getWithSpaces(stats.base.spAtk) + " | " + getWithSpaces(stats.base.spDef) + " | " + getWithSpaces(stats.base.speed) +
               "\nMAX:  | " + getWithSpaces(stats.max .hp) + " | " + getWithSpaces(stats.max .atk) + " | " + getWithSpaces(stats.max .def) + " | " + getWithSpaces(stats.max .spAtk) + " | " + getWithSpaces(stats.max .spDef) + " | " + getWithSpaces(stats.max .speed) +
               "\nMax Bulk: " + stats.max.bulk;
    }

    private static String getWithSpaces(int a) {
        return MiscUtil.space(3 - Integer.toString(a).length()) + a;
    }

    private static String getMovesAsSimpleString(JSONArray<Move> moves) {
        StringJoiner tor = new StringJoiner("\n");
        int i = 1;
        for (Move move : moves) {
            tor.add("__Move " + (i++) + ": " + move.name +
                    "__\n-> It is a " + move.type + " type " + move.category + " move, which targets " + (move.target.equals("Self") ? "the user itself" : move.target.toLowerCase()) +
                    ".\n-> It uses " + (move.cost == 0 ? "no" : move.cost) + " bars & " + (move.accuracy == 0 ? "it never misses." : "has " + move.accuracy + "% accuracy.") +
                    "\n-> It" + (move.minPower == 0 ? " does not deal any direct damage." : "s power varies between " + move.minPower + "-" + Math.floor(move.minPower * 1.2)) +
                    "\n-> It " + (move.effect.startsWith("No ")?"has no additional effects":move.effect.toLowerCase().charAt(0)+move.effect.substring(1)+"."));
        }
        return tor.toString().substring(0, tor.length() - 1);
    }

    private static String getSyncMoveAsSimpleString(SyncMove sm) {
        return "__" + sm.name +
                "__\n-> It is a " + sm.type + " type " + sm.category + " move, which targets " + (sm.target.equals("Self") ? "the user itself" : sm.target.toLowerCase()) +
                "\n-> It" + (sm.minPower == 0 ? " does not deal any direct damage." : "s power varies between " + sm.minPower + "-" + Math.round(sm.minPower * 1.2)) +
                "\n-> Description: " + sm.description + "\n";
    }

    private static String getPassivesAsSimpleString(JSONArray<Passive> passives) {
        StringJoiner tor = new StringJoiner("\n");
        int i = 1;
        for (Passive passive : passives) {
            tor.add( i + ". __" + passive.name + "__: " + passive.description);
        }
        return tor.toString().substring(0, tor.length() - 1);
    }

    private static String getFooter(int a, int b) {
        return String.format("Showing page [%d/%d] | Use " + PREFIX + "next to go forward or " + PREFIX + "back to go backward.", a, b);
    }

    private static boolean isTurnable(Type type) {
        return type == POKEMON || type == TRAINER;
    }

    public static class NavigationCommands extends Command {
        public NavigationCommands() {
            super("next", "n", "back", "back");
        }

        @Override
        public void onCommandRun(GuildMessageReceivedEvent e) {
            boolean next = e.getMessage().getContentRaw().startsWith(PREFIX + "n");
            long aid = e.getAuthor().getIdLong();
            if (!dataMap.containsKey(aid)) {
                return;
            }
            UserEmbedInfo uei = dataMap.get(aid);
            String tos;
            if (uei.proceed(next)) {
                try {
                    Message m = e.getChannel().retrieveMessageById(uei.embedId).complete();
                    if (m == null)
                        throw new Exception();
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
                            eb = prepareEmbed(t, "");
                        } else {
                            eb = prepareEmbed(t.pokemonData.get(prog - 1));
                        }
                    }
                    m.editMessage(eb.setFooter(getFooter((start ? 0 : prog) + 1, uei.limit + 1)).build()).queue();
                    tos = "Changed the page from " + (prog + (next ? 0 : 1)) + " to " + (prog + (next ? 1 : 0)) + "\nJump to the message from the link: " + m.getJumpUrl();
                } catch (Exception exc) {
                    uei.proceed(!next);
                    tos = "An error occurred while turning the page! Maybe the message was deleted.";
                    EventHandler.getInstance().process(exc);
                }
            } else {
                tos = "You're at the " + (next ? "end" : "start") + " of the query result. Can't turn the page any further";
            }
            e.getChannel().sendMessage(tos).queue();
        }
    }
}