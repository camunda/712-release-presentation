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

import static org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSetup {

  private static final String USER_ID = "john";
  private static final String USER_NAME = "John";
  private static final String USER_LASTNAME = "Doe";

  @Autowired
  private IdentityService identityService;

  @PostConstruct
  public void createSecondAdminUser() {
    boolean userExists = identityService.createUserQuery().userId(USER_ID).count() > 0;

    if (userExists) {
      return;
    }

    User user = identityService.newUser(USER_ID);
    user.setFirstName(USER_NAME);
    user.setLastName(USER_LASTNAME);
    user.setPassword(USER_ID);
    identityService.saveUser(user);

    identityService.createMembership(USER_ID, CAMUNDA_ADMIN);
  }
}
