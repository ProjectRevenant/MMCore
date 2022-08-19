package com.gestankbratwurst.core.mmcore.data.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.ServerAddress;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Builder
public class MongoDriverProperties {

  private String user;
  private String database;
  private String password;
  private String hostAddress;
  private int hostPort;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHostAddress() {
    return hostAddress;
  }

  public void setHostAddress(String hostAddress) {
    this.hostAddress = hostAddress;
  }

  public int getHostPort() {
    return hostPort;
  }

  public void setHostPort(int hostPort) {
    this.hostPort = hostPort;
  }

  @JsonIgnore
  public List<ServerAddress> getSingletonHostList() {
    return Collections.singletonList(new ServerAddress(this.hostAddress, this.hostPort));
  }

}
