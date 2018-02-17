package ru.ilyafx.whois.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.time.DurationFormatUtils;
import ru.ilyafx.whois.WhoIS;
import ru.ilyafx.whois.WhoISPlayer;
import ru.ilyafx.whois.handlers.JoinQuitHandlers;

/**
 * Created by User on 17.02.2018.
 */
public class WhoISCommand extends Command {

    public WhoISCommand(){
        super("whois","atlant.whois");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(getComp("&cНедостаточно аргументов"));
            return;
        }
        String player = args[0];
        if(!WhoIS.players.containsKey(player)){
            sender.sendMessage(getComp("&cТакого игрока нет!"));
            return;
        }
        WhoISPlayer pl = WhoIS.players.get(player);

        long var1 = pl.getPlayedTime().getTime();
        String playedTime = "";
        if(JoinQuitHandlers.time.containsKey(pl.getPlayer())){
            var1+=(System.currentTimeMillis()-JoinQuitHandlers.time.get(pl.getPlayer()));
        }
        playedTime = DurationFormatUtils.formatDurationWords(var1,true,true);

        sender.sendMessage(getComp(
                "     ",
                        "&eИгрок " + player,
                        "      ",
                        "&eВ последний раз заходил &a" + pl.getLastTimePlayed().getDate(),
                        "&eВсего наиграл &a" + playedTime,
                        "&eПоследний сервер: &a" + pl.getLastServer(),
                        "&eПоследний IP: &a" + pl.getLastIp(),
                        "        "
        ));
    }

    private TextComponent[] getComp(String... lines){
        TextComponent[] mas = new TextComponent[lines.length];
        for(int d = 0; d<lines.length; d++) mas[d] = new TextComponent(ChatColor.translateAlternateColorCodes('&',lines[d]) + "\n");
        return mas;
    }
}
