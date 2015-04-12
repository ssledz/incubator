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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class TaskExecutor {

	public static final Payload EMPTY_PAYLOAD = new Payload() {
		@Override
		public Payload addPayload(final File payload) {
			return this;
		}
	};
	
	private Map<Task, List<File>> payloads = new HashMap<Task, List<File>>();

	private List<Task> tasks = new LinkedList<>();

	public void execute() {
		for (final Task task : tasks) {
			final List<File> files = payloads.get(task);
			if (files == null) {
				continue;
			}
			for (final File payload : files) {
				task.execute(payload);
			}
		}
	}

	public Payload addTask(final Task task) {
		tasks.add(task);
		return new PayloadImpl(task);
	}

	public void addPayload(final File payload, final Task task) {

		List<File> files = payloads.get(task);
		if (files == null) {
			files = new LinkedList<>();
			payloads.put(task, files);
		}

		files.add(payload);

	}

	public interface Payload {
		public Payload addPayload(final File payload);
	}

	private class PayloadImpl implements Payload {

		private final Task task;

		private PayloadImpl(final Task task) {
			this.task = task;
		}

		@Override
		public Payload addPayload(final File payload) {
			TaskExecutor.this.addPayload(payload, task);
			return this;
		}
	}
}
