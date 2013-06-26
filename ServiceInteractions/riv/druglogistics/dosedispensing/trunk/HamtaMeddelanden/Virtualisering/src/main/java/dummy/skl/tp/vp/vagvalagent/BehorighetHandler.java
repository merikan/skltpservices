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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConstants;

import dummy.skl.tp.vp.exceptions.VpSemanticException;

import se.skl.tp.hsa.cache.HsaCache;
import se.skl.tp.hsa.cache.HsaCacheInitializationException;
import se.skl.tp.vagval.wsdl.v1.VisaVagvalRequest;
import se.skl.tp.vagvalsinfo.wsdl.v1.AnropsBehorighetsInfoType;

public class BehorighetHandler {

	// Initialized to a non-null value by the constructor.
	private List<AnropsBehorighetsInfoType> permissions;
	private Map<String, List<AnropsBehorighetsInfoType>> permissionMap;

	// Initialized to a non-null value by the constructor.
	private HsaCache hsaCache;

	public BehorighetHandler(HsaCache hsaCache, List<AnropsBehorighetsInfoType> permissions) {
		if (hsaCache == null) {
			throw new RuntimeException("Null is not allowed for the parameter hsaCache");
		}
		if (permissions == null) {
			throw new RuntimeException("Null is not allowed for the parameter permissions");
		}

		this.hsaCache = hsaCache;
		this.permissions = permissions;
		this.permissionMap = createPermissionMap(permissions);
	}
	
	public int size() {
		return permissions.size();
	}
	
	public List<AnropsBehorighetsInfoType> getAnropsBehorighetsInfoList() {
		return permissions;
	}
	
	/**
	 * Check in TAK if sender is authorized to call the requested
	 * receiver,if not check if sender is authorized to call any of the
	 * receiver parents using HSA tree. HSA tree is only used when old
	 * school default routing (VG#VE) is not used.
	 * 
	 * @param request
	 * @param receiverAddresses
	 * @return
	 */
	public boolean isAuthorized(VisaVagvalRequest request, List<String> receiverAddresses) {
		boolean authorized = true;
		if (!isAuthorizedToLeaf(request, receiverAddresses)) {
			if (!isAuthorizedToAnyParent(request)) {
				authorized = false;
			}
		}
		return authorized;
	}
	
	private boolean isAuthorizedToLeaf(VisaVagvalRequest request, List<String> receiverAddresses) {
		for (String requestReceiverId : receiverAddresses) {
			if (isAuthorized(request, requestReceiverId)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAuthorizedToAnyParent(VisaVagvalRequest request) {
		String receiverId = request.getReceiverId();
		while (receiverId != DEFAUL_ROOTNODE) {
			receiverId = getParentInHsaCache(receiverId);
			if (isAuthorized(request, receiverId)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAuthorized(VisaVagvalRequest request, String receiverId) {

		// Start to lookup elements matching recevier, sender and tjanstekontrakt in the map
		List<AnropsBehorighetsInfoType> matchingPermissions = lookupInPermissionMap(receiverId, request.getSenderId(), request.getTjanstegranssnitt());
		
		// Return false if no hit in the map
		if (matchingPermissions == null) return false;
		
		// Now look through that list for matching time intervals
		for (AnropsBehorighetsInfoType abi : matchingPermissions) {
			if (request.getTidpunkt().compare(abi.getFromTidpunkt()) != DatatypeConstants.LESSER &&
			    request.getTidpunkt().compare(abi.getTomTidpunkt()) != DatatypeConstants.GREATER) {
				return true;
			}
		}
		return false;
	}
	
	private String getParentInHsaCache(String receiverId) {
		try {
			return hsaCache.getParent(receiverId);
		} catch (HsaCacheInitializationException e) {
			throw new VpSemanticException("VP011 Internal HSA cache is not available!", e);
		}
	}
	
	/* PERMISSION MAP METHODS */

	private Map<String, List<AnropsBehorighetsInfoType>> createPermissionMap(List<AnropsBehorighetsInfoType> inPermissions) {

		Map<String, List<AnropsBehorighetsInfoType>> outPermissionMap = new HashMap<String, List<AnropsBehorighetsInfoType>>();
		
		// Loop through all entries in the list and store them by recevier, sender and tjanstekontrakt in a map for faster lookup
		for (AnropsBehorighetsInfoType p : inPermissions) {
			String key = createPermissionsMapKey(p.getReceiverId(), p.getSenderId(), p.getTjansteKontrakt());
			
			// Lookup the entry (list) in the map and create it if missing
			List<AnropsBehorighetsInfoType> value = outPermissionMap.get(key);
			if (value == null) {
				value = new ArrayList<AnropsBehorighetsInfoType>();
				outPermissionMap.put(key, value);
			}
			
			// Add the record to the list
			value.add(p);
		}

		return outPermissionMap;
	}
	
	List<AnropsBehorighetsInfoType> lookupInPermissionMap(String receiverId, String senderId, String tjansteKontrakt) {
		String key = createPermissionsMapKey(receiverId, senderId, tjansteKontrakt);
		return permissionMap.get(key);
	}

	private String createPermissionsMapKey(String receiverId, String senderId, String tjansteKontrakt) {
		return receiverId + "|" + senderId + "|" + tjansteKontrakt;
	}
}