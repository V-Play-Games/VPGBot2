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

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.SkillGroup;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static net.vplaygames.VPlayGames.data.GameData.skillNames;

public class SkillCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),skill,to_send;
        long aid = e.getAuthor().getIdLong();
        int tstr;
        if (Bot.current.DATA.containsKey(aid))
        {
            Damage d = Bot.current.DATA.get(aid);
            if (d.getAppStatus()>3)
            {
                skill=msg.substring(Bot.current.PREFIX.length()+6);
                tstr= Array.returnID(skillNames,skill.substring(0,skill.length()-(SkillGroup.isIntensive(skill)?2:0)));
                to_send="Wait... Let me check, if there is any skill with this name.\n";
                MiscUtil.send(e,to_send,true);
                if (skillNames[tstr].equals("NA"))
                    to_send="I cannot find any skill with that name. Maybe this skill isn't added in the bot yet.";
                else
                {
                    d.addSkill(skill);
                    to_send="Succesfully added "+ Strings.toProperCase(skill)+" as a skill in your damage app,";
                }
            } else
                to_send="Choose a move first!";
            Bot.current.DATA.put(aid,d);
        } else
            to_send="To use this command, you have to open up a new Damage Calculation Application.";
        MiscUtil.send(e,to_send,true);
    }
}