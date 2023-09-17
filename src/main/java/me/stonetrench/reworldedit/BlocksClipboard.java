package me.stonetrench.reworldedit;

import java.util.ArrayList;

public class BlocksClipboard {
    private static ArrayList<WEBlockData> Blocks;

    public static void SetClipboard(ArrayList<WEBlockData> blocks, WEVector3I origin) {
        Blocks = new ArrayList<>();
        for(WEBlockData block : blocks){
            Blocks.add(new WEBlockData(block.position.Clone().Sub(origin), block.Id, block.Meta));
        }
    }
    public static ArrayList<WEBlockData> GetClipboard(WEVector3I origin){
        ArrayList<WEBlockData> result = new ArrayList<>();
        for(WEBlockData block : Blocks){
            result.add(new WEBlockData(block.position.Clone().Add(origin), block.Id, block.Meta));
        }
        return result;
    }
}
