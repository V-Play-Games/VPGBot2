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

import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.core.UserEmbedInfo;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.PokeMasDB.Caches.*;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.*;
import net.vplaygames.VPlayGames.PokeMasDB.JSONFramework.JSONArray;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.PokeMasDB.Caches.Cache.Type.*;

public class SearchCommand extends HashMap<Long,UserEmbedInfo> {
    private static SearchCommand instance;
    TrainerDataCache tdc;
    MoveDataCache mdc;
    SkillDataCache sdc;
    PokemonDataCache pdc;

    private SearchCommand() {
        PokemasDBCache.getInstance(false);
        tdc = TrainerDataCache.getInstance();
        mdc = MoveDataCache.getInstance();
        sdc = SkillDataCache.getInstance();
        pdc = PokemonDataCache.getInstance();
    }

    public static SearchCommand getInstance() {
        return instance != null ? instance : (instance = new SearchCommand());
    }

    public void processSearch(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length < 3) {
            MiscUtil.send(e, Bot.current.INVALID_INPUTS, true);
            return;
        }
        if (!Cache.Type.isType(msg[1])) {
            MiscUtil.send(e, "Cannot find search method \"" + msg[1] + "\"", true);
            return;
        }
        String data = e.getMessage().getContentRaw().substring(msg[0].length() + msg[1].length() + 2);
        Cache.Type dataType = Cache.Type.parseType(msg[1]);
        if (!dataType.getCache().containsKey(data)) {
            MiscUtil.send(e, dataType.toString() + " \"" + data + "\" not found.", true);
            return;
        }
        EmbedBuilder eb;
        String toSend;
        if (dataType.isTurnable()) {
            int limit;
            if (dataType == TRAINER) {
                TrainerData td = tdc.get(data);
                eb = prepareEmbed(td, e.getAuthor().getName());
                limit = td.pokemon.length;
            } else {
                ArrayList<Pokemon> pd = pdc.get(data);
                eb = prepareEmbed(pd);
                limit = pd.size();
            }
            eb.setFooter(getFooter(1, limit + 1));
            e.getChannel().sendMessage(eb.build()).queue(response ->
                    this.put(e.getAuthor().getIdLong(),
                            new UserEmbedInfo(e.getAuthor().getIdLong(),
                                    response.getIdLong(),
                                    data,
                                    limit,
                                    dataType)));
            toSend = e.getAuthor().getAsMention() + ", You have opened a new " + dataType.toString().toLowerCase() + " search query!\n" +
                    "Any previous queries of yours will not be used to turn pages with ``" + Bot.current.PREFIX + "next`` & ``" + Bot.current.PREFIX + "back``";
            e.getChannel().sendMessage(toSend).queue();
        } else {
            if (dataType == MOVE) {
                eb = prepareEmbed(mdc.get(data));
            } else {
                eb = prepareEmbed(sdc.get(data));
            }
            eb.setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                    .setFooter("Requested by: " + e.getAuthor().getAsTag() + MiscUtil.dateTimeNow(), e.getAuthor().getEffectiveAvatarUrl());
            e.getChannel().sendMessage(eb.build()).queue();
            toSend = dataType.toString() + " Data for " + data;
        }
        Response.get(Bot.current.messagesProcessed).Responded(toSend);
    }

    public void processNavigation(GuildMessageReceivedEvent e) {
        boolean next = e.getMessage().getContentRaw().startsWith(Bot.current.PREFIX + "n");
        long aid = e.getAuthor().getIdLong();
        if (!this.containsKey(aid)) {
            return;
        }
        UserEmbedInfo uei = this.get(aid);
        String tos;
        delete(e.getMessage());
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
                    TrainerData tdata = tdc.get(uei.dataTitle);
                    if (start) {
                        eb = prepareEmbed(tdata, "");
                    } else {
                        eb = prepareEmbed(tdata.pokemonData.get(prog - 1));
                    }
                }
                m.editMessage(eb.setFooter(getFooter((start ? 0 : prog ) + 1, uei.limit + 1)).build()).queue();
                tos = "Changed the page from " + (prog + (next ? 0 : 1)) + " to " + (prog + (next ? 1 : 0)) + "\nJump to the message from the link: " + m.getJumpUrl();
            } catch (Exception exc) {
                uei.proceed(!next);
                tos = "An error occurred while finding the query message!";
                exc.printStackTrace();
            }
        } else {
            tos = "You're at the " + (next ? "end" : "start") + " of the query result. Can't turn the page any further";
        }
        e.getChannel().sendMessage(tos).queue();
    }


    public EmbedBuilder prepareEmbed(TrainerData td, String name) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(td.name + "'s Data");
        eb.addField("Details",
                "**Name:** " + td.name +
                        "\n**Rarity:** " + td.rarity + " Stars" +
                        "\n**Pokemons:** " + Array.toString(", ", (Object[]) td.pokemon, ""),
                false);
        eb.setImage(td.img);
        eb.setThumbnail(Bot.current.jda.getSelfUser().getEffectiveAvatarUrl());
        if (!"".equals(name) && name != null)
            eb.addField("Requested by", name, false);
        return eb;
    }

    public EmbedBuilder prepareEmbed(Pokemon pokemon) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(pokemon.name + "'s Data");
        eb.setDescription("Info about " + pokemon.trainer + "'s " + pokemon.name);
        eb.addField("Basic Info",
                "**Name:** " + pokemon.name +
                        "\n**Typing:** " + Array.toString(", ", (Object[]) pokemon.typing, "N/A") +
                        "\n**Weakness:** " + pokemon.weakness +
                        "\n**Role:** " + pokemon.role +
                        "\n**Rarity:** " + pokemon.rarity + " Stars" +
                        "\n**Gender:** " + pokemon.gender +
                        "\n**Other Forms:** " + Array.toString(", ", (Object[]) pokemon.otherForms, "N/A"),
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

    public EmbedBuilder prepareEmbed(MoveDataCache.Node node) {
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
                                : "\n**Power:** " + m.minPower + "->" + Math.round(m.minPower * 1.2)),
                false);
        eb.addField("Effect",
                m.effect,
                false);
        eb.addField("Usable by:",
                Array.toString(",\n",node.getUsers(),"None"),
                false);
        return eb;
    }

    public EmbedBuilder prepareEmbed(SkillDataCache.Node node) {
        Passive p = node.skill;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(p.name + "'s Data");
        eb.setDescription("Info about the skill: " + p.name);
        eb.addField("__Basic Info__",
                "**Name:** " + p.name +
                        "\n**Description:** " + p.description,
                false);
        Object[][] inbuilt = breakTillValid(node.getInbuilt());
        for (int i = 0; i<inbuilt.length; i++) {
            eb.addField(i==0?"It is present as the default passive skills of:":"",
                    Array.toString(",\n", inbuilt[i], i==0?"None":"")+(i==0?"":","),
                    false);
        }
        Object[][] ingrid = breakTillValid(node.getInGrid());
        for (int i = 0;i<ingrid.length; i++) {
            eb.addField(i==0?"It is present on the grid of:":"",
                    Array.toString(",\n", ingrid[i], i==0?"None":"")+(i==0?"":","),
                    false);
        }
        return eb;
    }

    public EmbedBuilder prepareEmbed(ArrayList<Pokemon> node) {
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
                "**Typing:** " + Array.toString(", ", (Object[]) first.typing, "N/A") +
                        "\n**Weakness:** " + first.weakness,
                false);
        return eb;
    }


    private String getStatsAsSimpleString(StatRange stats) {
        return "      |  HP | Atk | Def | SpA | SpD | Spe" +
               "\nBASE: | " + getWithSpaces(stats.base.hp) + " | " + getWithSpaces(stats.base.atk) + " | " + getWithSpaces(stats.base.def) + " | " + getWithSpaces(stats.base.spAtk) + " | " + getWithSpaces(stats.base.spDef) + " | " + getWithSpaces(stats.base.speed) +
               "\nMAX:  | " + getWithSpaces(stats.max .hp) + " | " + getWithSpaces(stats.max .atk) + " | " + getWithSpaces(stats.max .def) + " | " + getWithSpaces(stats.max .spAtk) + " | " + getWithSpaces(stats.max .spDef) + " | " + getWithSpaces(stats.max .speed) +
               "\nMax Bulk: " + stats.max.bulk;
    }

    private String getWithSpaces(int a) {
        return MiscUtil.space(3 - Integer.toString(a).length()) + a;
    }

    private String getMovesAsSimpleString(JSONArray<Move> moves) {
        StringJoiner tor = new StringJoiner("\n");
        int i = 1;
        for (Move move : moves) {
            tor.add("__Move " + (i++) + ": " + move.name +
                    "__\n-> It is a " + move.type + " type " + move.category + " move, which targets " + (move.target.equals("Self") ? "the user itself" : move.target.toLowerCase()) +
                    ".\n-> It uses " + (move.cost == 0 ? "no" : move.cost) + " bars & " + (move.accuracy == 0 ? "it never misses." : "has " + move.accuracy + "% accuracy.") +
                    "\n-> It" + (move.minPower == 0 ? " does not deal any direct damage." : "s power varies between " + move.minPower + "-" + Math.round(move.minPower * 1.2)) +
                    "\n-> Effect: " + move.effect);
        }
        return tor.toString().substring(0, tor.length() - 1);
    }

    private String getSyncMoveAsSimpleString(SyncMove sm) {
        return "__" + sm.name +
                "__\n-> It is a " + sm.type + " type " + sm.category + " move, which targets " + (sm.target.equals("Self") ? "the user itself" : sm.target.toLowerCase()) +
                "\n-> It" + (sm.minPower == 0 ? " does not deal any direct damage." : "s power varies between " + sm.minPower + "-" + Math.round(sm.minPower * 1.2)) +
                "\n-> Description: " + sm.description + "\n";
    }

    private String getPassivesAsSimpleString(JSONArray<Passive> passives) {
        StringJoiner tor = new StringJoiner("\n");
        int i = 1;
        for (Passive passive : passives) {
            tor.add( i + ". __" + passive.name + "__: " + passive.description);
        }
        return tor.toString().substring(0, tor.length() - 1);
    }

    private String getFooter(int a, int b) {
        return String.format("Showing page [%d/%d] | Use " + Bot.current.PREFIX + "next to go forward or " + Bot.current.PREFIX + "back to go backward.", a, b);
    }

    private void delete(Message m) {
        try {
            m.delete().complete();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static Object[][] breakIn(Object[] a, int interval) {
        ArrayList<Object[]> tor = new ArrayList<>();
        ArrayList<Object> temp = arrayToList(a);
        if (temp.size()<interval) {
            tor.add(a);
            return tor.toArray(new Object[0][]);
        }
        while (temp.size()>interval) {
            Object[] anArray = new Object[interval];
            for (int i = 0; i<anArray.length; i++) {
                anArray[i] = temp.get(0);
                temp.remove(0);
            }
            tor.add(anArray);
        }
        if (temp.size()>0) {
            tor.add(temp.toArray());
        }
        return tor.toArray(new Object[0][]);
    }

    private static boolean testIfAllValid(Object[][] a) {
        boolean tor = true;
        for (Object[] b : a) {
            tor = tor && (Array.toString(",\n",b,"None")+",").length()<1024;
        }
        return tor;
    }

    private static <T> ArrayList<T> arrayToList(T[] a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    private static Object[][] breakTillValid(Object[] a) {
        int current = 10;
        Object[][] tor = {a};
        while (current>0) {
            tor = breakIn(a,current--*10);
            if (testIfAllValid(tor)) {
                return tor;
            }
        }
        return tor;
    }
}