package se.skl.components.pull.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.skl.components.pull.domain.GetUpdatesStatus;
import se.skl.components.pull.service.GetUpdatesService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Author: Henrik Rostam
 */
@Path("/status")
@Component("statusView")
public class StatusView {

    @Autowired
    private GetUpdatesService getUpdatesService;

    @GET
    @Produces("text/html")
    public String viewStatus() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<GetUpdatesStatus> getUpdatesTypes = getUpdatesService.fetchAll();
        String simpleTable = "<table>\n";
            simpleTable += "<thead>";
                simpleTable += "<tr>";
                    simpleTable += "<th align=\"left\">";
                    simpleTable += "Logical address";
                    simpleTable += "</th>";
                    simpleTable += "<th align=\"left\">";
                    simpleTable += "Service domain";
                    simpleTable += "</th>";
                    simpleTable += "<th align=\"left\">";
                    simpleTable += "Errors since last success";
                    simpleTable += "</th>";
                    simpleTable += "<th align=\"left\">";
                    simpleTable += "Last success time";
                    simpleTable += "</th>";
                simpleTable += "</tr>";
            simpleTable += "</thead>";
                for (GetUpdatesStatus status : getUpdatesTypes) {
                    simpleTable += "<tr>";
                        simpleTable += "<td align=\"left\">";
                            simpleTable += status.getLogicalAddress();
                        simpleTable += "</td>";
                        simpleTable += "<td align=\"left\">";
                            simpleTable += status.getServiceDomain();
                        simpleTable += "</td>";
                        simpleTable += "<td align=\"left\">";
                            simpleTable += status.getAmountOfErrorsSinceLastSuccess();
                        simpleTable += "</td>";
                        simpleTable += "<td align=\"left\">";
                            simpleTable += simpleDateFormat.format(status.getLastSuccess());
                        simpleTable += "</td>";
                    simpleTable += "</tr>";
                }
        simpleTable += "</table>";
        return simpleTable;
    }

}
