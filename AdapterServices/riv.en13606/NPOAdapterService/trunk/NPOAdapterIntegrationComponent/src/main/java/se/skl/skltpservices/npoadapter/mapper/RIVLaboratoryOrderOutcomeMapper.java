/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
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
package se.skl.skltpservices.npoadapter.mapper;

import javax.xml.stream.XMLStreamReader;

import se.skl.skltpservices.npoadapter.mapper.error.MapperException;

public class RIVLaboratoryOrderOutcomeMapper extends LaboratoryOrderOutcomeMapper {

	@Override
	public String mapRequest(String uniqueId, XMLStreamReader reader) throws MapperException {
		return super.mapRequest(uniqueId, reader);
	}

	@Override
	public String mapResponse(String uniqueId, XMLStreamReader reader) throws MapperException {
		return super.mapResponse(uniqueId, reader);
	}
	
}