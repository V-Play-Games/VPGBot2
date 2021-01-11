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
package net.vplaygames.VPlayGames.db;

public class botresources
{
    public static final long BOT_OWNER = 701660977258561557L;
    public static final String TOKEN = System.getenv("TOKEN");
    public static String prefix = "v!";
    public static long booted = 0;
    public static void setBooted()
    {
        booted=System.currentTimeMillis();
    }
}
