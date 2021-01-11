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

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.database.*;
import static net.vplaygames.VPlayGames.db.userdatabase.*;
import static net.vplaygames.VPlayGames.util.Array.returnID;

public class ChooseCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw();
        int tstr,user_pv=returnID(user_ids,e.getAuthor().getIdLong()),i;
        String to_send;
        if (!e.getAuthor().isBot()&&msg.startsWith(prefix+"choose ")&&msg.length()>prefix.length()+7)
        {
            tstr=Integer.parseInt(msg.substring(prefix.length()+7))-1;
            if(app_stts[user_pv]==1)
            {
                if(tstr<0||tstr>=ucs[user_pv].length)
                    to_send="Invalid Input. There is no trainer at that place.";
                else {
                    uids[user_pv]=ucs[user_pv][tstr];
                    pids[user_pv]=ucs[user_pv][tstr]%1000000;
                    to_send="\nThis means you want to calculate damage for "+returnSP(ucs[user_pv][tstr])+
                            "\nChoose the move for which you want to calculate the damage:";
                    mSets[user_pv]=msets[pids[user_pv]%1000-1];
                    for (i=1; i<=mSets[user_pv].length; i++)
                        to_send+="\n"+i+". "+moves[(mSets[user_pv][i-1]-1)%1000];
                    to_send+="\n"+i+". "+smoves[pids[user_pv]%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(mSets[user_pv].length+1)+"\nusing the command ``"+prefix+"choose <choice>``";
                    app_stts[user_pv] = 2;
                }
            }
            else if(app_stts[user_pv]==2)
            {
                if(tstr<0||tstr>mSets.length)
                    to_send="Invalid Input. There is no move at that place.";
                else {
                    if (tstr==mSets[user_pv].length)
                    {
                        mvnams[user_pv]=smoves[pids[user_pv]%1000-1]+" (Sync Move)";
                        mInfos[user_pv]=template;
                        for (i=0; i<3; i++)
                            mInfos[user_pv][(i==0)?i:i+1]=sminfos[pids[user_pv]%1000-1][i];
                    } else {
                        smds[user_pv]=mSets[user_pv][tstr]/1000;
                        mInfos[user_pv]=minfos[(mSets[user_pv][tstr]-1)%1000];
                        mvnams[user_pv]=moves[(mSets[user_pv][tstr]-1)%1000];
                    }
                    to_send="You chose "+mvnams[user_pv]+".\n\nMove Info:-\nBase Power: "+mInfos[user_pv][0]+
                            "\nCategory: "+((mInfos[user_pv][2]==1)?"Special":"Physical") +
                            "\nReach: "+((mInfos[user_pv][1]==1)?"All opponents":"An opponent")+
                            "\nType: "+types[mInfos[user_pv][3]-1]+"\n\nUse ``"+prefix+"view move info`` to view this info again.";
                    app_stts[user_pv]=3;
                }
            } else {
                to_send="Cannot find a list to choose from";
            }
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
