package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.BlockSetHistory;

public class UndoCommand extends CommandCompat {
    public UndoCommand() {
        super("/undo");
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
                if (BlockSetHistory.UndoPreviousChanges(world)) counter++;
                else break;
            }
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKeyFormat("reworldedit.command.undo.completed.multiple", counter + ""));
        } else {
            if (BlockSetHistory.UndoPreviousChanges(world))
                commandExecutor.displayChatMessage(
                        gameRegistry.translateKey("reworldedit.command.undo.completed"));
            else
                commandExecutor.displayChatMessage(
                        gameRegistry.translateKey("reworldedit.command.undo.failed"));
        }
    }
}
