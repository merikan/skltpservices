<%--

    Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, for (Boston,
    MA 02110-1301  USA

--%>
<!DOCTYPE html>
<%@page
	import="se.skl.skltpservices.components.analyzer.services.LogAnalyzerService,se.skl.skltpservices.components.analyzer.services.Event,org.springframework.web.context.support.WebApplicationContextUtils,se.skl.skltpservices.components.analyzer.domain.ServiceProducer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Mentometer</title>
<meta charset='utf-8'>

<script src="http://code.jquery.com/jquery.min.js"></script>

<script type="text/javascript">
<%LogAnalyzerService analyzer = (LogAnalyzerService) WebApplicationContextUtils
					.getWebApplicationContext(getServletContext()).getBean(
							"logAnalyzerService");
			out.print("var data = [");
			ServiceProducer[] arr = analyzer.getServicePproducers();
			for (int i = 0; i < arr.length; i++) {
				ServiceProducer sp = arr[i];
				out.print("{ ");
				out.print("\"endpoint\" : \"" + sp.getServiceUrl() + "\"");
				out.print(", \"system\" : \"" + sp.getSystemName() + "\"");
				out.print(", \"domain\" : \"" + sp.getDomainName() + "\"");
				out.print(", \"description\" : \"" + sp.getDomainDescription()
						+ "\"");
				out.print(", \"timestamp\" : \"" + sp.getLastUpdated() + "\"");
				out.print(", \"status\" : \""
						+ analyzer.calcRuntimeStatus(sp.getTimeLine()) + "\"");
				out.print(", \"latency\" : ");
				out.print(" }" + ((i < (arr.length - 1)) ? "," : ""));
			}
			out.print("]");%>
	$(document).ready(function() {
			$('#test').html(data[0].endpoint)
	});
	
</script>
</head>
<body>
	<h2>Hello World!</h2>
	<div id="test"></div>
</body>
</html>
