/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
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
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.exception;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;

/**
 * Transformer to handle the case when we get a soap fault from the producer and
 * just wants to redirect it back to the consumer. If message contains a
 * ExceptionPayload, this is the one that will be returned to the consumer. And
 * in this case the ExceptionPayload is empty and the Payload contains the
 * correct fault to be returned.
 * 
 * Note that tests for this transformer is performed in
 * HamtaAktuellaOrdinationer.
 * 
 */
public class IgnoreAnyEcxeptionPayloadsTransformer extends AbstractMessageAwareTransformer {

    @Override
    public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
	if (containsBothPayloadAndExceptionPayload(message)) {
	    removeExceptionPayload(message);
	}
	return message;
    }

    private void removeExceptionPayload(MuleMessage message) {
	logger.debug("Exception payload detected together with payload! Exception payload will be removed to return payload only");
	message.setExceptionPayload(null);
    }

    private boolean containsBothPayloadAndExceptionPayload(MuleMessage message) {
	return message.getPayload() != null && message.getExceptionPayload() != null;
    }
}
