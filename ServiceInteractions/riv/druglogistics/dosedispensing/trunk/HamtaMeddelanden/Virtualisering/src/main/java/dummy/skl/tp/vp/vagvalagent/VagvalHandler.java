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
package dummy.skl.tp.vp.vagvalagent;

import static se.skl.tp.hsa.cache.HsaCache.DEFAUL_ROOTNODE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConstants;

import dummy.skl.tp.vp.exceptions.VpSemanticException;

import se.skl.tp.hsa.cache.HsaCache;
import se.skl.tp.hsa.cache.HsaCacheInitializationException;
import se.skl.tp.vagval.wsdl.v1.VisaVagvalRequest;
import se.skl.tp.vagval.wsdl.v1.VisaVagvalResponse;
import se.skl.tp.vagvalsinfo.wsdl.v1.VirtualiseringsInfoType;

public class VagvalHandler {

	// Initialized to a non-null value by the constructor.
	private List<VirtualiseringsInfoType> virtualiseringsInfo = null;
	private Map<String, List<VirtualiseringsInfoType>> virtualiseringsInfoMap;

	// Initialized to a non-null value by the constructor.
	private HsaCache hsaCache;

	public VagvalHandler(HsaCache hsaCache, List<VirtualiseringsInfoType> virtualiseringsInfo) {

		if (hsaCache == null) {
			throw new RuntimeException("Null is not allowed for the parameter hsaCache");
		}
		if (virtualiseringsInfo == null) {
			throw new RuntimeException("Null is not allowed for the parameter virtualiseringsInfo");
		}

		this.hsaCache = hsaCache;
		this.virtualiseringsInfo = virtualiseringsInfo;
		this.virtualiseringsInfoMap = createVirtualiseringsInfoMap(virtualiseringsInfo);
	}
	
	public int size() {
		return virtualiseringsInfo.size();
	}
	
	public List<VirtualiseringsInfoType> getVirtualiseringsInfo() {
		return virtualiseringsInfo;
	}
	
	public VisaVagvalResponse getRoutingInformation(VisaVagvalRequest request, boolean useDeprecatedDefaultRouting, List<String> receiverAddresses) {
		// Check if routing was found in TAK for requested receiver.
		VisaVagvalResponse response = getRoutingInformationFromLeaf(request, useDeprecatedDefaultRouting, receiverAddresses);

		// No routing information found for requested receiver, check in the HSA
		// tree if there is any routing information registered on parents.
		// Routing using HSA tree is only applicable when not using deprecated
		// default routing (VG#VE)
		if (containsNoRouting(response) && !useDeprecatedDefaultRouting) {
			response = getRoutingInformationFromParent(request, receiverAddresses);
		}
		return response;
	}

	public boolean containsNoRouting(VisaVagvalResponse response) {
		return response == null || response.getVirtualiseringsInfo().isEmpty();
	}
	
	public VisaVagvalResponse getRoutingInformationFromLeaf(VisaVagvalRequest request, boolean useDeprecatedDefaultRouting, List<String> receiverAddresses) {

		VisaVagvalResponse response = new VisaVagvalResponse();

		// Outer loop over all given addresses
		boolean addressFound = false;
		for (String requestReceiverId : receiverAddresses) {
			// Find all possible LogiskAdressat
			
			// Start to lookup elements matching recevier, tjanstekontrakt in the map
			List<VirtualiseringsInfoType> matchingVirtualiseringsInfo = lookupInVirtualiseringsInfoMap(requestReceiverId, request.getTjanstegranssnitt());
			
			// Skip if no hit in the map
			if (matchingVirtualiseringsInfo != null) {

				// Now look through that list for matching time intervals
				for (VirtualiseringsInfoType vi : matchingVirtualiseringsInfo) {
					if (request.getTidpunkt().compare(vi.getFromTidpunkt()) != DatatypeConstants.LESSER &&
						request.getTidpunkt().compare(vi.getTomTidpunkt()) != DatatypeConstants.GREATER) {
						addressFound = true;
						response.getVirtualiseringsInfo().add(vi);
					}
				}
			}
			
			// Only return 1 address if we do a delimiter search!
			if (useDeprecatedDefaultRouting && addressFound) {
				break;
			}
		}
		return response;
	}

	public VisaVagvalResponse getRoutingInformationFromParent(VisaVagvalRequest request, List<String> receiverAddresses) {

		VisaVagvalResponse response = new VisaVagvalResponse();

		String receiverId = receiverAddresses.get(0);
		while (response.getVirtualiseringsInfo().isEmpty() && receiverId != DEFAUL_ROOTNODE) {
			receiverId = getParent(receiverId);
			// FIXME: When deprecated default routing is removed
			// construction using Arrays.asList(receiverId) can be replaced
			// with String
			response = getRoutingInformationFromLeaf(request, false, Arrays.asList(receiverId));
		}

		return response;
	}

	private String getParent(String receiverId) {
		try {
			return hsaCache.getParent(receiverId);
		} catch (HsaCacheInitializationException e) {
			throw new VpSemanticException("VP011 Internal HSA cache is not available!", e);
		}
	}
	
	/* VIRTUALISERINGS-INFO MAP METHODS */

	private Map<String, List<VirtualiseringsInfoType>> createVirtualiseringsInfoMap(List<VirtualiseringsInfoType> inVirtualiseringsInfo) {

		Map<String, List<VirtualiseringsInfoType>> outVirtualiseringsInfoMap = new HashMap<String, List<VirtualiseringsInfoType>>();
		
		// Loop through all entries in the list and store them by recevier and tjanstekontrakt in a map for faster lookup
		for (VirtualiseringsInfoType v : inVirtualiseringsInfo) {
			String key = createVirtualiseringsInfoMapKey(v.getReceiverId(), v.getTjansteKontrakt());
			
			// Lookup the entry (list) in the map and create it if missing
			List<VirtualiseringsInfoType> value = outVirtualiseringsInfoMap.get(key);
			if (value == null) {
				value = new ArrayList<VirtualiseringsInfoType>();
				outVirtualiseringsInfoMap.put(key, value);
			}
			
			// Add the record to the list
			value.add(v);
		}

		return outVirtualiseringsInfoMap;
	}
	
	List<VirtualiseringsInfoType> lookupInVirtualiseringsInfoMap(String receiverId, String tjansteKontrakt) {
		String key = createVirtualiseringsInfoMapKey(receiverId, tjansteKontrakt);
		return virtualiseringsInfoMap.get(key);
	}

	private String createVirtualiseringsInfoMapKey(String receiverId, String tjansteKontrakt) {
		return receiverId + "|" + tjansteKontrakt;
	}
}