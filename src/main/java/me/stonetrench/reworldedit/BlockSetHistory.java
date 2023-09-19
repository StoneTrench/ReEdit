package me.stonetrench.reworldedit;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.RegisteredWorld;

import java.util.ArrayList;

public class BlockSetHistory {

    private static final PerPlayerDataHandling<ArrayList<ArrayList<WEBlockData>>> PlayerPreviousBlocks = new PerPlayerDataHandling<>();
    private static final PerPlayerDataHandling<ArrayList<ArrayList<WEBlockData>>> PlayerUndidBlocks = new PerPlayerDataHandling<>();

    public static void ApplyBlockChanges(NetworkPlayer player, ArrayList<WEBlockData> changes, RegisteredWorld world) {
        PlayerUndidBlocks.GetPlayerData(player, new ArrayList<>()).clear();

        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        ArrayList<ArrayList<WEBlockData>> prevs = PlayerPreviousBlocks.GetPlayerData(player, new ArrayList<>());

        prevs.add(result);

        if (prevs.size() > 16)
            prevs.remove(0);
    }

    public static boolean UndoPreviousChanges(NetworkPlayer player, RegisteredWorld world){
        ArrayList<ArrayList<WEBlockData>> prevs = PlayerPreviousBlocks.GetPlayerData(player, new ArrayList<>());

        if (prevs.size() == 0) return false;
        ArrayList<WEBlockData> changes = prevs.remove(prevs.size() - 1);
        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        PlayerUndidBlocks.GetPlayerData(player, new ArrayList<>()).add(result);
        return true;
    }
    public static boolean RedoPreviousChanges(NetworkPlayer player, RegisteredWorld world) {
        ArrayList<ArrayList<WEBlockData>> undid = PlayerUndidBlocks.GetPlayerData(player, new ArrayList<>());

        if (undid.size() == 0) return false;
        ArrayList<WEBlockData> changes = undid.remove(undid.size() - 1);
        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        PlayerPreviousBlocks.GetPlayerData(player, new ArrayList<>()).add(result);
        return true;
    }
}
