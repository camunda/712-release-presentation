/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.bpm.example.release_presentation;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component("forceassign")
@Controller
public class ForceAssignTimeoutListener implements TaskListener {

  private SimpMessagingTemplate template;

  @Autowired
  public ForceAssignTimeoutListener(SimpMessagingTemplate template) {
    this.template = template;
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    String assignee = "demo";
    delegateTask.setAssignee(assignee);

    TaskUpdate update = new TaskUpdate();
    update.setTaskName(delegateTask.getName());
    update.setUpdatingUser(StringUtils.capitalize(assignee));
    update.setTaskEvent(delegateTask.getEventName());

    template.convertAndSend("/topic/" + delegateTask.getId(), update);
  }

}