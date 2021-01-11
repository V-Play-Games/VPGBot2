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

import net.vplaygames.VPlayGames.db.Damage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.database.*;

public class ChooseCommand
{
    public static void ChooseCommand(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        int tstr,i;
        long aid=e.getAuthor().getIdLong();
        String to_send;
        if(data.containsKey(aid))
        {
            Damage d = data.get(aid);
            tstr=Integer.parseInt(msg[1])-1;
            if(d.getApp_stts()==1)
            {
                if(tstr<0||tstr>=d.getUc().length)
                    to_send="Invalid Input. There is no trainer at that place.";
                else {
                    d.setUid(d.getUc()[tstr]);
                    d.setPid(d.getUc()[tstr]%1000000);
                    to_send="\nThis means you want to calculate damage for "+returnSP(d.getUc()[tstr])+
                            "\nChoose the move for which you want to calculate the damage:";
                    d.setMSet(msets[d.getPid()%1000-1]);
                    for (i=1; i<=d.getMSet().length; i++)
                        to_send+="\n"+i+". "+moves[(d.getMSet()[i-1]-1)%1000];
                    to_send+="\n"+i+". "+smoves[d.getPid()%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(d.getMSet().length+1)+"\nusing the command ``"+prefix+"choose <choice>``";
                    d.app_stts();
                }
            } else if(d.getApp_stts()==2)
            {
                if(tstr<0||tstr>d.getMSet().length)
                    to_send="Invalid Input. There is no move at that place.";
                else {
                    if (tstr==d.getMSet().length)
                    {
                        d.setMvnam(smoves[d.getPid()%1000-1]+" (Sync Move)");
                        d.setMInfo(sminfos[d.getPid()%1000-1]);
                    } else {
                        d.setSmd(d.getMSet()[tstr]/1000);
                        d.setMInfo(minfos[(d.getMSet()[tstr]-1)%1000]);
                        d.setMvnam(moves[(d.getMSet()[tstr]-1)%1000]);
                    }
                    to_send="You chose "+d.getMvnam()+".\n\nMove Info:-\nBase Power: "+d.getMInfo()[0]+
                            "\nCategory: "+((d.getMInfo()[2]==1)?"Special":"Physical") +
                            "\nReach: "+((d.getMInfo()[1]==1)?"All opponents":"An opponent")+
                            "\nType: "+types[d.getMInfo()[3]-1]+"\n\nUse ``"+prefix+"view move info`` to view this info again.";
                    if(d.getMInfo()[1]==1)
                    {
                        e.getChannel().sendMessage(to_send).queue();
                        to_send="This move can affect more than 1 targets.\nHow many targets were on the field when the move was used?\n1. 1\n2. 2\n3. 3\nnGive your choice in an integer number in the range of 1-3\nusing the command ``"+prefix+"choose <choice>``";
                    } else
                        d.app_stts();
                    d.app_stts();
                }
            } else if(d.getApp_stts()==3){
                tstr++;
                if(tstr<1||tstr>3)
                    to_send="Invalid no. of targets.";
                else {
                    d.setMod(2,tstr);
                    to_send="Set the no. of targets to"+tstr;
                }
            } else
                to_send = "Cannot find a list to choose from";
            data.put(aid,d);
        } else
            to_send="Start a PM Damage Calculator App first!";
        e.getChannel().sendMessage(to_send).queue();
    }
}
