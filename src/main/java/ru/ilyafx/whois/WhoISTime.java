package ru.ilyafx.whois;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.SimpleDateFormat;

@AllArgsConstructor
@Getter
@Setter
public class WhoISTime {

    private long time;

    public String getDate(){
        SimpleDateFormat formating = new SimpleDateFormat("YYYY:MM:dd HH:mm:ss");
        return formating.format((time+(10800*1000)));
    }

    public String getString(){
        return DurationFormatUtils.formatDurationWords(time,true,true);
    }

    public String getWTFDate(){
        return DurationFormatUtils.formatDurationWords(time,true,true);
    }

}
