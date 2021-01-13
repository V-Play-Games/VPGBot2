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
package net.vplaygames.VPlayGames.PokeMasDB.Util;

import net.vplaygames.VPlayGames.util.MiscUtil;

public class Strings {
    public static int toInt(String a) {
        int rtrn = 0;
        for (int i = 0; i < a.length(); i++)
            rtrn = rtrn * 10 + ((MiscUtil.charToInt(a.charAt(i)) == 10) ? -rtrn * 9 : MiscUtil.charToInt(a.charAt(i)));
        return rtrn;
    }

    public static boolean equalsAnyIgnoreCase(String b, String... a) {
        boolean bool = false;
        for (String s : a) bool = bool || (b.equalsIgnoreCase(s));
        return bool;
    }
}
