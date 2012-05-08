package se.skl.components.pull;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Henrik Rostam
 */
public class EngagementIndexHelper {

    protected final static List<String> getServiceDomainList(String commaSeparatedDomains) {
        List<String> serviceDomainList = new LinkedList<String>();
        String[] stringDomainList = commaSeparatedDomains.split(",");
        for (String serviceDomain : stringDomainList) {
            serviceDomainList.add(StringUtils.trim(serviceDomain));
        }
        return serviceDomainList;
    }

    protected final static String getFormattedOffsetTime(int offsetFromNowInSeconds, String dateFormat) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.set(Calendar.SECOND, offsetFromNowInSeconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(calendar.getTime());
    }

}
