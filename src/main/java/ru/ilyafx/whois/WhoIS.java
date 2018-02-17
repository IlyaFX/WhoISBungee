package ru.ilyafx.whois;

import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.ilyafx.whois.commands.WhoISCommand;
import ru.ilyafx.whois.handlers.JoinQuitHandlers;

import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;

public class WhoIS extends Plugin {

    @Getter
    private static WhoIS instance;

    public static HashMap<String,WhoISPlayer> players = new HashMap<>();

    @Getter
    private static Configuration config;

    @Getter
    private static Database database;

    @Override
    public void onEnable() {
        instance = this;

        getProxy().getPluginManager().registerCommand(getInstance(),new WhoISCommand());
        getProxy().getPluginManager().registerListener(getInstance(),new JoinQuitHandlers());

        loadConfig();
        database = new Database(
                getConfig().getString("mysql.host"),
                getConfig().getString("mysql.base"),
                getConfig().getString("mysql.user"),
                getConfig().getString("mysql.pass")
        );
        BungeeCord.getInstance().getScheduler().runAsync(getInstance(),()->loadAllData());
    }

    public static void loadAllData(){
        ResultSet set = getDatabase().query("SELECT * FROM `whoisdata`;");
        try{
            set.previous();
            while(set.next()){
                players.put(
                        set.getString("player"),
                        new WhoISPlayer(
                                set.getString("player"),
                                new WhoISTime(set.getLong("playedtime")),
                                new WhoISTime(set.getLong("lastjoin")),
                                set.getString("server"),
                                set.getString("ip")
                                )
                );
            }
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    public static void loadConfig(){
        try
        {
            if (!getInstance().getDataFolder().exists()) {
                getInstance().getDataFolder().mkdir();
            }
            File file = new File(getInstance().getDataFolder(), "whois.yml");
            if (!file.exists()) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(ConfigurationProvider.getProvider(YamlConfiguration.class).load(getInstance().getResourceAsStream("whois.yml")), file);
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void saveData(WhoISPlayer player){
        BungeeCord.getInstance().getScheduler().runAsync(getInstance(),()->{
            ResultSet set = getDatabase().query("SELECT * FROM `whoisdata` WHERE `player` = '" + player.getPlayer() + "';");
            try{
                long var1 = set.getLong("playedtime");
                long var2 = set.getLong("lastjoin");
                String var3 = set.getString("server");
                String var4 = set.getString("ip");
                if(var1 != player.getPlayedTime().getTime()) getDatabase().update("UPDATE `whoisdata` SET `playedtime` = " + player.getPlayedTime().getTime() + " WHERE `player` = '" + player.getPlayer() + "';");
                if(var2 != player.getLastTimePlayed().getTime()) getDatabase().update("UPDATE `whoisdata` SET `lastjoin` = " + player.getLastTimePlayed().getTime() + " WHERE `player` = '" + player.getPlayer() + "';");
                if(!var3.equalsIgnoreCase(player.getLastServer())) getDatabase().update("UPDATE `whoisdata` SET `server` = '" + player.getLastServer() + "' WHERE `player` = '" + player.getPlayer() + "';");
                if(!var4.equalsIgnoreCase(player.getLastIp())) getDatabase().update("UPDATE `whoisdata` SET `ip` = '" + player.getLastIp() + "' WHERE `player` = '" + player.getPlayer() + "';");
            }catch(Exception e){
                return;
            }
        });
    }
}
