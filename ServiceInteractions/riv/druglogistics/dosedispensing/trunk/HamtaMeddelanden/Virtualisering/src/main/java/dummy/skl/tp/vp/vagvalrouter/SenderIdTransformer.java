/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package dummy.skl.tp.vp.vagvalrouter;

import java.util.regex.Pattern;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dummy.skl.tp.vp.util.VPUtil;


public class SenderIdTransformer extends AbstractMessageTransformer {

	private static final Logger logger = LoggerFactory.getLogger(SenderIdTransformer.class);

	private Pattern pattern;

	private String whiteList;

	public void setWhiteList(final String whiteList) {
		this.whiteList = whiteList;
	}

	public void setSenderIdPropertyName(String senderIdPropertyName) {
		pattern = Pattern.compile(senderIdPropertyName + "=([^,]+)");
		if (logger.isInfoEnabled()) {
			logger.info("senderIdPropertyName set to: " + senderIdPropertyName);
		}
	}

	@Override
	public Object transformMessage(MuleMessage msg, String encoding) throws TransformerException {

		String senderId = VPUtil.getSenderId(msg, whiteList, pattern);
		msg.setOutboundProperty(VagvalRouter.X_VP_CONSUMER_ID, senderId);
		return msg;
	}

}
