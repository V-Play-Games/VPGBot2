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
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.DATA;
import static net.vplaygames.VPlayGames.data.Bot.PREFIX;
import static net.vplaygames.VPlayGames.data.GameData.*;

public class TrainerCommand extends Command {
    public TrainerCommand() {
        super("trainer");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw(),trnr,toSend;
        long aid = e.getAuthor().getIdLong();
        int tstr;
        if (DATA.containsKey(aid))
        {
            Damage d = DATA.get(aid);
            if (d.getAppStatus()==0)
            {
                trnr=msg.substring(PREFIX.length()+8);
                tstr= Array.returnID(trnrs, trnr);
                toSend="Wait... Let me check, if there is any trainer with this name.\n";
                MiscUtil.send(e,toSend,true);
                if (trnrs[tstr].equals("NA"))
                    toSend="I cannot find any trainer with that name. Maybe this trainer is not usable in the game yet.";
                else {
                    toSend="Oh, you want to calculate damage for sync pairs associated with the trainer "+trnrs[tstr]+".\n\nI will show you the sync pairs including this trainer.";
                    d.setTid((tstr==0)?1:tstr);
                    d.updateAppStatus();
                    toSend+=MiscUtil.returnSPs(d.getTid());
                    d.setUc(tdabs[d.getTid()-1]);
                    int[] uc = d.getUc();
                    MiscUtil.send(e,toSend,true);
                    if (uc.length==1)
                    {
                        int i;
                        d.setUid(uc[0]);
                        d.setPid(uc[0]%1000000);
                        toSend="Oh, there is only one sync pair found."+
                                "\nThis means you want to calculate damage for "+MiscUtil.returnSP(uc[0])+
                                "\nChoose the move for which you want to calculate the damage:";
                        d.setMSet(msets[d.getPid()%1000-1]);
                        for (i=1; i<=d.getMSet().length; i++)
                            toSend+="\n"+i+". "+moves[(d.getMSet()[i-1]-1)%1000];
                        toSend+="\n"+i+". "+smoves[d.getPid()%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(d.getMSet().length+1)+"\nusing the command ``"+ PREFIX+"choose <choice>``";
                        d.updateAppStatus();
                    } else
                        toSend= "\nGive your choice in an integer number in the range of 1-"+uc.length+"\nusing the command ``"+ PREFIX+"choose <choice>``";
                }
            } else
                toSend="You have already chosen the trainer for this Damage Calculation Application.";
            DATA.put(aid,d);
        } else
            toSend= Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}