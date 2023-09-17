package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.*;

import java.util.ArrayList;

public class PasteSelection extends CommandCompat {

    public PasteSelection() {
        super("/paste");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }

        WEVector3I playerPos = new WEVector3I(commandExecutor.getRegisteredX(), commandExecutor.getRegisteredY(), commandExecutor.getRegisteredZ());
        final RegisteredWorld world = commandExecutor.getCurrentRegisteredWorld();

        ArrayList<WEBlockData> blocks = BlocksClipboard.GetClipboard(playerPos);

        BlockSetHistory.ApplyBlockChanges(blocks, world);

        commandExecutor.displayChatMessage(
                gameRegistry.translateKeyFormat("reworldedit.command.paste.completed", blocks.size() + ""));
    }
}
