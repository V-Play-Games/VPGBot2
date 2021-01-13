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
package net.vplaygames.VPlayGames.core;

import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.data.GameData.*;

public class SkillGroup
{
    final boolean intensive;
    final int id;
    final String name;
    final String[] skillInfo;
    final ArrayList<Integer> intensity;
    Damage d;

    public SkillGroup(int i, boolean b, int[] in) {
        intensity = new ArrayList<>();
        intensive = b;
        id = i;
        name = skillNames[id];
        skillInfo = skillInfos[id];
        for (int j : in) intensity.add(j);
    }

    public SkillGroup(String skillName, boolean intense) {
        intensity = new ArrayList<>();
        intensive = intense;
        intensity.add(intensive ? Math.max(MiscUtil.charToInt(skillName.charAt(skillName.length() - 1)), 0) : 0);
        id = Array.returnID(skillNames, skillName.substring(0, skillName.length() - (intensive ? 2 : 0)));
        name = skillNames[id];
        skillInfo = skillInfos[id];
    }

    public String toString() {
        return "name = " + name +
                "\npassiveString = " + getPassiveString() +
                "\nmultiplierString = " + getMultiplierString() +
                "\nskillInfo = " + Arrays.toString(skillInfo) +
                "\nid = " + id +
                "\nintensity = " + intensity +
                "\nintensive = " + intensive;
    }

    public boolean isActive(Damage damn) {
        d=damn;
        boolean act1=(skillInfo[2].equals("s") && d.getMoveName().endsWith(" (Sync Move)")) || (skillInfo[2].equals("m")&&!d.getMoveName().endsWith(" (Sync Move)")) || skillInfo[2].equals("a");
        int t = (skillInfo[1].equals("t"))?1:0,b;
        String r = skillInfo[0];
        boolean act2=false;
        boolean j=false;
        switch (r.charAt(r.length()-1)) {
            case '-': j = true;
            case '+': r = r.substring(0, r.length() - 1);
        }
        switch (r)
        {
            case "always":
            case "ub": act2=true; break;
            case "wthr": act2=Array.sumAll(d.getWthr())==1; break;
            case "sun": act2=d.getWthr()[0]==1; break;
            case "rain": act2=d.getWthr()[1]==1; break;
            case "sand": act2=d.getWthr()[2]==1; break;
            case "hail": act2=d.getWthr()[3]==1; break;
            case "stts": act2=Array.sumAll(d.getStatus()[t])==1; break;
            case "par": act2=d.getStatus()[t][0]==1; break;
            case "slp": act2=d.getStatus()[t][1]==1; break;
            case "psn": act2=d.getStatus()[t][2]==1||d.getStatus()[t][3]==1; break;
            case "brn": act2=d.getStatus()[t][4]==1; break;
            case "frz": act2=d.getStatus()[t][5]==1; break;
            case "sstts": act2=Array.sumAll(d.getSStatus()[t])==1; break;
            case "fln": act2=d.getSStatus()[t][0]==1; break;
            case "cf": act2=d.getSStatus()[t][1]==1; break;
            case "trp": act2=d.getSStatus()[t][2]==1; break;
            case "ch": act2=d.getMod()[0]==1; break;
            case "se": act2=d.getMod()[1]==1; break;
            case "mg": act2=d.getGauge()>=1; break;
            case "atk": b=d.getBuffs()[t][0]; act2=j?b<0:b>0; break;
            case "spa": b=d.getBuffs()[t][1]; act2=j?b<0:b>0; break;
            case "def": b=d.getBuffs()[t][2]; act2=j?b<0:b>0; break;
            case "spd": b=d.getBuffs()[t][3]; act2=j?b<0:b>0; break;
            case "spe": b=d.getBuffs()[t][4]; act2=j?b<0:b>0; break;
            case "acc": b=d.getBuffs()[t][5]; act2=j?b<0:b>0; break;
            case "eva": b=d.getBuffs()[t][6]; act2=j?b<0:b>0; break;
            case "hpp": act2=d.getHPP()[t]>0; break;
        }
        return act1&&act2;
    }

    public void add(int a) {
        if (!intensive) return;
        intensity.add(a);
    }

    public String getPassiveString() {
        StringJoiner tor=new StringJoiner("+");
        intensity.forEach(i -> tor.add(i.toString()));
        return name+tor.toString();
    }

    public String getMultiplierString() {
        String tor="";
        for(int i:intensity) tor+="+"+getMultiplier(i);
        return tor.equals("") ? "0": tor;
    }

    public double getMultiplier() {
        return getMultiplier(Array.sumAll(intensity.toArray(new Integer[0])));
    }

    public double getMultiplier(int tintensity) {
        double tor;
        switch (skillInfo[0]) {
            case "mg": tor=d.getGauge()*tintensity/100.0; break;
            case "hppu": tor=d.getHPP()[0]*tintensity/100.0; break;
            case "hppt": tor=d.getHPP()[1]*tintensity/100.0; break;
            default:
                if (intensive)
                    tor=tintensity/10.0;
                else
                    tor=1+Math.abs(getStatDependency()/2.0);
        }
        return tor;
    }

    public int getStatDependency() {
        int t = (skillInfo[1].equals("t")) ? 1 : 0;
        int bi = Array.returnID(new String[]{"atk", "spa", "def", "spd", "spe", "acc", "eva"}, skillInfo[0].substring(0, skillInfo[0].length() - 1));
        return skillInfo[0].endsWith("-") ? Math.min(d.getBuffs()[t][bi], 0) : Math.max(d.getBuffs()[t][bi], 0);
    }

    public static boolean isIntensive(String n) {
        return !Array.contains(n,unintensiveSkills);
    }

    public boolean isIntensive() {
        return intensive;
    }

    public String translateToString() {
        return "["+id+","+(intensive?1:0)+","+Array.toString("",intensity.toArray(),"0")+"]";
    }

    public static SkillGroup parse(String skillString) {
        if(skillString.equals("[]")) return null;
        String[] ss=skillString.substring(1,skillString.length()-1).split(",");
        return new SkillGroup(Strings.toInt(ss[0]),
                ss[1].equals("1"),
                Array.stringToInt(ss[2].split("")));
    }

    public String getName() {
        return name;
    }
}