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
package net.vplaygames.VPlayGames.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.db.database.trnrs;
import static net.vplaygames.VPlayGames.db.database.types;
import static net.vplaygames.VPlayGames.db.userdatabase.*;
import static net.vplaygames.VPlayGames.util.Array.returnID;

public class ViewCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        int user_pv = returnID(user_ids,e.getAuthor().getIdLong());
        if(msg[0].equals("v!view"))
        {
            String to_send;
            if(msg.length>=3)
            {
                String m1 = msg[1], m2 = msg[2];
                if(isRegistered(e.getAuthor().getIdLong()))
                {
                    if(m1.equals("trainer"))
                    {
                        if(app_stts[returnID(user_ids,e.getAuthor().getIdLong())]<1)
                            to_send="Choose a Trainer first!";
                        else if (m2.equals("name"))
                            to_send=trnrs[tids[user_pv]];
                        else if (m2.equals("id"))
                            to_send=(Integer.toString(tids[user_pv]));
                        else
                            to_send="Cannot find entry \""+m2+"\" in list \""+m1+"\"";
                    } else if (m1.equals("move"))
                    {
                        if(app_stts[returnID(user_ids,e.getAuthor().getIdLong())]<3)
                            to_send="Choose a Move first!";
                        else if (m2.equals("name"))
                            to_send=mvnams[user_pv];
                        else if (m2.equals("info"))
                            to_send="Move Info:-\nBase Power: "+mInfos[user_pv][0]+
                                    "\nCategory: "+((mInfos[user_pv][2]==1)?"Special":"Physical") +
                                    "\nReach: "+((mInfos[user_pv][1]==1)?"All opponents":"An opponent")+
                                    "\nType: "+types[mInfos[user_pv][3]-1];
                        else if (m2.equals("level"))
                            to_send = (smls[user_pv] != 0)?Integer.toString(smls[user_pv]):"Set the Sync Move Level first!";
                        else
                            to_send="Cannot find entry \""+m2+"\" in list \""+m1+"\"";
                    } else
                        to_send="Cannot find list \""+m1+"\"";
                } else
                    to_send="Cannot find a Pokemon Masters Damage Calculator created by "+e.getAuthor().getAsMention();
            } else
                to_send="Not enough inputs!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
