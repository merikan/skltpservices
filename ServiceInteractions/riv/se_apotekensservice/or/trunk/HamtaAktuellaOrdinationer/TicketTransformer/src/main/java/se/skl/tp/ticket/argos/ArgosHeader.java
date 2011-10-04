/**
 * Copyright 2011 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General 

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General  License for more details.
 *
 *   You should have received a copy of the GNU Lesser General 
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.skl.tp.ticket.argos;

/**
 * Class representing the Argos header information.
 */
public class ArgosHeader {

    private String forskrivarkod;
    private String legitimationskod;
    private String fornamn;
    private String efternamn;
    private String yrkesgrupp;
    private String befattningskod;
    private String arbetsplatskod;
    private String arbetsplatsnamn;
    private String postort;
    private String postadress;
    private String postnummer;
    private String telefonnummer;
    private String requestId;
    private String rollnamn;
    private String directoryID;
    private String hsaID;
    private String katalog;
    private String organisationsnummer;
    private String systemnamn;
    private String systemversion;
    private String systemIp;

    public ArgosHeader() {
    }

    public ArgosHeader(String forskrivarkod, String legitimationskod, String fornamn, String efternamn, String yrkesgrupp,
	    String befattningskod, String arbetsplatskod, String arbetsplatsnamn, String postort, String postadress,
	    String postnummer, String telefonnummer, String requestId, String rollnamn, String directoryID,
	    String hsaID, String katalog, String organisationsnummer, String systemnamn, String systemversion,
	    String systemIp) {

	this.forskrivarkod = forskrivarkod;
	this.legitimationskod = legitimationskod;
	this.fornamn = fornamn;
	this.efternamn = efternamn;
	this.yrkesgrupp = yrkesgrupp;
	this.befattningskod = befattningskod;
	this.arbetsplatskod = arbetsplatskod;
	this.arbetsplatsnamn = arbetsplatsnamn;
	this.postort = postort;
	this.postadress = postadress;
	this.postnummer = postnummer;
	this.telefonnummer = telefonnummer;
	this.requestId = requestId;
	this.rollnamn = rollnamn;
	this.directoryID = directoryID;
	this.hsaID = hsaID;
	this.katalog = katalog;
	this.organisationsnummer = organisationsnummer;
	this.systemnamn = systemnamn;
	this.systemversion = systemversion;
	this.systemIp = systemIp;
    }

    public   void setForskrivarkod(String forskrivarkod) {
	this.forskrivarkod = forskrivarkod;
    }

    public  void setLegitimationskod(String legitimationskod) {
	this.legitimationskod = legitimationskod;
    }

    public  void setFornamn(String fornamn) {
	this.fornamn = fornamn;
    }

    public  void setEfternamn(String efternamn) {
	this.efternamn = efternamn;
    }

    public  void setYrkesGrupp(String yrkesgrupp) {
	this.yrkesgrupp = yrkesgrupp;
    }

    public  void setBefattningskod(String befattningskod) {
	this.befattningskod = befattningskod;
    }

    public  void setArbetsplatskod(String arbetsplatskod) {
	this.arbetsplatskod = arbetsplatskod;
    }

    public  void setArbetsplatsnamn(String arbetsplatsnamn) {
	this.arbetsplatsnamn = arbetsplatsnamn;
    }

    public  void setPostort(String postort) {
	this.postort = postort;
    }

    public  void setPostadress(String postadress) {
	this.postadress = postadress;
    }

    public  void setPostnummer(String postnummer) {
	this.postnummer = postnummer;
    }

    public   void setTelefonnummer(String telefonnummer) {
	this.telefonnummer = telefonnummer;
    }

    public     void setRequestId(String requestId) {
	this.requestId = requestId;
    }

    public void setRollnamn(String rollnamn) {
	this.rollnamn = rollnamn;
    }

    public    void setDirectoryID(String directoryID) {
	this.directoryID = directoryID;
    }

    public  void setHsaID(String hsaID) {
	this.hsaID = hsaID;
    }

    public  void setKatalog(String katalog) {
	this.katalog = katalog;
    }

    public   void setOrganisationsnummer(String organisationsnummer) {
	this.organisationsnummer = organisationsnummer;
    }

    public  void setSystemnamn(String systemnamn) {
	this.systemnamn = systemnamn;
    }

    public void setSystemversion(String systemversion) {
	this.systemversion = systemversion;
    }

    public  void setSystemIp(String systemIp) {
	this.systemIp = systemIp;
    }

    public String getForskrivarkod() {
	return forskrivarkod;
    }

    public   String getLegitimationskod() {
	return legitimationskod;
    }

    public  String getFornamn() {
	return fornamn;
    }

    public String getEfternamn() {
	return efternamn;
    }

    public String getYrkesgrupp() {
	return yrkesgrupp;
    }

    public String getBefattningskod() {
	return befattningskod;
    }

    public  String getArbetsplatskod() {
	return arbetsplatskod;
    }

    public String getArbetsplatsnamn() {
	return arbetsplatsnamn;
    }

    public String getPostort() {
	return postort;
    }

    public  String getPostadress() {
	return postadress;
    }

    public String getPostnummer() {
	return postnummer;
    }

    public  String getTelefonnummer() {
	return telefonnummer;
    }

    public  String getRequestId() {
	return requestId;
    }

    public String getRollnamn() {
	return rollnamn;
    }

    public  String getDirectoryID() {
	return directoryID;
    }

    public String getHsaID() {
	return hsaID;
    }

    public  String getKatalog() {
	return katalog;
    }

    public  String getOrganisationsnummer() {
	return organisationsnummer;
    }

    public String getSystemnamn() {
	return systemnamn;
    }

    public String getSystemversion() {
	return systemversion;
    }

    public  String getSystemIp() {
	return systemIp;
    }

}
