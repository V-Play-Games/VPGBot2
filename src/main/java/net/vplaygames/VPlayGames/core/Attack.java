package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.entities.Move;
import com.vplaygames.PM4J.entities.SyncMove;
import com.vplaygames.PM4J.jsonFramework.ParsableJSONObject;

public class Attack {
    public final String name;
    public final String type;
    public final String category;
    public final int minPower;
    public final String target;
    public final String effect;
    public final ParsableJSONObject<?> base;
    public final boolean isSync;

    public Attack(Move move) {
        this.base = move;
        this.name = move.name;
        this.type = move.type;
        this.category = move.category;
        this.minPower = move.minPower;
        this.target = move.target;
        this.effect = move.effect;
        this.isSync = false;
    }

    public Attack(SyncMove sync) {
        this.base = sync;
        this.name = sync.name + " (Sync Move)";
        this.type = sync.type;
        this.category = sync.category;
        this.minPower = sync.minPower;
        this.target = sync.target;
        this.effect = sync.description;
        this.isSync = true;
    }
}
