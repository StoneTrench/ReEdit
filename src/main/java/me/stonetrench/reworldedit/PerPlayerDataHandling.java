package me.stonetrench.reworldedit;

import com.fox2code.foxloader.network.NetworkPlayer;

import java.util.HashMap;

public class PerPlayerDataHandling<T> {
    private final HashMap<String, T> PlayerData = new HashMap<>();

    public void SetPlayerData(NetworkPlayer player, T data){
        PlayerData.put(player.getPlayerName(), data);
    }
    public T GetPlayerData(NetworkPlayer player, T defaultReturn){
        T value = PlayerData.get(player.getPlayerName());
        if (value == null) {
            SetPlayerData(player, defaultReturn);
            return defaultReturn;
        }
        return value;
    }
}
