/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.shared.task;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class TaskExecutor {

	private List<File> payloads = new LinkedList<>();
	
	private List<Task> tasks = new LinkedList<>();
	
	public void execute() {
		for(final Task task : tasks) {
			for(final File payload : payloads) {
				task.execute(payload);
			}
		}
	}
	
	public void addTask(final Task task) {
		tasks.add(task);
	}
	
	public void addPayload(final File payload) {
		payloads.add(payload);
	}
}
