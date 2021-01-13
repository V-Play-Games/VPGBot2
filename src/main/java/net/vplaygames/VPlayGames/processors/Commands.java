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
package net.vplaygames.VPlayGames.processors;

import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.data.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class Commands {
    public static boolean process(GuildMessageReceivedEvent e) {
        String MSG = e.getMessage().getContentRaw();
        boolean resp = false;
        if (MSG.startsWith(Bot.PREFIX) && MSG.length() > Bot.PREFIX.length()) {
            String[] msg = split(MSG);
            if (Array.contains(msg[0], Bot.STAFF_COMMANDS))
                resp = BotStaffCommands.process(e);
            if (Array.contains(msg[0], Bot.COMMANDS))
                resp = resp || CommonCommands.process(e);
        }
        return resp;
    }

    public static String[] split(String s) {
        String[] tor;
        if (s.contains("\n")) {
            ArrayList<String> temp = new ArrayList<>();
            for (String s1 : s.split("\n")) {
                if (s1.contains(" ")) {
                    temp.addAll(Arrays.asList(s1.split(" ")));
                } else {
                    temp.add(s1);
                }
            }
            tor = temp.toArray(new String[0]);
            if (tor[0].startsWith(Bot.PREFIX))
                tor[0] = tor[0].substring(Bot.PREFIX.length());
        } else {
            tor = s.substring(Bot.PREFIX.length()).split(" ");
        }
        return tor;
    }
}
