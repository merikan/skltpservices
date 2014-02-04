package se.skltp.adapterservices.druglogistics.dosdispensing;

public class SoapFaultInPayload extends RuntimeException {

	public SoapFaultInPayload() {
		super();
	}

	public SoapFaultInPayload(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SoapFaultInPayload(String message, Throwable cause) {
		super(message, cause);
	}

	public SoapFaultInPayload(String message) {
		super(message);
	}

	public SoapFaultInPayload(Throwable cause) {
		super(cause);
	}

	private String soapFault = null;
	public void setSoapFault(String soapFault) {
		this.soapFault = soapFault;
	}
	public String getSoapFault() {
		return soapFault;
	}
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9093275941853317643L;

}
