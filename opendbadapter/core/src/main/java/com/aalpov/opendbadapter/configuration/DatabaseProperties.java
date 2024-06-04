package com.aalpov.opendbadapter.configuration;

public class DatabaseProperties {
  String url;
  String user;
  String password;

  public DatabaseProperties(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public String getUrl() {
    return url;
  }

  public String getUser() {
    return user;
  }
}
