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
package net.vplaygames.VPlayGames;

import net.vplaygames.VPlayGames.BotStaffCommands.LogCommand;
import net.vplaygames.VPlayGames.events.BotPingedEvent;
import net.vplaygames.VPlayGames.events.HelloEvent;
import net.vplaygames.VPlayGames.db.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import static net.vplaygames.VPlayGames.db.botresources.*;

public class Bot
{
    public static void main(String[] args) throws Exception
    {
        JDA jda = JDABuilder.createDefault(TOKEN).setActivity(Activity.playing("v!pm start")).build();
        setBooted();
        initStaff();
        jda.awaitReady();
        jda.addEventListener(new HelloEvent(),new BotPingedEvent(),new CommandManager(),new LogCommand());
    }
}