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

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component("publishtask")
@Controller
public class PublishTaskUpdateListener implements TaskListener {

  private SimpMessagingTemplate template;
  private IdentityService identityService;

  private Logger LOG = LoggerFactory.getLogger(PublishTaskUpdateListener.class);
  
  @Autowired
  public PublishTaskUpdateListener(SimpMessagingTemplate template, IdentityService identityService) {
    this.template = template;
    this.identityService = identityService;
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    LOG.info("This is a message from the task update listener");

    User currentUser = identityService.createUserQuery()
        .userId(identityService.getCurrentAuthentication().getUserId())
        .singleResult();

    TaskUpdate update = new TaskUpdate();
    update.setTaskName(delegateTask.getName());
    update.setUpdatingUser(currentUser.getFirstName());
    update.setTaskEvent(delegateTask.getEventName());

    template.convertAndSend("/topic/" + delegateTask.getId(), update);
  }
}
