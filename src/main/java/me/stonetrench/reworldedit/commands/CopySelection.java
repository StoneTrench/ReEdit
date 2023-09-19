package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.BlocksClipboard;
import me.stonetrench.reworldedit.WEBlockData;
import me.stonetrench.reworldedit.WEVector3I;
import me.stonetrench.reworldedit.WorldEditHelper;

import java.util.ArrayList;

public class CopySelection extends CommandCompat {

    public CopySelection() {
        super("/copy");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }
        NetworkPlayer.NetworkPlayerController networkPlayerController = commandExecutor.getNetworkPlayerController();
        if (!networkPlayerController.hasSelection()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.no-area-selected"));
            return;
        }

        WEVector3I playerPos = new WEVector3I(commandExecutor.getRegisteredX(), commandExecutor.getRegisteredY(), commandExecutor.getRegisteredZ());
        final RegisteredWorld world = commandExecutor.getCurrentRegisteredWorld();

        ArrayList<WEBlockData> blocks = WorldEditHelper.ScanVolume(networkPlayerController, world);

        BlocksClipboard.SetClipboard(commandExecutor, blocks, playerPos);

        commandExecutor.displayChatMessage(
                gameRegistry.translateKeyFormat("reworldedit.command.copy.completed", blocks.size() + ""));
    }
}
