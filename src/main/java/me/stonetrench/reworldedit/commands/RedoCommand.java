package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.BlockSetHistory;

public class RedoCommand extends CommandCompat {
    public RedoCommand() {
        super("/redo");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }
        final RegisteredWorld world = commandExecutor.getCurrentRegisteredWorld();
        if (args.length == 2) {
            int counter = 0;
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                if (BlockSetHistory.RedoPreviousChanges(commandExecutor, world)) counter++;
                else break;
            }
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKeyFormat("reworldedit.command.redo.completed.multiple", counter + ""));
        } else {
            if (BlockSetHistory.RedoPreviousChanges(commandExecutor, world))
                commandExecutor.displayChatMessage(
                        gameRegistry.translateKey("reworldedit.command.redo.completed"));
            else
                commandExecutor.displayChatMessage(
                        gameRegistry.translateKey("reworldedit.command.redo.failed"));
        }
    }
}
