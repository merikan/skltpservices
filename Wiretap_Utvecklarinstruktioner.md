# Instruktioner för utvecklare #

## Initial uppsättning av utvecklingsmiljön ##

Källkoden för wiretap är utvecklad med hjälp av soi-toolkit.

För att kunna utveckla, underhålla och/eller felsöka källkoden för wiretap behöver man sätta upp en utvecklingsmiljö enligt instruktion på soi-toolkit's wiki: Installation Guide.

Eftersom källkoden för wiretap versionshanteras i Subversion så rekommenderas också att en subversion-plugin, Subversive, också installeras i Mule Studio: Installation Subversive Eclipse plugin.

När detta är gjort kan man hämta ut källkoden för wiretap på http://skltpservices.googlecode.com/svn/Components/intyg-wiretap/trunk.

Öppna därefter ett kommandofönster för att bygga och testa källkoden med hjälp av Maven samt skapa Eclipse projekt-filer för att kunna importera projekten in i Mule Studio:

```
cd trunk
mvn clean install
mvn eclipse:clean eclipse:eclipse
```

Därefter skall man kunna importera Eclipse projekten in i Mule Studio.

## Hur commita kod ##

Innan man commitar ändringar är det väldigt viktigt att man:

  1. Gjort en svn update (eller svn synchronize i Eclipse pluginen) för att säkerställa att man har senaste koden hos sig. Gör man inte det så kan det leda till onödiga merge-problem när man väl commitar eller att commitade ändringar bryter tester i byggservern.
  1. Gör en fullständig mvn clean install på trunk nivå för att försäkra sig om att allt fortfarande sitter ihop innan man commitar. Se till att stänga alt restarta Mule Studio innan mvn clean startas annars upptäcker Mule Studio detta och börjar kompilera om klasser vilket kan förvilla maven och bygget går då fel...
  1. Commita med en relevant commit kommentar.
Företrädesvis skall kod commitas mot ett JIRA-ärende och då skall dess id vara prefix i commit kommentaren. Jobbar man inte mot ett JIRA ärende få man prefixa sin commit kommentar med "WIRETAP, " för att skilja ut WIRETAP-ändringar mot ändraingar i svn för andra SKLTP komponenter.