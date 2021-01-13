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

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class EmbedUtil {
    public static Object[][] breakIn(Object[] a, int interval) {
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

    public static boolean testIfAllValid(Object[][] a) {
        return testIfAllValid(a,",\n",",","None");
    }

    public static boolean testIfAllValid(Object[][] a, String delimiter1, String delimiter2, String def) {
        boolean tor = true;
        for (Object[] b : a) {
            tor = tor && (Array.toString(delimiter1,b,def)+delimiter2).length()<1024;
        }
        return tor;
    }

    public static <T> ArrayList<T> arrayToList(T[] a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    public static Object[][] breakTillValid(Object[] a) {
        int current = 10;
        Object[][] tor = {a};
        while (current>0) {
            tor = breakIn(a,current--*10);
            if (testIfAllValid(tor,"","","")) {
                return tor;
            }
        }
        return tor;
    }

    public static EmbedBuilder prepareEmbed(Throwable t) {
        EmbedBuilder eb = new EmbedBuilder();
        Object[][] stack = breakTillValid(Strings.valueOf(t).split("\n"));
        eb.setTitle("Error Occurred [" + Bot.LOCATION + "]");
        eb.addField("Exception ID", String.valueOf(Bot.lastExceptionID), true);
        eb.addField("Last Response", String.valueOf(Bot.messagesProcessed), true);
        for (int i = 0; i < stack.length; i++) {
            eb.addField((i == 0 ? "Exception Trace:" : ""),
                    Array.toString("\n", stack[i], "\t**[Erroneous stack]**"),
                    false);
        }
        GuildMessageReceivedEvent current = EventHandler.getCurrent();
        if (current != null) {
            eb.addField("Probable Cause",
                    "**Type:** Guild Message" +
                            "\n**Sender:** " + current.getAuthor().getAsMention() +
                            "\n**Channel:** " + current.getChannel().getAsMention(),
                    false);
            eb.addField("Message", current.getMessage().getContentRaw(), false);
        }
        eb.setColor(0xff0000);
        return eb;
    }

    public static EmbedBuilder addFieldSafely(String title, Object[] value, boolean inline, String delimiter1, String delimiter2, String def, EmbedBuilder eb) {
        Object[][] doubleArray = EmbedUtil.breakTillValid(value);
        for (int i = 0; i<doubleArray.length; i++) {
            eb.addField(i==0?title:"",
                    Array.toString(delimiter1, doubleArray[i], i==0?def:"")+(i==0?"":delimiter2),
                    inline);
        }
        return eb;
    }

    public static EmbedBuilder addFieldSafely(String title, String value, boolean inline, EmbedBuilder eb) {
        return addFieldSafely(title, charArrayToCharacterArray(value.toCharArray()), inline, "", "", "", eb);
    }

    private static Character[] charArrayToCharacterArray(char[] array) {
        Character[] tor = new Character[array.length];
        for (int i =0; i<array.length; i++) {
            tor[i] = array[i];
        }
        return tor;
    }
}
