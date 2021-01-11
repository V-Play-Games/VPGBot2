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
package net.vplaygames.VPlayGames.PokeMasDB.Caches;

import static net.vplaygames.VPlayGames.PokeMasDB.Util.MiscUtil.*;
import static java.lang.System.currentTimeMillis;

public class PokemasDBCache {
    private static PokemasDBCache instance;

    private PokemasDBCache(boolean log) {
        long startTime = currentTimeMillis();
        TrainerDataCache tdc = TrainerDataCache.getInstance(log);
        long temp = currentTimeMillis();
        PokemonDataCache PDC =
        PokemonDataCache.getInstance();
        MoveDataCache MDC =
        MoveDataCache.getInstance();
        SkillDataCache SDC =
        SkillDataCache.getInstance();
        tdc.processingTime = currentTimeMillis() - temp + tdc.processingTime;
        if (log) {
            String pSpeed = tdc.speed(tdc.processingTime);
            String dSpeed = tdc.speed(tdc.downloadTime);
            pSpeed += space(dSpeed.length() - pSpeed.length());
            dSpeed += space(pSpeed.length() - dSpeed.length());
            TrainerDataCache.log("Successfully finished processing of data of all trainers.\n", TrainerDataCache.LogMode.INFO, getClass());
            TrainerDataCache.log("Took " + msToString(currentTimeMillis() - startTime) + ".\n", TrainerDataCache.LogMode.INFO, getClass());
            TrainerDataCache.log("Process  - Speed: " + pSpeed + " Time: " + msToString(tdc.processingTime) + ".\n", TrainerDataCache.LogMode.INFO, getClass());
            TrainerDataCache.log("Download - Speed: " + dSpeed + " Time: " + msToString(tdc.downloadTime) + ".\n", TrainerDataCache.LogMode.INFO, getClass());
            TrainerDataCache.log("Processed " + bytesToString(tdc.totalProcessed * 2) + " of data.\n", TrainerDataCache.LogMode.INFO, getClass());
        }
    }

    public static PokemasDBCache getInstance() {
        return getInstance(true);
    }

    public static PokemasDBCache getInstance(boolean log) {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new PokemasDBCache(log));
            }
        } else {
            return instance != null ? instance : (instance = new PokemasDBCache(log));
        }
    }
}