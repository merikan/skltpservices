package se.skl.skltpservices.takecare;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

public class TakeCareUtil {

	public static final String INVOKING_SYSTEM = "InvSysMVK";
	public static final String EXTERNAL_USER = "ExtUsrMVK";
	public static final String HSAID = "hsaid";
	public static final String REQUEST = "Request";
	public static final String RESPONSE = "Response";
	public static final String WEB = "Web";

	public static final BigInteger numericToBigInteger(String value) {
		if (StringUtils.isNumeric(value)) {
			return new BigInteger(value);
		}
		return null;
	}

	public static final int numericToInteger(String value) {
		if (StringUtils.isNumeric(value)) {
			return new Integer(value);
		}
		return 0;
	}

}
