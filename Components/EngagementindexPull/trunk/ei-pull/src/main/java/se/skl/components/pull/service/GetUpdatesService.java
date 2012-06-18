package se.skl.components.pull.service;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.skl.components.pull.domain.GetUpdatesStatus;
import se.skl.components.pull.repository.GetUpdatesStatusRepository;
import se.skl.components.pull.utils.DateHelper;
import se.skl.components.pull.utils.EngagementIndexHelper;
import se.skl.components.pull.utils.PropertyResolver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: Henrik Rostam
 */

@Service("getUpdatesService")
public class GetUpdatesService {

    // DEBUG
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetUpdatesService.class);
    // END DEBUG

    @Autowired
    private GetUpdatesStatusRepository getUpdatesStatusRepository;

    public List<GetUpdatesStatus> fetchAll() {
        return getUpdatesStatusRepository.fetchAll();
    }

    public String getFormattedDateForGetUpdates(String logicalAddress, String serviceDomain, String timestampFormat) {
        GetUpdatesStatus status = getUpdatesStatusRepository.getStatusForLogicalAddressAndServiceContract(logicalAddress, serviceDomain);
        if (status == null || status.getLastSuccess() == null) {
            // No recorded date for last success existed, let's default to property offset.
            String timeOffset = PropertyResolver.get("ei.pull.time.offset");
            return EngagementIndexHelper.getFormattedOffsetTime(DateHelper.now(), timeOffset, timestampFormat);
        }
        int secondsToRemove = (-(NumberUtils.toInt(PropertyResolver.get("ei.pull.time.margin"))));
        Date returnDate = getPastTimeInSeconds(status.getLastSuccess(), secondsToRemove);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timestampFormat);
        return simpleDateFormat.format(returnDate);
    }

    private synchronized Date getPastTimeInSeconds(Date date, int secondsToRemove) {
        // Remove one minute from last success time to make sure in case updates were made just when the update was made.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, secondsToRemove);
        return calendar.getTime();
    }

    public void updateDateForGetUpdates(String logicalAddress, String serviceDomain, Date timeOfFetch) {
        GetUpdatesStatus status = getUpdatesStatusRepository.getStatusForLogicalAddressAndServiceContract(logicalAddress, serviceDomain);
        // DEBUG
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsText = simpleDateFormat.format(timeOfFetch);
        log.debug("\n\n\nTrying to set the new data: " + dateAsText + " for logical address: " + logicalAddress + ", using service domain: " + serviceDomain + "!\n\n\n");
        // END DEBUG
        if (status == null) {
            // Let's create one
            status = new GetUpdatesStatus();
            status.setLogicalAddress(logicalAddress);
            status.setServiceDomain(serviceDomain);
            status.setLastSuccess(timeOfFetch);
            status.setAmountOfErrorsSinceLastSuccess(0);
            getUpdatesStatusRepository.save(status);
        } else {
            // Just update time
            status.setLastSuccess(timeOfFetch);
            status.setAmountOfErrorsSinceLastSuccess(0);
            getUpdatesStatusRepository.update(status);
        }
    }

    public void incrementErrorsSinceLastFetch(String logicalAddress, String serviceDomain) {
        GetUpdatesStatus status = getUpdatesStatusRepository.getStatusForLogicalAddressAndServiceContract(logicalAddress, serviceDomain);
        if (status == null) {
            // Let's create one
            status = new GetUpdatesStatus();
            status.setLogicalAddress(logicalAddress);
            status.setServiceDomain(serviceDomain);
            status.setLastSuccess(null);
            status.setAmountOfErrorsSinceLastSuccess(1);
            getUpdatesStatusRepository.save(status);
        } else {
            // Just increase errors by one
            int amountOfErrorsSinceLastSuccess = status.getAmountOfErrorsSinceLastSuccess() + 1;
            status.setAmountOfErrorsSinceLastSuccess(amountOfErrorsSinceLastSuccess);
            getUpdatesStatusRepository.update(status);
        }
    }

}
