package me.stonetrench.reworldedit.commands;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.RegisteredWorld;
import me.stonetrench.reworldedit.BlockSetHistory;
import me.stonetrench.reworldedit.WEBlockData;
import me.stonetrench.reworldedit.WEVector3I;
import me.stonetrench.reworldedit.WorldEditHelper;
import net.minecraft.src.game.entity.Entity;

import java.util.ArrayList;

public class StackSelection extends CommandCompat {


    public StackSelection() {
        super("/stack");
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        if (args.length < 2) {
            commandExecutor.displayChatMessage(gameRegistry.translateKey("reworldedit.command.error.args"));
            return;
        }
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

        final WEVector3I[] minMax = WorldEditHelper.ConvertToVectors(networkPlayerController);
        final WEVector3I size = minMax[1].Clone().Sub(minMax[0]).Offset(1, 1, 1);

        final RegisteredWorld world = commandExecutor.getCurrentRegisteredWorld();
        final ArrayList<WEBlockData> blocks = WorldEditHelper.ScanVolume(networkPlayerController, world);

        int count = Integer.parseInt(args[1]);

        String cardinalArg;
        WEVector3I cardinalDirection;
        if (args.length == 3)
            cardinalArg = args[2];
        else
            cardinalArg = WorldEditHelper.CardinalDirectionCalculator(((Entity)commandExecutor).rotationYaw, ((Entity)commandExecutor).rotationPitch);

        cardinalDirection = WorldEditHelper.GetCardinalVector(cardinalArg);
        if (cardinalDirection == null) {
            commandExecutor.displayChatMessage(gameRegistry.translateKey("reworldedit.command.error.args"));
            return;
        }

        WEVector3I offsetDirection = cardinalDirection.Clone().Mult(size);
        ArrayList<WEBlockData> allBlocks = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            for (WEBlockData block : blocks) {
                allBlocks.add(new WEBlockData(block.position.Add(offsetDirection).Clone(), block.Id, block.Meta));
            }
        }

        BlockSetHistory.ApplyBlockChanges(allBlocks, world);

        commandExecutor.displayChatMessage(
                gameRegistry.translateKeyFormat("reworldedit.command.stack.completed", (blocks.size() * count) + "", cardinalDirection + ""));
    }
}
