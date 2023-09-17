package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import me.stonetrench.reworldedit.WorldEditHelper;

public class SelectPosition1 extends CommandCompat {

    public SelectPosition1() {
        super("/pos1");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }

        WorldEditHelper.SetSelectionPosition(args, commandExecutor, false);
    }
}
