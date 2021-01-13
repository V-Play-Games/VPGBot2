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

import net.vplaygames.VPlayGames.PokeMasDB.Connection;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.Trainer;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.TrainerData;
import net.vplaygames.VPlayGames.PokeMasDB.Exceptions.CachingException;
import net.vplaygames.VPlayGames.PokeMasDB.JSONFramework.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalTime;

import static net.vplaygames.VPlayGames.PokeMasDB.Entities.Constant.EMPTY_TRAINER;
import static net.vplaygames.VPlayGames.PokeMasDB.Util.MiscUtil.*;
import static java.lang.System.currentTimeMillis;

public class TrainerDataCache extends Cache<TrainerData>
{
    private static TrainerDataCache instance;

    public final JSONArray<Trainer> TrainerCache;
    public final JSONArray<TrainerData> DataCache;
    long totalProcessed;
    long processingTime;
    long downloadTime;

    private TrainerDataCache(boolean log) {
        try {
            long temp = currentTimeMillis();
            log("Commencing the download & parse of data from https://pokemasdb.com\n", LogMode.INFO, log);
            Connection conn = new Connection();
            log("Downloading the list of trainers.\n", LogMode.DEBUG, log);
            processingTime = currentTimeMillis();
            String tcache = conn.requestTrainers();
            downloadTime = currentTimeMillis();
            log("Downloaded the list of all trainers.\n", LogMode.INFO, log);
            String toPrint = "Downloading Trainer Data.";
            int first = log(toPrint, LogMode.DEBUG).length();
            TrainerCache = JSONArray.parseFromJSON(
                    (org.json.simple.JSONArray) ((JSONObject) new JSONParser().parse(tcache)).get("trainers"),
                    EMPTY_TRAINER);
            temp = (currentTimeMillis() - downloadTime) + (processingTime - temp);
            downloadTime = downloadTime - processingTime;
            int len = TrainerCache.size();
            int maxNameLen = 12;
            int i = 0;
            totalProcessed = tcache.length();
            String[] localDataCache = new String[len];
            processingTime = temp;
            while (i < len) {
                temp = currentTimeMillis();
                String name = TrainerCache.get(i).name;
                try {
                    localDataCache[i] = conn.requestTrainer(name);
                } catch (Exception exc) {
                    if (log) {
                        System.out.println();
                        log("An error occurred while downloading " + name + "'s data.\n"+exc.getMessage(), LogMode.ERROR);
                        toPrint = "";
                        log("", LogMode.DEBUG);
                    }
                    i++;
                    continue;
                }
                totalProcessed += localDataCache[i].length();
                downloadTime += (currentTimeMillis() - temp);
                if (log) {
                    System.out.print(backspace(toPrint.length()));
                    toPrint = "Total Progress: " + numberToString(5, ++i * 100.0 / len) + "% | Downloaded " + name + "'s Data" + space(maxNameLen - name.length()) + " | Download Speed: " + speed(downloadTime);
                    System.out.print(toPrint);
                }
            }
            i = 0;
            totalProcessed = 0;
            JSONArray<TrainerData> data = new JSONArray<>();
            while (i < len) {
                temp = currentTimeMillis();
                String name = TrainerCache.get(i).name;
                String json = localDataCache[i];
                if (json.equals("")) {
                    i++;
                    continue;
                }
                TrainerData datum;
                try {
                    datum = TrainerData.parse(json);
                } catch (Exception exc) {
                    if (log) {
                        System.out.println();
                        log("An error occurred while parsing " + name + "'s data.\n", LogMode.ERROR);
                        exc.printStackTrace();
                        toPrint = "";
                        log("", LogMode.DEBUG);
                    }
                    i++;
                    continue;
                }
                data.add(datum);
                this.put(name, datum);
                totalProcessed += json.length();
                processingTime += currentTimeMillis() - temp;
                if (log) {
                    System.out.print(backspace(toPrint.length()));
                    toPrint = "Total Progress: " + numberToString(5, ++i * 100.0 / len) + "% | Parsed " + name + "'s Data" + space(maxNameLen - name.length()) + " | Processing Speed: " + speed(processingTime);
                    System.out.print(toPrint);
                }
            }
            DataCache = data;
            if (log) {
                System.out.print(backspace(first + toPrint.length()));
                log("Parsed data for all the trainers.\n", LogMode.INFO);
            }
            conn.close();
        } catch (IOException | ParseException exc) {
            if (log) System.out.println();
            throw new CachingException(exc);
        }
    }

    public static TrainerDataCache getInstance() throws CachingException {
        return getInstance(true);
    }

    public static TrainerDataCache getInstance(boolean log) throws CachingException {
        if (log) {
            synchronized (System.out) {
                return instance != null ? instance : (instance = new TrainerDataCache(true));
            }
        } else {
            return instance != null ? instance : (instance = new TrainerDataCache(false));
        }
    }

    String speed(long time) {
        return bytesToString(Math.round(totalProcessed*2*1000.0/time))+"/sec";
    }

    private String log(String s, LogMode mode) {
        return log(s, mode, getClass());
    }

    private String log(String s, LogMode mode, boolean print) {
        return log(s, mode, getClass(), print);
    }

    static String log(String s, LogMode mode, Class<?> clazz) {
        return log(s, mode, clazz, true);
    }

    static String log(String s, LogMode mode, Class<?> clazz, boolean print) {
        String top = LocalTime.now().toString() + " ["+Thread.currentThread().getName()+"] "+mode+" by " + clazz.getName() + " - " + s;
        if (print) System.out.print(top);
        return top;
    }

    enum LogMode {
        INFO, DEBUG, WARN, ERROR
    }
}