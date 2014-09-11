/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.pandoc.generation

import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.DocumentationTask;

/**
 * Generates the documentation in html format
 * @author sleroy
 *
 */
class GenerateMd2HTML extends DocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-html')

	/**
	 * This task collects all md files into the tmp folder and converts them into html files, and then copy resources required to display the pictures
	 */
	@TaskAction
	void runTask() {

		project.fileTree(tmpFolder) { include '**/*.md' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)

			if (project.documentation.conversions[docType].contains('html')) {
				println "Generating HTML doc for ${docFileBase}..."
				println project.file("${outputDir}/${docType}/${docFileBase}.html")
				def generateCmdLine = [
					project.documentation.panDocBin,
					'--write=html5',
					'--template=' + project.file("${tmpTemplatesFolder}/${docType}.html"),
					'--toc',
					'--toc-depth=2',
					'--section-divs',
					'--no-highlight',
					'--smart'
				]
				for (myVar in project.documentation.templateVariables) {
					generateCmdLine.add("--variable=${myVar.key}:${myVar.value}")
				}
				generateCmdLine.addAll( [
					'--output=' + project.file("${outputDir}/${docType}/${docFileBase}.html"),
					"${docFile}"
				])
				project.exec({
					commandLine = generateCmdLine
					workingDir = tmpFolder
				}
				)
			}
		}
		LOGGER.info('Copying resources(pic, scripts, styles) files into distribution site')
		// Copy over resources needed for the HTML docs
		copyGeneratedAndCompiledResources()
	}

	private copyGeneratedAndCompiledResources() {
		project.copy {
			from(tmpFolder) {
				include 'images/**'
				include 'scripts/**'
				include 'styles/**'
			}
			into outputDir
		}
		for (String docType in docTypeNames) {
			project.copy {
				from(new File(tmpFolder, docType)) {
					include 'images/**'
					include 'scripts/**'
					include 'styles/**'
				}
				into new File(outputDir, docType)
			}
		}
	}
}

