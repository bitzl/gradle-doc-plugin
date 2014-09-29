/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.pandoc.project

import org.apache.commons.io.FileUtils;
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.AbstractDocumentationTask

/**
 * Delete temporary folders and output dir
 * @author afloch
 *
 */
class CleanTask extends AbstractDocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-clean')



	protected void process() {
		FileUtils.deleteDirectory(tmpFolder)
		FileUtils.deleteDirectory(outputDir)
	}
}


