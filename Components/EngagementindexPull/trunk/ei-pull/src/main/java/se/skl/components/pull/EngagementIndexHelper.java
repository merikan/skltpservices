package se.skl.components.pull;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Henrik Rostam
 */
public class EngagementIndexHelper {

    protected final static List<String> stringToList(String commaSeparatedDomains) {
        if (StringUtils.isBlank(commaSeparatedDomains)) {
            return null;
        }
        List<String> serviceDomainList = new LinkedList<String>();
        String[] stringDomainList = commaSeparatedDomains.split(",");
        for (String serviceDomain : stringDomainList) {
            serviceDomainList.add(StringUtils.trim(serviceDomain));
        }
        return serviceDomainList;
    }

    protected final static String getFormattedOffsetTime(Date startingDate, String timeOffset, String dateFormat) {
        int offsetFromNowInSeconds = -NumberUtils.toInt(timeOffset);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(startingDate);
        calendar.set(Calendar.SECOND, offsetFromNowInSeconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(calendar.getTime());
    }

}
