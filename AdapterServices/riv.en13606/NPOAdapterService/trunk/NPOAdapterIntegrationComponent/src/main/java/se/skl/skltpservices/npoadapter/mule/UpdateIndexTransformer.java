/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.npoadapter.mule;

import lombok.extern.slf4j.Slf4j;
import org.mule.api.MuleMessage;
import org.mule.transformer.AbstractMessageTransformer;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import riv.itintegration.engagementindex._1.EngagementTransactionType;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex.updateresponder._1.ObjectFactory;
import riv.itintegration.engagementindex.updateresponder._1.UpdateType;
import se.nationellpatientoversikt.*;

import java.util.List;

/**
 * Created by Peter on 2014-08-19.
 */
@Slf4j
public class UpdateIndexTransformer extends AbstractMessageTransformer {

    private static final ObjectFactory of = new ObjectFactory();
    private static final JaxbUtil jaxbUtil = new JaxbUtil(UpdateType.class);

    private String eiLogicalAddress;

    public String getEiLogicalAddress() {
        return eiLogicalAddress;
    }

    public void setEiLogicalAddress(String eiLogicalAddress) {
        this.eiLogicalAddress = eiLogicalAddress;
    }

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) {

        final Object[] objArr = (Object[])message.getPayload();

        UpdateType updateType = null;

        if (objArr[1] instanceof ArrayOfinfoTypeInfoTypeType) {
            log.debug("SimpleIndex to Update");
            final SendSimpleIndex simpleIndex = new SendSimpleIndex();
            simpleIndex.setSubjectOfCareId((String) objArr[0]);
            simpleIndex.setInfoTypes((ArrayOfinfoTypeInfoTypeType) objArr[1]);
            simpleIndex.setParameters((ArrayOfparameternpoParameterType) objArr[2]);
            updateType = map(simpleIndex, message);
        } else if (objArr[1] instanceof ArrayOfindexUpdateIndexUpdateType) {
            log.debug("SendIndex2 to Update");
            final SendIndex2 sendIndex2 = new SendIndex2();
            sendIndex2.setSubjectOfCareId((String) objArr[0]);
            sendIndex2.setIndexUpdates((ArrayOfindexUpdateIndexUpdateType) objArr[1]);
            sendIndex2.setParameters((ArrayOfparameternpoParameterType) objArr[2]);
            updateType = map(sendIndex2, message);
        } else {
            throw new IllegalStateException("Unexpected type of message: " + objArr[1]);
        }

        final Object[] payload = { getEiLogicalAddress(), updateType };

        message.setPayload(payload);

        return message;
    }

    //
    protected UpdateType map(final SendSimpleIndex simpleIndex, final MuleMessage message) {
        final UpdateType update = of.createUpdateType();
        for (final InfoTypeType info : simpleIndex.getInfoTypes().getInfoType()) {
            final EngagementType engagement = domain(
                    create(simpleIndex.getSubjectOfCareId(), simpleIndex.getParameters().getParameter(), message),
                    info.getInfoTypeId());
            final EngagementTransactionType engagementTransaction = create(!info.isExists(), engagement);
            engagementTransaction.setEngagement(domain(engagement, info.getInfoTypeId()));
            update.getEngagementTransaction().add(engagementTransaction);
        }
        return update;
    }

    //
    protected UpdateType map(final SendIndex2 sendIndex2, final MuleMessage message) {
        final UpdateType update = of.createUpdateType();
        for (final IndexUpdateType info : sendIndex2.getIndexUpdates().getIndexUpdate()) {
            final EngagementType engagement = domain(
                    create(sendIndex2.getSubjectOfCareId(), sendIndex2.getParameters().getParameter(), message),
                    info.getInfoTypeId());
            engagement.setOwner(info.getCareGiver());
            final EngagementTransactionType engagementTransaction = create(false, engagement);
            update.getEngagementTransaction().add(engagementTransaction);
        }
        return update;
    }

    //
    protected EngagementTransactionType create(final boolean deleteFlag, final EngagementType engagement) {
        final EngagementTransactionType engagementTransaction = new EngagementTransactionType();
        engagementTransaction.setDeleteFlag(deleteFlag);
        engagementTransaction.setEngagement(engagement);
        return engagementTransaction;
    }

    //
    protected EngagementType domain(final EngagementType engagement, final String infoType) {
        switch (infoType) {
            case "vko":
                engagement.setServiceDomain("riv:clinicalprocess:logistics:logistics");
                break;
            case "voo":
                engagement.setServiceDomain("riv:clinicalprocess:healthcond:description");
                break;
        }
        return engagement;
    }

    //
    protected EngagementType create(final String personId, List<NpoParameterType> parameters, final MuleMessage message) {
        final EngagementType engagement = new EngagementType();

        engagement.setRegisteredResidentIdentification(personId);

        SendIndexTransformer.setParameters(message, parameters);

        final String hsaId = message.getOutboundProperty(SendIndexTransformer.NPO_PARAM_PREFIX + "hsa_id");

        engagement.setSourceSystem(hsaId);
        engagement.setDataController(hsaId);
        engagement.setOwner(hsaId);

        return engagement;
    }
}
