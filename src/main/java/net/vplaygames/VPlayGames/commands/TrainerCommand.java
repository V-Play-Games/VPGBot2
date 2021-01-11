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
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.database.*;
import static net.vplaygames.VPlayGames.util.Array.returnID;

public class TrainerCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),trnr,to_send;
        long aid = e.getAuthor().getIdLong();
        int tstr;
        if (msg.startsWith(prefix+"trainer ")&&!e.getAuthor().isBot())
        {
            if (data.containsKey(aid))
            {
                Damage d = data.get(aid);
                if (d.getApp_stts()==0)
                {
                    trnr=msg.substring(prefix.length()+8);
                    tstr=returnID(trnrs, trnr);
                    e.getChannel().sendMessage("Wait... Let me check, if there is any trainer with this name.\n").queue();
                    if (trnrs[tstr].equals("NA"))
                        to_send="I cannot find any trainer with that name. Maybe this trainer is not usable in the game yet.";
                    else
                    {
                        to_send="Oh, you want to calculate damage for sync pairs associated with the trainer "+trnrs[tstr]+".\n\nI will show you the sync pairs including this trainer.";
                        d.setTid((tstr==0)?1:tstr);
                        d.app_stts();
                        to_send+=returnSPs(d.getTid());
                        d.setUc(tdabs[d.getTid()-1]);
                        int[] uc = d.getUc();
                        e.getChannel().sendMessage(to_send).queue();
                        if (uc.length==1)
                        {
                            int i;
                            tstr=0;
                            d.setUid(uc[tstr]);
                            d.setPid(uc[tstr]%1000000);
                            to_send="Oh, there is only one sync pair found."+
                                    "\nThis means you want to calculate damage for "+returnSP(uc[tstr])+
                                    "\nChoose the move for which you want to calculate the damage:";
                            d.setMSet(msets[d.getPid()%1000-1]);
                            for (i=1; i<=d.getMSet().length; i++)
                                to_send+="\n"+i+". "+moves[(d.getMSet()[i-1]-1)%1000];
                            to_send+="\n"+i+". "+smoves[d.getPid()%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(d.getMSet().length+1)+"\nusing the command ``"+prefix+"choose <choice>``";
                            d.app_stts();
                        } else
                            to_send= "\nGive your choice in an integer number in the range of 1-"+uc.length+"\nusing the command ``"+prefix+"choose <choice>``";
                    }
                } else
                    to_send="You have already chosen the trainer for this Damage Calculation Application.";
                data.put(aid,d);
            } else
                to_send="To use this command, you have to open up a new Damage Calculation Application.";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
    public static String returnSPs(int tid)
    {
        StringBuilder to_return = new StringBuilder();
        for (int i=0; i<tdabs[tid-1].length; i++)
            to_return.append("\n").append(i + 1).append(". ").append(returnSP(tdabs[tid - 1][i]));
        return to_return.toString();
    }
    public static String returnSP(int uid)
    {
        String sp=" and "+pkmns[uid%1000-1],trnr=trnrs[uid/1000%1000];
        switch (uid/10000000)
        {
            case 1:
                trnr="Sygna Suit "+trnr;
                break;
            case 2:
                trnr+="(Holiday 20"+(19+(uid/1000000)%10)+")";
                break;
            case 3:
                trnr+="(Summer 20"+(19+(uid/1000000)%10)+")";
        }
        return trnr+sp;
    }
}