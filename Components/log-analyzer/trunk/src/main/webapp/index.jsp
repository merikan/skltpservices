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
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page
	import="se.skl.skltpservices.components.analyzer.services.LogAnalyzerService,se.skl.skltpservices.components.analyzer.services.Event,org.springframework.web.context.support.WebApplicationContextUtils,se.skl.skltpservices.components.analyzer.domain.ServiceProducer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Driftstatus Tjänsteplattformen | Inera - Säkra och
	innovativa lösningar för vården</title>

<meta charset='utf-8'>

<style>
.hide {
    display:none;
}
</style>

<link href="css/inera.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="css/inera-print.css" rel="stylesheet" type="text/css"
	media="screen" />

<link rel="shortcut icon" href="images/favicon.png" />

<script src="http://code.jquery.com/jquery.min.js"></script>

<script type="text/javascript">


<%LogAnalyzerService analyzer = (LogAnalyzerService) WebApplicationContextUtils
					.getWebApplicationContext(getServletContext()).getBean(
							"logAnalyzerService");
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			out.print("\tvar data = [");
			ServiceProducer[] arr = analyzer.getServicePproducers();
			for (int i = 0; i < arr.length; i++) {
				ServiceProducer sp = arr[i];
				out.print("{ ");
				out.print("\"endpoint\" : \"" + sp.getServiceUrl() + "\"");
				out.print(", \"system\" : \"" + sp.getSystemName() + "\"");
				out.print(", \"domain\" : \"" + sp.getDomainName() + "\"");
				out.print(", \"description\" : \"" + sp.getDomainDescription()
						+ "\"");
				
				java.util.Date ts = new java.util.Date(sp.getLastUpdated());
				out.print(", \"timestamp\" : \"" + fmt.format(ts) + "\"");
				out.print(", \"status\" : \""
						+ analyzer.calcRuntimeStatus(sp.getTimeLine()) + "\"");
				out.print(", \"latency\" : ");
				out.print(sp.getMaxLatency());
				out.print(" }" + ((i < (arr.length - 1)) ? ", " : ""));
			}
			out.print("]");%>
			
			
	$(document).ready(function() {
		$('#status-table tbody > tr').empty();
		var old = null;
		$.each(data, function(i, e) {
			var icon = (e.status == 'UP') ? $('<img>', { src : 'images/up.png' }) : $('<img>', { src : 'images/down.png' });
			var row = $('<tr>').attr('id', e.status);
			var domain = $('<td>');
			if (old != e.domain) {
				domain.html('<strong>' + e.domain + '</strong><br/>' + e.description);
			}
			row.append(domain);
			row.append($('<td>').append(icon));
			row.append($('<td>').html(e.system + '<br/>' + e.endpoint));
			row.append($('<td>').css('text-align', 'right').html(e.latency));
			row.append($('<td>').css('text-align', 'right').html(e.timestamp));
			
			$('#status-table').append(row);
			old = e.domain;
		});
		
		var hideText = 'Göm fungerande ' + $('#UP').length + ' (' + data.length + ')';
		$('#toggleBtn').html(hideText);
		$('#toggleBtn').click(function () {
			 if ($('#UP').hasClass('hide')) {
				 $('#UP').removeClass('hide');
				 $('#toggleBtn').html(hideText);
			 } else {
				 $('#UP').addClass('hide'); 
				 $('#toggleBtn').html('Visa alla ('  + data.length + ')');
			 }
		});
	});
</script>
</head>
<body>
	<div style="float: left; margin: 10px;">
		<a href="http://www.inera.se"><img src="images/inera-logo.png"/></a>
	</div>
	<div id="mainwrapper">
		<div id="contentarea">
			<h1>Status för tjänsteplattformens producenter
				(PingForConfiguration)</h1>
			<p />
			<div id="contentarea-inner">
				<button id="toggleBtn" style="background-color: #8A8E36;  color: #ffffff;"></button>
				<table id="status-table" style="width: 80%">
					<thead>
						<tr>
							<td>Domän</td>
							<td colspan="2">System</td>
							<td style='text-align: right;'>Senaste svarstid (ms)</td>
							<td style='text-align: right;'>Senaste access</td>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
