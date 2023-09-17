package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.BlockSetHistory;
import me.stonetrench.reworldedit.WEBlockData;
import me.stonetrench.reworldedit.WEVector3I;
import me.stonetrench.reworldedit.WorldEditHelper;

import java.util.ArrayList;

public class ReplaceSelection extends CommandCompat {

    public ReplaceSelection(){
        super("/replace");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (args.length != 3) {
            commandExecutor.displayChatMessage(gameRegistry.translateKey("command.usage.id-meta-twice"));
            return;
        }
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }

        int[] ids = WorldEditHelper.ParseBlockId(args[1]);
        int idSource = ids[0];
        int metaSource = ids[1];

        if (idSource < 0 || idSource > GameRegistry.getInstance().getMaxBlockId()) {
            commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                    "command.error.invalid-block-id", Integer.toString(idSource)));
            return;
        }
        if (metaSource != -1 && (metaSource < 0 || metaSource > 15)) {
            commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                    "command.error.invalid-meta-data", Integer.toString(metaSource)));
            return;
        }

        ids = WorldEditHelper.ParseBlockId(args[2]);
        int idTarget = ids[0];
        int metaTarget = ids[1];

        if (idTarget < 0 || idTarget > GameRegistry.getInstance().getMaxBlockId()) {
            commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                    "command.error.invalid-block-id", Integer.toString(idTarget)));
            return;
        }
        if (metaTarget < 0 || metaTarget > 15) {
            commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                    "command.error.invalid-meta-data", Integer.toString(metaTarget)));
            return;
        }

        NetworkPlayer.NetworkPlayerController networkPlayerController = commandExecutor.getNetworkPlayerController();

        if (!networkPlayerController.hasSelection()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.no-area-selected"));
            return;
        }

        final int minX = networkPlayerController.getMinX();
        final int maxX = networkPlayerController.getMaxX();
        final int minY = networkPlayerController.getMinY();
        final int maxY = networkPlayerController.getMaxY();
        final int minZ = networkPlayerController.getMinZ();
        final int maxZ = networkPlayerController.getMaxZ();
        final long blockChanged = (maxX - (long) minX) * (maxX - minX) * (maxX - minX);
        if (blockChanged > 1000000) {
            commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                    "command.error.changing-too-many-blocks", Long.toString(blockChanged)));
            return;
        }
        commandExecutor.displayChatMessage(gameRegistry.translateKeyFormat(
                "command.changing-blocks", Long.toString(blockChanged)));

        ArrayList<WEBlockData> blocks = new ArrayList<>();

        final RegisteredWorld world = commandExecutor.getCurrentRegisteredWorld();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (world.getRegisteredBlockId(x, y, z) == idSource && (metaSource == -1 ||
                            world.getRegisteredBlockMetadata(x, y, z) == metaSource)) {
                        blocks.add(new WEBlockData(new WEVector3I(x, y, z), idTarget, metaTarget));
                    }
                }
            }
        }

        BlockSetHistory.ApplyBlockChanges(blocks, world);

        commandExecutor.displayChatMessage(gameRegistry.translateKey("command.done"));
    }
}
