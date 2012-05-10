package se.skl.components.pull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.RegisteredResidentEngagementType;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Author: Henrik Rostam
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyResolver.class)
public class EngagementIndexPullTest {

    @Mock
    private GetLogicalAddresseesByServiceContractResponderInterface getAddressesClient;

    @Mock
    private GetUpdatesResponderInterface getUpdatesClient;

    @Mock
    private UpdateResponderInterface updateClient;

    @InjectMocks
    private EngagementIndexPull engagementIndexPull = new EngagementIndexPull();

    @Before
    public void initTests() {
        mockStatic(PropertyResolver.class);

        final String dateFormat = "yyyyMMddHHmmss";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        final String serviceDomain = "mockito:test:namespace";
        final String consumerHsaId = "Mock170Test1D";
        final String pushAddress = "Mock170T35t4ddr3ss";
        final String serviceDomain1 = "Mockito";
        final String serviceDomain2 = "Testing";
        final String serviceDomain3 = "Rocks";
        final String serviceDomainList = serviceDomain1 + ", " + serviceDomain2 + ", " + serviceDomain3;
        final String timeOffset = "-123";
        final String engagementBookingId = "bookingId";
        final String engagementCategorization = "Booking";
        final Date dateNow = new Date();
        final String engagementOwner = "HSA-id";
        final String engagementLogicalAddress = "Landstingets hsaid:Vårdgivarens HSA-id:Enhetens hsaid";
        final String engagementSourceSystem = "Systemets HSA-ID";
        final String patientSsn = "012345678901";
        final List<String> testAddresses = new LinkedList<String>();
        testAddresses.add(serviceDomain1);
        testAddresses.add(serviceDomain2);
        testAddresses.add(serviceDomain3);

        when(PropertyResolver.get(eq("ei.push.service.contract.namespace"))).thenReturn(serviceDomain);
        when(PropertyResolver.get(eq("ei.push.service.consumer.hsaid"))).thenReturn(consumerHsaId);
        when(PropertyResolver.get(eq("ei.push.address"))).thenReturn(pushAddress);
        when(PropertyResolver.get(eq("ei.push.service.domain.list"))).thenReturn(serviceDomainList);
        when(PropertyResolver.get(eq("ei.push.time.offset"))).thenReturn(timeOffset);

        EngagementType engagement = new EngagementType();
        engagement.setBusinessObjectInstanceIdentifier(engagementBookingId);
        engagement.setCategorization(engagementCategorization);
        engagement.setClinicalProcessInterestId(UUID.randomUUID().toString());
        engagement.setCreationTime(simpleDateFormat.format(dateNow));
        engagement.setLogicalAddress(engagementLogicalAddress);
        engagement.setMostRecentContent(simpleDateFormat.format(dateNow));
        engagement.setOwner(engagementOwner);
        engagement.setRegisteredResidentIdentification(patientSsn);
        engagement.setServiceDomain(serviceDomain);
        engagement.setSourceSystem(engagementSourceSystem);
        engagement.setUpdateTime(simpleDateFormat.format(dateNow));

        RegisteredResidentEngagementType registeredResidentEngagementType = new RegisteredResidentEngagementType();
        registeredResidentEngagementType.setRegisteredResidentIdentification(patientSsn);
        registeredResidentEngagementType.getEngagement().add(engagement);

        GetUpdatesResponseType getUpdatesResponseType = new GetUpdatesResponseType();
        getUpdatesResponseType.getRegisteredResidentEngagement().add(registeredResidentEngagementType);
        getUpdatesResponseType.setResponseIsComplete(true);

        GetLogicalAddresseesByServiceContractResponseType addressResponse = new GetLogicalAddresseesByServiceContractResponseType();
        addressResponse.getLogicalAddress().addAll(testAddresses);

        when(getUpdatesClient.getUpdates(anyString(), any(GetUpdatesType.class))).thenReturn(getUpdatesResponseType);
        when(getAddressesClient.getLogicalAddresseesByServiceContract(anyString(), any(GetLogicalAddresseesByServiceContractType.class))).thenReturn(addressResponse);
    }

    @Test
    public void testOneAddressFetchCall() {
        engagementIndexPull.doFetchUpdates();
        verify(getAddressesClient, times(1)).getLogicalAddresseesByServiceContract(anyString(), any(GetLogicalAddresseesByServiceContractType.class));
    }

    @After
    public void postTests() {

    }

}
