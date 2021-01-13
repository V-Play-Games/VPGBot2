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
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.data.GameData.*;
import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;

public class ChooseCommand extends Command {
    public ChooseCommand() {
        super("choose", "c");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length!=2)
        {
            MiscUtil.send(e, Bot.INVALID_INPUTS,true);
            return;
        }
        int tstr,i;
        long aid=e.getAuthor().getIdLong();
        String toSend;
        if(Bot.DATA.containsKey(aid))
        {
            Damage d = Bot.DATA.get(aid);
            tstr=Strings.toInt(msg[1])-1;
            if(d.getAppStatus()==1)
            {
                if(tstr<0||tstr>=d.getUc().length)
                    toSend="Invalid Input. There is no trainer at that place.";
                else {
                    d.setUid(d.getUc()[tstr]);
                    d.setPid(d.getUid()%1000000);
                    toSend="\nThis means you want to calculate damage for "+returnSP(d.getUc()[tstr])+
                            "\nChoose the move for which you want to calculate the damage:";
                    d.setMSet(msets[d.getPid()%1000-1]);
                    for (i=1; i<=d.getMSet().length; i++)
                        toSend+="\n"+i+". "+moves[(d.getMSet()[i-1]-1)%1000];
                    toSend+="\n"+i+". "+smoves[d.getPid()%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(d.getMSet().length+1)+"\nusing the command ``"+Bot.PREFIX+"choose <choice>``";
                    d.updateAppStatus();
                }
            } else if(d.getAppStatus()==2)
            {
                if(tstr<0||tstr>d.getMSet().length)
                    toSend="Invalid Input. There is no move at that place.";
                else {
                    if (tstr==d.getMSet().length)
                    {
                        d.setMoveName(smoves[d.getPid()%1000-1]+" (Sync Move)");
                        d.setMInfo(sminfos[d.getPid()%1000-1]);
                    } else {
                        d.setSmd(d.getMSet()[tstr]/1000);
                        d.setMInfo(minfos[(d.getMSet()[tstr]-1)%1000]);
                        d.setMoveName(moves[(d.getMSet()[tstr]-1)%1000]);
                    }
                    toSend="You chose "+d.getMoveName()+".\n\nMove Info:-\nBase Power: "+d.getMInfo()[0]+
                            "\nCategory: "+((d.getMInfo()[2]==1)?"Special":"Physical") +
                            "\nReach: "+((d.getMInfo()[1]==1)?"All opponents":"An opponent")+
                            "\nType: "+types[d.getMInfo()[3]-1]+"\n\nUse ``"+Bot.PREFIX+"view move info`` to view this info again.";
                    if(d.getMInfo()[1]==1)
                    {
                        MiscUtil.send(e,toSend,true);
                        toSend="This move can affect more than 1 targets.\nHow many targets were on the field when the move was used?\n1. 1\n2. 2\n3. 3\nGive your choice in an integer number in the range of 1-3\nusing the command ``"+Bot.PREFIX+"choose <choice>``";
                    } else
                        d.updateAppStatus();
                    d.updateAppStatus();
                }
            } else if(d.getAppStatus()==3){
                tstr++;
                if(tstr<1||tstr>3)
                    toSend="Invalid no. of targets.";
                else {
                    d.setMod(2,tstr);
                    toSend="Set the no. of targets to "+tstr+".";
                    d.updateAppStatus();
                }
            } else
                toSend = "Cannot find a list to choose from.";
            Bot.DATA.put(aid,d);
        } else
            toSend=Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}
