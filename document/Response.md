# 统一响应

框架提供了一个统一的响应类`Response`，用于返回给客户端的数据。

## 使用

```java
// 成功
return Response.ok();
// 成功并响应数据
return Response.ok(data);
// 失败
return Response.bad();
// 失败并携带提示信息
return Response.bad().message("It's bad");
```

## 默认响应码

| Response Code | Represent |
|---------------|-----------|
| 0             | success   |
| 1             | fail      |
| 2             | exception 

### 自定义默认响应码

```java
ResponseCodesSupplier.Instance.setResponseCodesSupplier(new ResponseCodesSupplier(){
    @Override 
    public ResponseMessage bad(){
        return ResponseMessage.valueOf("404","Operation Error");
    }
});
```

### 响应码定义

框架提供了统一的响应码定义接口`ResponseMessage`，用来进行响应码的定义。

```java
public enum Errors implements ResponseMessage {

	TOO_NEAR("1004001", "上报过于频繁");
}
```

使用：

```java
Response.result(Errors.TOO_NEAR);
```

## 响应结果对象自定义响应消息内容

在一些响应的结果对象中，希望可以自定义该对应要响应给的消息内容，而不需要每次都设置相同的响应消息。

```java
public class ReturnData implements EgoResponseMessage {

	@Override
	public String message() {
		return "Autonomous";
	}
}
```

在如上的响应结果对象`ReturnData`中，重写了`message()`方法，返回自定义的消息内容。

```java
Response.ok(new ReturnData());
```

此时，响应的数据的消息内容为`Autonomous`。

## 响应异常

框架提供了一个响应异常的处理器，用来在业务处理时可以直接使用异常进行响应。

```java
throw ResponseException.valueOf(Response.bad().message("It's bad"));
//或者
throw ResponseException.valueOf(new ResponseMessage(){ ... });
```
