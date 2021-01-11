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

import net.vplaygames.VPlayGames.commands.*;
import net.vplaygames.VPlayGames.events.HelloEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import static net.vplaygames.VPlayGames.db.botresources.TOKEN;
import static net.vplaygames.VPlayGames.db.botresources.setBooted;

public class Bot
{
    public static void main(String[] args) throws Exception
    {
        JDA jda = JDABuilder.createDefault(TOKEN).build();
        setBooted();

        jda.awaitReady();

        jda.addEventListener(new HelloEvent());
        jda.addEventListener(new PMCommand());
        jda.addEventListener(new TrainerCommand());
        jda.addEventListener(new HelpCommands());
        jda.addEventListener(new TerminatingCommands());
        jda.addEventListener(new PingCommand());
        jda.addEventListener(new ChooseCommand());
        jda.addEventListener(new ViewCommand());
        jda.addEventListener(new SyncMoveLevel());
        jda.addEventListener(new StatRegisterCommand());
        jda.addEventListener(new VerifyCommand());
        jda.addEventListener(new CalculateDamageCommand());
        jda.addEventListener(new MiscCommands());
        jda.addEventListener(new WeatherCommand());
    }
}