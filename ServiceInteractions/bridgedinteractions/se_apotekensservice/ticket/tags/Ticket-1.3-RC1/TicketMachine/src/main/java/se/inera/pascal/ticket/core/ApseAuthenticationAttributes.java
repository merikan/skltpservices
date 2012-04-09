package se.inera.pascal.ticket.core;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ApseAuthenticationAttributes {
	private String directoryID;
	private String organisationID;

	public ApseAuthenticationAttributes() {
	}

	public void setDirectoryID(String directoryID) {
		this.directoryID = directoryID;
	}

	public String getDirectoryID() {
		return directoryID;
	}

	public void setOrganisationID(String organisationID) {
		this.organisationID = organisationID;
	}

	public String getOrganisationID() {
		return organisationID;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
