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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.SkillGroup;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.data.Bot.DATA;
import static net.vplaygames.VPlayGames.data.GameData.skillNames;

public class SkillCommand extends Command {
    public SkillCommand() {
        super("skill");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw(),skill,toSend;
        long aid = e.getAuthor().getIdLong();
        int tstr;
        if (DATA.containsKey(aid))
        {
            Damage d = DATA.get(aid);
            if (d.getAppStatus()>3)
            {
                skill=msg.substring(Bot.PREFIX.length()+6);
                tstr= Array.returnID(skillNames,skill.substring(0,skill.length()-(SkillGroup.isIntensive(skill)?2:0)));
                toSend="Wait... Let me check, if there is any skill with this name.\n";
                MiscUtil.send(e,toSend,true);
                if (skillNames[tstr].equals("NA"))
                    toSend="I cannot find any skill with that name. Maybe this skill isn't added in the bot yet.";
                else
                {
                    d.addSkill(skill);
                    toSend="Succesfully added "+ Strings.toProperCase(skill)+" as a skill in your damage app,";
                }
            } else
                toSend="Choose a move first!";
            DATA.put(aid,d);
        } else
            toSend=Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}