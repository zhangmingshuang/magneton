# Exception Processor

## Global Expcetion Processor

The framework customized a `RestControllerAdvice` to intercept all the `Exceptions`.

In default, the **Global Expcetion Processor** handles the `ResponseException` only, you can extend
a customized **Exception Processor** that implements `ExceptionProcessor`.

```java

@Component
public class CustomizedExceptionProcessor implements ExceptionProcessor {

  @Override
  public void registerExceptionProcessor(ExceptionProcessorRegister register) {
    register.addHandler(
        NullPointerException.class, e -> Response.exception().message(NULL_ERROR));
  }
}
```

On this code, it registers a `NullPointerException` processor, and will process if
a `NullPointException` is throwing.

**Note:**
this **Global Exception Processor** uses to process **Exception**'s **Throwable** only, not
include **Error**s.So, it'll ignore all the **Error**'s **Throwable** type.

### Disable Global Exception Processor
```yaml
magneton:
  exception:
    advice:
      enable: false
```
**Note:**
The `ResponseException` transform to the global united `Response` is also invalid,
become an ordinary **RuntimeException**, if **Global Exception Processor** is disabled.

## `ResponseException`

`ResponseException`(RuntimeException) uses to throw a clear message to reply responses,
which combine the **Global Exception Processor** to process.

It usually used to interrupt a business process with an exception.

Usually, throws an exception to interrupt a process is not recommended. But it is useful to rollback
in **DB Affairs**.

