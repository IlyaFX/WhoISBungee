package ru.ilyafx.whois;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by User on 17.02.2018.
 */
public class Database {

    private static Connection connection;
    private static Statement statement;

    public Database(String host, String base, String user, String password){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + base, user, password);

            update("CREATE TABLE IF NOT EXISTS `whoisdata` " +
                    "(`player` varchar(50) NOT NULL,`playedtime` long NOT NULL,`lastjoin` long NOT NULL,`server` varchar(50) NOT NULL,`ip` varchar(50) NOT NULL) " +
                    "ENGINE=InnoDB DEFAULT CHARSET=utf8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public WhoISPlayer load(ProxiedPlayer player){
        ResultSet set = query("SELECT * FROM `whoisdata` WHERE `player` = '" + player.getName() + "';");
        try{
            set.previous();
            if(set.next()){
                return new WhoISPlayer(
                        player.getName(),
                        new WhoISTime(set.getLong("playedtime")),
                        new WhoISTime(System.currentTimeMillis()),
                        "lobby",
                        player.getAddress().getAddress().getHostAddress()
                );
            }else{
                update("INSERT INTO `whoisdata` (`player`,`playedtime`,`lastjoin`,`server`,`ip`) VALUES ('" + player.getName() + "',0," + System.currentTimeMillis() + ",'lobby','" + player.getAddress().getAddress().getHostAddress() + "');");
                return new WhoISPlayer(player.getName(),new WhoISTime(0),new WhoISTime(System.currentTimeMillis()),"lobby",player.getAddress().getAddress().getHostAddress());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet query(String query) {
        try {
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            set.next();
            return set;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void update(String... lines){
        try {
            for(String string : lines){
                statement = connection.createStatement();
                statement.executeUpdate(string);
                continue;
            }
            return;
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

}
