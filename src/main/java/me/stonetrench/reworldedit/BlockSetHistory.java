package me.stonetrench.reworldedit;

import com.fox2code.foxloader.registry.RegisteredWorld;

import java.util.ArrayList;

public class BlockSetHistory {
    private static final ArrayList<ArrayList<WEBlockData>> PreviousBlocks = new ArrayList<>();
    private static ArrayList<ArrayList<WEBlockData>> UndidBlocks = new ArrayList<>();

    public static void ApplyBlockChanges(ArrayList<WEBlockData> changes, RegisteredWorld world) {
        UndidBlocks = new ArrayList<>();

        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        PreviousBlocks.add(result);

        if (PreviousBlocks.size() > 16)
            PreviousBlocks.remove(0);
    }

    public static boolean UndoPreviousChanges(RegisteredWorld world){
        if (PreviousBlocks.size() == 0) return false;
        ArrayList<WEBlockData> changes = PreviousBlocks.remove(PreviousBlocks.size() - 1);
        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        UndidBlocks.add(result);
        return true;
    }
    public static boolean RedoPreviousChanges(RegisteredWorld world) {
        if (UndidBlocks.size() == 0) return false;
        ArrayList<WEBlockData> changes = UndidBlocks.remove(UndidBlocks.size() - 1);
        ArrayList<WEBlockData> result = new ArrayList<>();

        for (WEBlockData block : changes)
            result.add(WorldEditHelper.GetBlockAt(block.position, world));
        for (WEBlockData block : changes)
            world.setRegisteredBlockAndMetadataWithNotify(block.position.X, block.position.Y, block.position.Z, block.Id, block.Meta);

        PreviousBlocks.add(result);

        if (PreviousBlocks.size() > 16)
            PreviousBlocks.remove(0);
        return true;
    }
}
