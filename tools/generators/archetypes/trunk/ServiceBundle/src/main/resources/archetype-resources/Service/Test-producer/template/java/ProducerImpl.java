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
package ${service.base.package};

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import ${service.package}.${service.name}ResponderInterface;
import ${service.responder.package}.${service.name}Type;
import ${service.responder.package}.${service.name}ResponseType;

@WebService(
		serviceName = "${service.name}ResponderService", 
		endpointInterface="${service.package}.${service.name}ResponderInterface", 
		portName = "${service.name}ResponderPort", 
		targetNamespace = "${service.ns}",
		wsdlLocation = "schemas/interactions/${service.name}Interaction/${service.wsdl.file}")
public class ProducerImpl implements ${service.name}ResponderInterface {

	@Override
	public ${service.name}ResponseType ${service.method.name}(final AttributedURIType logicalAddress, final ${service.name}Type parameters) {
		throw new UnsupportedOperationException("Implement");
	}
}
