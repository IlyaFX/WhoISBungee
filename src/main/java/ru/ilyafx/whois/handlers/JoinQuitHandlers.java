package ru.ilyafx.whois.handlers;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.ilyafx.whois.WhoIS;
import ru.ilyafx.whois.WhoISPlayer;

import java.util.HashMap;

public class JoinQuitHandlers implements Listener {

    public static HashMap<String,Long> time = new HashMap<>();

    @EventHandler
    public void onJoin(PostLoginEvent event){
        try {
            WhoISPlayer player = WhoIS.getDatabase().load(event.getPlayer());
            time.put(event.getPlayer().getName(), System.currentTimeMillis());
            WhoIS.players.put(event.getPlayer().getName(), player);
        }catch(Exception e){
            return;
        }
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event){
        try {
            long tm = System.currentTimeMillis() - time.get(event.getPlayer().getName());
            WhoISPlayer player = WhoIS.players.get(event.getPlayer().getName());
            player.getPlayedTime().setTime(player.getPlayedTime().getTime() + tm);
            player.getLastTimePlayed().setTime(System.currentTimeMillis());
            player.setLastServer(event.getPlayer().getServer().getInfo().getName());
            WhoIS.saveData(player);
        }catch(Exception e){
            return;
        }
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent event){
        try {
            WhoISPlayer player = WhoIS.players.get(event.getPlayer().getName());
            player.setLastServer(event.getPlayer().getServer().getInfo().getName());
            WhoIS.saveData(player);
        }catch(Exception e){
            return;
        }
    }

}
