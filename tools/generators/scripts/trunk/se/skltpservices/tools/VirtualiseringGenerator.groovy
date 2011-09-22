#!/usr/bin/env groovy

package se.skltpservices.tools

import groovy.io.FileType

org.apache.commons.io.FileUtils

@Grab(group='commons-io', module='commons-io', version='1.3.2')
import org.apache.commons.io.FileUtils

/**
 * This script should help us to generate many services at one time. This script is depending on the archetype named service-archetype.
 * This archetype must be installed before running the script.
 *
 * TO RUN:
 * Just execute ./VirtualiseringGenerator.groovy and follow the instruction coming up.
 * 
 * Version info:
 * A first version is created to solve that we would like to generate several service interactions without to much manual work.
 *
 * TODO:
 * Make the script more smart by
 * - Today its hardcoded to rivtabp20
 * - Today it only works for version 1
 * 
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

def buildVirtualServices(serviceInteractionDirectories, targetDir, domain){

	serviceInteractionDirectories.each { serviceInteractionDirectory ->

		def artifactId = serviceInteractionDirectory.name - 'Interaction'
		def schemasFiles = getAllFilesMatching(serviceInteractionDirectory, /.*\.wsdl/)
		def wsdlFileName = schemasFiles[0].name
		def rivtaVersion = 'rivtabp20'
		def serviceVersion = '1'
		def version = '1.1-SNAPSHOT'
		
		def domainArray = domain.split("\\.")
		def maindomain = domainArray[0]
		def subdomain =  domainArray[1] 
		
		def mvnCommand = """mvn archetype:generate 
		-DinteractiveMode=false 
		-DarchetypeArtifactId=service-archetype 
		-DarchetypeGroupId=se.skl.tp.archetype 
		-DarchetypeVersion=1.0-SNAPSHOT 
		-Duser.dir=${targetDir} 
		-DgroupId=se.skl.skltpservices.${maindomain}.${subdomain}
		-DartifactId=${artifactId} 
		-Dversion=${version}
		-DdomainName=${maindomain} 
		-DdomainSubName=${subdomain} 
		-DserviceMethod=${artifactId} 
		-DserviceInteraction=${artifactId}Interaction  
		-DserviceRelativePath=${artifactId}/${serviceVersion}/${rivtaVersion} 
		-DserviceWsdlFile=${wsdlFileName} 
		-DserviceNamespace=urn:riv:${maindomain}:${subdomain}:${artifactId}:${serviceVersion}:${rivtaVersion}  
		"""
		
		def process = mvnCommand.execute()
		process.waitFor()

		// Obtain status and output
		println "RETURN CODE: ${ process.exitValue()}"
		println "STDOUT: ${process.in.text}"
	}
}

def copyServiceSchemas(serviceInteractionDirectories, targetDir){
	serviceInteractionDirectories.each { serviceInteractionDirectory ->
		def schemasFiles = getAllFilesMatching(serviceInteractionDirectory, /.*\.xsd|.*\.xml|.*\.wsdl/)
		
		def serviceInteraction = serviceInteractionDirectory.name
		def serviceDirectory = serviceInteraction - 'Interaction'
		def schemaTargetDir = "${targetDir}/${serviceDirectory}/Virtualisering/src/main/resources/schemas/interactions/${serviceInteraction}"
		new File("${schemaTargetDir}").mkdirs()
		
		schemasFiles.each {sourceSchemaFile -> 
			def targetSchemaFile = new File("${schemaTargetDir}/$sourceSchemaFile.name")
			FileUtils.copyFile(sourceSchemaFile, targetSchemaFile)}
	}
}

def copyCoreSchemas(serviceInteractionDirectories, coreSchemaDirectory, targetDir){
	serviceInteractionDirectories.each { serviceInteractionDirectory ->
		def schemasFiles = getAllFilesMatching(coreSchemaDirectory, /.*\.xsd/)
		
		def serviceInteraction = serviceInteractionDirectory.name
		def serviceDirectory = serviceInteraction - 'Interaction'
		def coreSchemaTargetDir = "${targetDir}/${serviceDirectory}/Virtualisering/src/main/resources/schemas/core_components"
		new File("${coreSchemaTargetDir}").mkdirs()
		
		schemasFiles.each {sourceSchemaFile -> 
			def targetSchemaFile = new File("${coreSchemaTargetDir}/$sourceSchemaFile.name")
			FileUtils.copyFile(sourceSchemaFile, targetSchemaFile)}
		
	}
}

if( args.size() < 2){
	println "This tool generates service virtualising components based on service interactions found in sourceDir. They are generated in the targetDir."
	println "Point sourceDir to the schemas dir containing:"
	println "core_components"
	println "interactions"
	println ""
	println "Required parameters: source directory [sourceDir], target directory [targetDir],domain [domain] \n"
	println "PARAMETERS DESCRIPTION:"
	println "[sourceDir] is the base direcory where this script will start working to look for servivce interactions, e.g /repository/rivta/ServiceInteractions/riv/crm/scheduling/trunk "
	println "[targetDir] is the direcory where new interactions will be generated, e.g /repository/skltpservices/ServiceInteractions/riv/crm/scheduling/trunk "
	println "[domain] is the name of the domain to process, e.g crm.scheduling"
	println ""
	println "OUTPUT:"
	println "New maven folders containing service interactions"
	return
}

def sourceDir = new File(args[0])
def targetDir = new File(args[1])
def domain = args[2]

def serviceInteractionDirectories = getAllDirectoriesMatching(sourceDir,/.*Interaction$/)
def coreSchemaDirectory = getAllDirectoriesMatching(sourceDir,/core_components/)[0]

buildVirtualServices(serviceInteractionDirectories, targetDir, domain)
copyServiceSchemas(serviceInteractionDirectories, targetDir)
copyCoreSchemas(serviceInteractionDirectories, coreSchemaDirectory, targetDir)