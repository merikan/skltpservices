package se.skl.skltpservices.takecare;

import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

public final class TakeCareDateHelper {

	public static final BigInteger yyyyMMddHHmmss(Date date) {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
		return new BigInteger(dateFormat.format(date));
	}

	public static final BigInteger yyyyMMddHHmm(Date date) {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmm");
		return new BigInteger(dateFormat.format(date));
	}

	/**
	 * RIV TA is YYYYMMDDttmmss, Take Care YYYYMMDDttmm
	 * 
	 * @return
	 */
	public static final String removeSecondsFromRivTaTime(String yyyyMMddHHmmss) {
		if (StringUtils.isEmpty(yyyyMMddHHmmss) || yyyyMMddHHmmss.length() != 14) {
			throw new RuntimeException("Time is not in correct RIV TA format, expecting yyyyMMddHHmmss");
		}
		return yyyyMMddHHmmss.substring(0, 12);
	}

}
