#!/usr/bin/env groovy

package se.skltpservices.tools

import groovy.io.FileType

/**
 * 
 */

def getAllserviceInteractionDirectories(directory){
	def dirsFound = []
	directory.traverse(type:FileType.DIRECTORIES, nameFilter: ~/.*Interaction$/){ dirFound -> dirsFound << dirFound }
	dirsFound.each { dirFound -> println "Directory to process: ${dirFound.name}" }
	return dirsFound
}

def buildVirtualServices(serviceInteractionDirectories, targetDir, domain){

	serviceInteractionDirectories.each { serviceInteractionDirectory ->

		def artifactId = serviceInteractionDirectory.name - 'Interaction'
		
		def domainArray = domain.split("\\.")
		def maindomain = domainArray[0]
		def subdomain =  domainArray[1] 
		
		def mvnCommand = """mvn archetype:generate 
		-DinteractiveMode=false
		-DarchetypeArtifactId=service-archetype 
		-DarchetypeGroupId=se.skl 
		-DarchetypeVersion=1.0-SNAPSHOT 
		-Duser.dir=${targetDir} 
		-DgroupId=se.skl.tp 
		-DartifactId=${artifactId} 
		-Dversion=1.0-SNAPSHOT
		-Ddomain=${maindomain} 
		-Dsubdomain=${subdomain} """

		def process = mvnCommand.execute()
		process.waitFor()

		// Obtain status and output
		println "RETURN CODE: ${ process.exitValue()}"
		println "STDOUT: ${process.in.text}"
	}
}

if( args.size() < 2){
	println "This tool generates virtualising components based on service interactions found in sourceDir."
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

def serviceInteractionDirectories = getAllserviceInteractionDirectories(sourceDir)
buildVirtualServices(serviceInteractionDirectories, targetDir, domain)