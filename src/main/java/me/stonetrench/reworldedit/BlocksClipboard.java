package me.stonetrench.reworldedit;

import com.fox2code.foxloader.network.NetworkPlayer;

import java.util.ArrayList;

public class BlocksClipboard {
    private static final PerPlayerDataHandling<ArrayList<WEBlockData>> PlayerBlocks = new PerPlayerDataHandling<>();

    public static void SetClipboard(NetworkPlayer player, ArrayList<WEBlockData> blocks, WEVector3I origin) {
        ArrayList<WEBlockData> Blocks = PlayerBlocks.GetPlayerData(player, new ArrayList<>());
        Blocks.clear();

        for(WEBlockData block : blocks){
            Blocks.add(new WEBlockData(block.position.Clone().Sub(origin), block.Id, block.Meta));
        }
    }
    public static ArrayList<WEBlockData> GetClipboard(NetworkPlayer player, WEVector3I origin){
        ArrayList<WEBlockData> Blocks = PlayerBlocks.GetPlayerData(player, new ArrayList<>());

        ArrayList<WEBlockData> result = new ArrayList<>();
        for(WEBlockData block : Blocks){
            result.add(new WEBlockData(block.position.Clone().Add(origin), block.Id, block.Meta));
        }
        return result;
    }
}
