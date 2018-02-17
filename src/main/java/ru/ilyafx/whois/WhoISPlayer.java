package ru.ilyafx.whois;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WhoISPlayer {

    private String player;
    private WhoISTime playedTime;
    private WhoISTime lastTimePlayed;
    private String lastServer;
    private String lastIp;

}
