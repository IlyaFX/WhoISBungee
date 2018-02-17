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

        boolean needAddHours = true; // Type true if time on server car if lowest than your time zone
        long var = time;
        if(needAddHours){
            int needHoursToAdd = 3;
            var+=((3600*needHoursToAdd)*1000);
        }

        return formating.format(var);
    }

    public String getString(){
        return DurationFormatUtils.formatDurationWords(time,true,true);
    }

    public String getWTFDate(){
        return DurationFormatUtils.formatDurationWords(time,true,true);
    }

}
