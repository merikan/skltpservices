<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="xslt/FormatTakeCareMsgGetAvailableTimeslots_req.map" md5sum="15a186b9b21e368f9c1bf4af9cd4f029" version="7.0.400" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.400
*
*   Mapping file:		FormatTakeCareMsgGetAvailableTimeslots_req.map
*   Map declaration(s):	FormatTakeCareMsgGetAvailableTimeslots_req
*   Input file(s):		smo://smo/name%3Dwsdl-primary/transientContext%3D%257Burn%253Amvk%253Aasb%253Atakecare%253Av100%253ATakeCareServiceBO%257DTakeCareServiceBO/message%3D%257Burn%253Amvk%253Aasb%253Atakecare%253Av100%253AgetAvailableTimeslotsInternalInterface%257DinvokeTakeCareRequestMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/transientContext%3D%257Burn%253Amvk%253Aasb%253Atakecare%253Av100%253ATakeCareServiceBO%257DTakeCareServiceBO/message%3D%257Burn%253Amvk%253Aasb%253Atakecare%253Av100%253AgetAvailableTimeslotsInternalInterface%257DinvokeTakeCareRequestMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:str="http://exslt.org/strings"
    xmlns:set="http://exslt.org/sets"
    xmlns:math="http://exslt.org/math"
    xmlns:exsl="http://exslt.org/common"
    xmlns:date="http://exslt.org/dates-and-times"
    xmlns:io="urn:mvk:asb:takecare:v100:TakeCareCommonAttribute"
    xmlns:io2="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io4="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io3="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io5="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:io6="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:io7="wsdl.urn:mvk:asb:takecare:v100:getAvailableTimeslotsInternalInterface"
    xmlns:io8="urn:ProfdocHISMessage:GetAvailableTimeslots:Request"
    xmlns:xsd4xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io9="http://www.w3.org/2005/08/addressing"
    xmlns:io10="urn:mvk:asb:takecare:v100:getAvailableTimeslotsInternalInterface"
    xmlns:map="http://Mvk_Scheduling_TakeCare_Adapter/xslt/FormatTakeCareMsgGetAvailableTimeslots_req"
    xmlns:msl="http://www.ibm.com/xmlmap"
    exclude-result-prefixes="xalan str set msl math map exsl date"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root wrapper template  -->
  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="msl:datamap">
        <msl:datamap>
          <dataObject>
            <xsl:attribute name="xsi:type">
              <xsl:value-of select="'io7:invokeTakeCareRequestMsg'"/>
            </xsl:attribute>
            <xsl:call-template name="map:FormatTakeCareMsgGetAvailableTimeslots_req2">
              <xsl:with-param name="body" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="body" mode="map:FormatTakeCareMsgGetAvailableTimeslots_req"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:FormatTakeCareMsgGetAvailableTimeslots_req">
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'io7:invokeTakeCareRequestMsg'"/>
      </xsl:attribute>
      <io10:invokeTakeCare>
        <io8:ProfdocHISMessage>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/@InvokingSystem"(string) to "InvokingSystem"(string) -->
          <xsl:attribute name="InvokingSystem">
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/@InvokingSystem"/>
          </xsl:attribute>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType"(string) to "MsgType"(string) -->
          <xsl:if test="io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType">
            <xsl:attribute name="MsgType">
              <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType"/>
            </xsl:attribute>
          </xsl:if>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/@Time"(unsignedLong) to "Time"(unsignedLong) -->
          <xsl:attribute name="Time">
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/@Time"/>
          </xsl:attribute>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"(string) to "BookingId"(string) -->
          <xsl:if test="io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId">
            <BookingId>
              <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"/>
            </BookingId>
          </xsl:if>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitIdType"(string) to "CareUnitIdType"(string) -->
          <CareUnitIdType>
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitIdType"/>
          </CareUnitIdType>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitId"(string) to "CareUnitId"(string) -->
          <CareUnitId>
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitId"/>
          </CareUnitId>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/StartDate"(unsignedInt) to "StartDate"(unsignedInt) -->
          <StartDate>
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/StartDate"/>
          </StartDate>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/EndDate"(unsignedInt) to "EndDate"(unsignedInt) -->
          <EndDate>
            <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/EndDate"/>
          </EndDate>
          <!-- variables for custom code -->
          <xsl:variable name="TimeTypeId" select="io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"/>
          <xsl:variable name="BookingId2" select="io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"/>
          <xsl:if test="not($BookingId2)">
            <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"(unsignedShort) to "TimeTypeId"(unsignedShort) -->
            <TimeTypeId>
              <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"/>
            </TimeTypeId>
          </xsl:if>
          <!-- a simple data mapping: "io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId"(unsignedLong) to "ResourceId"(unsignedLong) -->
          <xsl:if test="io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId">
            <ResourceId>
              <xsl:value-of select="io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId"/>
            </ResourceId>
          </xsl:if>
        </io8:ProfdocHISMessage>
        <!-- a simple data mapping: "io10:invokeTakeCare/TargetAdressURLString"(string) to "TargetAdressURLString"(string) -->
        <xsl:choose>
          <xsl:when test="normalize-space(io10:invokeTakeCare/TargetAdressURLString)">
            <TargetAdressURLString>
              <xsl:value-of select="io10:invokeTakeCare/TargetAdressURLString"/>
            </TargetAdressURLString>
          </xsl:when>
          <xsl:otherwise>
            <TargetAdressURLString xsi:nil="true"/>
          </xsl:otherwise>
        </xsl:choose>
        <!-- a structural mapping: "io10:invokeTakeCare/ProfdocHISMessageHeader"(TakeCareCommonAttribute) to "ProfdocHISMessageHeader"(TakeCareCommonAttribute) -->
        <xsl:copy-of select="io10:invokeTakeCare/ProfdocHISMessageHeader"/>
      </io10:invokeTakeCare>
    </body>
  </xsl:template>

  <!-- This rule represents a type mapping: "body" to "body".  -->
  <xsl:template name="map:FormatTakeCareMsgGetAvailableTimeslots_req2">
    <xsl:param name="body"/>
    <io10:invokeTakeCare>
      <io8:ProfdocHISMessage>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@InvokingSystem"(string) to "InvokingSystem"(string) -->
        <xsl:attribute name="InvokingSystem">
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@InvokingSystem"/>
        </xsl:attribute>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType"(string) to "MsgType"(string) -->
        <xsl:if test="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType">
          <xsl:attribute name="MsgType">
            <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@MsgType"/>
          </xsl:attribute>
        </xsl:if>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@Time"(unsignedLong) to "Time"(unsignedLong) -->
        <xsl:attribute name="Time">
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/@Time"/>
        </xsl:attribute>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"(string) to "BookingId"(string) -->
        <xsl:if test="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId">
          <BookingId>
            <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"/>
          </BookingId>
        </xsl:if>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitIdType"(string) to "CareUnitIdType"(string) -->
        <CareUnitIdType>
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitIdType"/>
        </CareUnitIdType>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitId"(string) to "CareUnitId"(string) -->
        <CareUnitId>
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/CareUnitId"/>
        </CareUnitId>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/StartDate"(unsignedInt) to "StartDate"(unsignedInt) -->
        <StartDate>
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/StartDate"/>
        </StartDate>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/EndDate"(unsignedInt) to "EndDate"(unsignedInt) -->
        <EndDate>
          <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/EndDate"/>
        </EndDate>
        <!-- variables for custom code -->
        <xsl:variable name="TimeTypeId" select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"/>
        <xsl:variable name="BookingId2" select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/BookingId"/>
        <xsl:if test="not($BookingId2)">
          <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"(unsignedShort) to "TimeTypeId"(unsignedShort) -->
          <TimeTypeId>
            <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/TimeTypeId"/>
          </TimeTypeId>
        </xsl:if>
        <!-- a simple data mapping: "$body/io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId"(unsignedLong) to "ResourceId"(unsignedLong) -->
        <xsl:if test="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId">
          <ResourceId>
            <xsl:value-of select="$body/io10:invokeTakeCare/io8:ProfdocHISMessage/ResourceId"/>
          </ResourceId>
        </xsl:if>
      </io8:ProfdocHISMessage>
      <!-- a simple data mapping: "$body/io10:invokeTakeCare/TargetAdressURLString"(string) to "TargetAdressURLString"(string) -->
      <xsl:choose>
        <xsl:when test="normalize-space($body/io10:invokeTakeCare/TargetAdressURLString)">
          <TargetAdressURLString>
            <xsl:value-of select="$body/io10:invokeTakeCare/TargetAdressURLString"/>
          </TargetAdressURLString>
        </xsl:when>
        <xsl:otherwise>
          <TargetAdressURLString xsi:nil="true"/>
        </xsl:otherwise>
      </xsl:choose>
      <!-- a structural mapping: "$body/io10:invokeTakeCare/ProfdocHISMessageHeader"(TakeCareCommonAttribute) to "ProfdocHISMessageHeader"(TakeCareCommonAttribute) -->
      <xsl:copy-of select="$body/io10:invokeTakeCare/ProfdocHISMessageHeader"/>
    </io10:invokeTakeCare>
  </xsl:template>

  <!-- *****************    Utility Templates    ******************  -->
  <!-- copy the namespace declarations from the source to the target -->
  <xsl:template name="copyNamespaceDeclarations">
    <xsl:param name="root"/>
    <xsl:for-each select="$root/namespace::*">
      <xsl:copy/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>