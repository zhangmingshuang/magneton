# Global United Response

The frameworl customized a global united response class : `Response`.

## Use

```java
  return Response.ok();
    //or return with data
    return Response.ok(data);
    //return fail
    return Response.bad();
    //return fail with message
    return Response.bad().message("It's bad");
```

## Response Code

In default, response code table below:

| Response Code | Represent |
| ---------- | ------ |
| 0 | success |
| 1 | fail |
| 2 | exception

### Change default response's code rule.

The inner class `Instance` in `ResponseCodes` exposed the entrance `setResponseCodes` to change the
default response's code supplier.

```java
ResponseCodesSupplier.Instance.setResponseCodesSupplier(
    new ResponseCodesSupplier(){
@Override
public ResponseMessage bad(){
    return ResponseMessage.valueOf(CODE,BAD);
    }
    });
```

## Use Resonse's Data Message: EgoResponseMessage

In some cases, you may want uses the response's data object to control the response's message.

At this time, you can implement the `EgoResponseMessage` to customize the resonse's message.

```java
public class ReturnData implements EgoResponseMessage{
  
  @Override
  public String message(){
    return "Autonomous";  
  }
}
```
Then, the response message:
```java
Response.ok(new ReturnData());
```
is "Autonomous".
