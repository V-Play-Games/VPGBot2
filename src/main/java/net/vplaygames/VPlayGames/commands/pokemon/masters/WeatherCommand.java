/*
 * Copyright 2020-2021 Vaibhav Nargwani
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

import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.DamageAppCommand;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.core.Damage;

import static net.vplaygames.VPlayGames.core.Damage.Weather.*;

public class WeatherCommand extends DamageAppCommand {
    public WeatherCommand() {
        super("weather", 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        Damage d = Bot.DATA.get(e.getAuthor().getIdLong());
        switch (e.getArg(1)) {
            case "sunny":
            case "sun":
                d.setWeather(SUN);
                break;
            case "rainy":
            case "rain":
                d.setWeather(RAIN);
                break;
            case "sandstorm":
                d.setWeather(SANDSTORM);
                break;
            case "hail":
            case "hailstorm":
                d.setWeather(HAIL);
                break;
            default:
                d.setWeather(NORMAL);
        }
        e.send("Ok, so " + d.weather).queue();
    }
}
