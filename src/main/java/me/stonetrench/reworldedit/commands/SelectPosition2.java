package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import me.stonetrench.reworldedit.WorldEditHelper;

public class SelectPosition2 extends CommandCompat {

    public SelectPosition2() {
        super("/pos2");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (!commandExecutor.getNetworkPlayerController().hasCreativeModeRegistered()) {
            commandExecutor.displayChatMessage(
                    gameRegistry.translateKey("command.error.creative-only"));
            return;
        }

        WorldEditHelper.SetSelectionPosition(args, commandExecutor, true);
    }
}
