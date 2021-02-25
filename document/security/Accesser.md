# Accesser

Used to access manage.

## Eg

### Login Access Manage

```java
  RequestAccesser requestAccesser =
          (RequestAccesser)
              AccesserBuilder.of(new InMemoryRequestAccesser<>()).build();
  
  public String login() {
    String loginUserName = "zhangmsh";
    String loginPwd = "123";
    Accessible access =
        this.requestAccesser.access(
            loginUserName,
            () -> {
              String pwd = db.getUserPwd(loginUserName);
              return Objects.equals(loginPwd, pwd);
            });
    if (!access.isAccess()) {
      if (access.isLocked()) {
        long ttl = access.getTtl();
        return "locked, can operation after " + ttl + "ms";
      }
      int remainingErrorCount = access.getErrorRemainCount();
      return "error, " + remainingErrorCount + " operating opportunities";
    }
    return "success";
  }
```
