#!/usr/bin/env groovy

import groovy.io.FileType
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

@Grab(group='dom4j', module='dom4j', version='1.6.1')
import org.dom4j.io.SAXReader


/**
 *Script to extract namespaces from WSDL.
 */

def getAllFilesMatching(direcory, pattern){
	def filesFound = []
	direcory?.traverse(type:FileType.FILES, nameFilter: ~pattern){ fileFound -> filesFound << fileFound }
	filesFound.each { fileFound -> println "File to process: ${fileFound.name}" }
	return filesFound
}

def getAllDirectoriesMatching(direcory, pattern){
	def dirsFound = []
	direcory?.traverse(type:FileType.DIRECTORIES, nameFilter: ~pattern){ dirFound -> dirsFound << dirFound }
	dirsFound.each { dirFound -> println "Directory to process: ${dirFound}" }
	return dirsFound
}

def getFilePattern(rivtaProfile, version){
	def pattern = ".*_${version}.*(?i)${rivtaProfile}\\.wsdl"
	return pattern
}

def getRelativeSchemaPath(absoluteSchemaPath){
	def startIndex = absoluteSchemaPath.indexOf('schemas')
	def relativeSchemaPath = absoluteSchemaPath.getAt(startIndex..-1)
	relativeSchemaPath = relativeSchemaPath.replace('schemas','')
	return '${schema.path}' + relativeSchemaPath
}

def getAllUniqueRivNameSpaces(wsdlFile, rivtaProfile){
	def rivNameSpaces = []

	new SAXReader().read(wsdlFile).getRootElement().declaredNamespaces().grep(~/.*urn:riv.*/).each{ namespace ->
		if(namespace.text.contains(rivtaProfile)){
			rivNameSpaces << namespace.text
		}
	}
	return rivNameSpaces.unique()
}

def extractNamespaces(wsdlFiles, rivtaProfile){

	wsdlFiles.each { wsdlFile ->
		def rivNameSpaces = getAllUniqueRivNameSpaces(wsdlFile,rivtaProfile)
		rivNameSpaces.each {println it}
	}
}

if( args.size() < 3){
	println "For this script to work, you need Groovy version 1.8.2 or higher."
	println "This is a script that will extract namespaces from wsdl files in path. The script requires the directory"
	println "structure to follow the RIVTA standard: "
	println "schemas/..."
	println "    ...interactions/<a folder per service interaction>/<service schemas and wsdl file>"
	println "    ...core_components/<all core schemas>"
	println ""
	println "Required parameters: service domain root directory [domainDir], service domain [domain], RIV TA Profile [rivtaprofile], Major version [version] \n"
	println "PARAMETERS DESCRIPTION:"
	println "[domainDir] is the base direcory where this script will start working, i.e. the parent directory of the 'schemas' directory "
	println "[rivtaprofile], the short-name of the RIVTA profile e.g rivtabp20"
	println "[version], the major version of the service domain e.g 1, 2 or 3 etc"
	println "example invocation:"
	println "	groovy JaxWsMavenPomGenerator . rivtabp20 1"
	println ""
	println "OUTPUT:"
	println "namespaces.txt"
	return
}

def baseDir = new File(args[0])
def rivtaProfile = args[1]
def version = args[2]

def pattern = getFilePattern(rivtaProfile, version)
def wsdlFiles = getAllFilesMatching(baseDir, pattern)
if (wsdlFiles.isEmpty()){
	println "NOTE! No wsdl files found under dir ${baseDir}"
	return
}

extractNamespaces(wsdlFiles, rivtaProfile)

assert '${schema.path}/subdomain/Service/Service_1.1_RIVTABP20.wsdl' == getRelativeSchemaPath("/absolute/path/to/a/service/trunk/schemas/subdomain/Service/Service_1.1_RIVTABP20.wsdl")
assert ".*_1.*(?i)rivtabp21/.wsdl" == getFilePattern("rivtabp21", "1").replaceAll("\\\\","/")