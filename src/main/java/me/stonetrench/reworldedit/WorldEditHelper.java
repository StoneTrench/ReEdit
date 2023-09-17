package me.stonetrench.reworldedit;

import com.fox2code.foxloader.client.network.ImplNetworkPlayerControllerExt;
import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.RegisteredWorld;
import net.minecraft.src.game.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WorldEditHelper {

    private static Map<String, WEVector3I> Cardinals = null;
    public static void Initialize(){
        if (Cardinals == null) {
            Cardinals = new HashMap<>();

            Cardinals.put("south", new WEVector3I(0, 0, 1));
            Cardinals.put("north", new WEVector3I(0, 0, -1));
            Cardinals.put("east", new WEVector3I(1, 0, 0));
            Cardinals.put("west", new WEVector3I(-1, 0, 0));
            Cardinals.put("up", new WEVector3I(0, 1, 0));
            Cardinals.put("down", new WEVector3I(0, -1, 0));

            Cardinals.put("s", new WEVector3I(0, 0, 1));
            Cardinals.put("n", new WEVector3I(0, 0, -1));
            Cardinals.put("e", new WEVector3I(1, 0, 0));
            Cardinals.put("w", new WEVector3I(-1, 0, 0));
            Cardinals.put("u", new WEVector3I(0, 1, 0));
            Cardinals.put("d", new WEVector3I(0, -1, 0));
        }
    }

    public static WEVector3I GetCardinalVector(String name){
        if (!Cardinals.containsKey(name)) return null;
        return Cardinals.get(name).Clone();
    }

    public static WEVector3I[] ConvertToVectors(NetworkPlayer.NetworkPlayerController networkPlayerController){
        final int MinX = networkPlayerController.getMinX();
        final int MinY = networkPlayerController.getMinY();
        final int MinZ = networkPlayerController.getMinZ();
        final int MaxX = networkPlayerController.getMaxX();
        final int MaxY = networkPlayerController.getMaxY();
        final int MaxZ = networkPlayerController.getMaxZ();

        return new WEVector3I[] {
            new WEVector3I(MinX, MinY, MinZ),
            new WEVector3I(MaxX, MaxY, MaxZ)
        };
    }

    /**
     * If the operator returns true, the iteration will end and return the position it ended on.
     */
    public static WEVector3I OperateOverVolume(WEVector3I Min, WEVector3I Max, Function<WEVector3I, Boolean> operation){
        for (int x = Min.X; x <= Max.X; x++) {
            for (int y = Min.Y; y <= Max.Y; y++) {
                for (int z = Min.Z; z <= Max.Z; z++) {
                    WEVector3I pos = new WEVector3I(x, y, z);
                    if (operation.apply(pos)) return pos;
                }
            }
        }

        return null;
    }

    /**
     * If the operator returns true, the iteration will end and return the position it ended on.
     */
    public static WEVector3I OperateOverVolume(NetworkPlayer.NetworkPlayerController networkPlayerController, Function<WEVector3I, Boolean> operation){
        WEVector3I[] array = ConvertToVectors(networkPlayerController);
        return OperateOverVolume(array[0], array[1], operation);
    }

    /**
     * @return idSource, metaSource
     */
    public static int[] ParseBlockId(String arg){
        int i = arg.indexOf(':');
        int idSource, metaSource;
        if (i == -1) {
            idSource = Integer.parseInt(arg);
            metaSource = -1;
        } else {
            idSource = Integer.parseInt(arg.substring(0, i));
            metaSource = Integer.parseInt(arg.substring(i + 1));
        }
        return new int[] {idSource, metaSource};
    }


    /**
     * @return idSource, metaSource
     */
    public static WEVector3F ParseVector(String[] args, int startFrom){
        float[] values = new float[3];

        for (int i = 0; i < 3; i++){
            int index = i + startFrom;
            if (index >= args.length) return null;
            if (args[index] == null) return null;
            try {
                values[i] = Float.parseFloat(args[index]);
            }
            catch (Exception e){
                return null;
            }
        }

        return new WEVector3F(values[0], values[1], values[2]);
    }

    public static void SetSelectionPosition(String[] args, NetworkPlayer commandExecutor, boolean secondary){
        ImplNetworkPlayerControllerExt playerController = ((ImplNetworkPlayerControllerExt) commandExecutor.getNetworkPlayerController());

        WEVector3I playerPos = new WEVector3I(commandExecutor.getRegisteredX(), commandExecutor.getRegisteredY(), commandExecutor.getRegisteredZ());

        String posName = secondary ? "Pos2:" : "Pos1:";
        if (secondary)
            playerController.notifyRegisteredSetSecondary(playerPos.X, playerPos.Y, playerPos.Z);
        else
            playerController.notifyRegisteredSetPrimary(playerPos.X, playerPos.Y, playerPos.Z);
        commandExecutor.displayChatMessage(
                posName + " " + playerPos);
    }

    public static WEBlockData GetBlockAt(WEVector3I position, RegisteredWorld world){
        return new WEBlockData(position, world.getRegisteredBlockId(position.X, position.Y, position.Z), world.getRegisteredBlockMetadata(position.X, position.Y, position.Z));
    }

    public static ArrayList<WEBlockData> ScanVolume(NetworkPlayer.NetworkPlayerController networkPlayerController, RegisteredWorld world){
        ArrayList<WEBlockData> result = new ArrayList<>();

        WorldEditHelper.OperateOverVolume(networkPlayerController, (pos) -> {
            result.add(GetBlockAt(pos, world));
            return false;
        });

        return result;
    }

    public static String CardinalDirectionCalculator(float yaw, float pitch){
        // -90 up
        // 0 horizontal
        // +90 down

        pitch %= 360.0F;

        if (pitch >= 45F) return "down";
        if (pitch <= -45F) return "up";

        switch(MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5) & 3) {
            case 1:
                return "west";
            case 2:
                return "north";
            case 3:
                return "east";
            default:
                return "south";
        }
    }
}
