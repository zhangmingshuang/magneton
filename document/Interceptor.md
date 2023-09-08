# 拦截器

[返回目录](../guide.md)

框架的拦截器功能可以使开发人员不需要关注如何接入到SpringBoot框架中，只需要定义拦截器， 然后注解`@Component`即可。

## 定义拦截器

```java
@Component
public class SignInterceptor implements InterceptorAdapter {
    @Override
    @Nullable
    public HandlerInterceptorAdapter handlerInterceptorAdapter() {
        // 定义Spring的 HandlerInterceptorAdapter 拦截器
        return new HandlerInterceptorAdapter(){
            @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse result, Object handler) {
            ....
        };
    }
}
```

在上面的代码中，方法`HandlerInterceptorAdapter handlerInterceptorAdapter`允许返回一个`null`
表示不作用。这个功能可以使在业务时使用开启来决定某个拦截器是否要作用。

比如：

```java
@Override
public HandlerInterceptorAdapter handlerInterceptorAdapter() {
    if (!this.lookProperties.isSign()) {
        return null;
    }
    return new ....;
}
```

### 声明拦截地址规则

```java
@Override
public Set<String> pathPatterns() {
    return Sets.newHashSet("/api/**");
}
```

声明的方式其实是一个集合，可以声明多个拦截地址规则。如上面的声明，表示该拦截器拦截所有的`/api/`开头的请求路径。

### 声明过滤拦截地址规则

```java
@Nullable
@Override
public Set<String> excludePathPatterns() {
    return Sets.newHashSet("/api-doc/**", "/api/basic/getSecretKey","/api/pay/wechatcb");
}
```

该声明表示的是，在拦截器需要拦截的地址规则中，过滤掉`/api-doc/**`，`/api/basic/getSecretKey`和`/api/pay/wechatcb`这三个地址。

### 注解式声明过滤拦截地址规则

为便于在多个拦截器中重复的声明需要过滤的地址规则，可以在类或者方法中使用`@ExcludeInterceptor`注解。

```java
@RestController
@RequestMapping("/api/auth")
@ExcludeInterceptor({ AuthInterceptor.class })
public class AuthController {
    ....
}
```

比如，如上声明中，则对应`AuthInterceptor`拦截器则会自动忽略`/api/auth/**`的请求地址的拦截。

如果没有指定特定的拦截器，即仅声明了`@ExcludeInterceptor`，则表示作用于所有的拦截器。

该注解可以作用于类与方法，但是前提是对应的类与方法必须有声明的`@RequestMapping`注解。

除此之外，还可以使用`@GlobalExcludeInterceptor("...")`来声明全局的过滤地址规则。

比如：

```java
@GlobalExcludeInterceptor("/api-doc/**")
```

表示所有的拦截器均需要过滤对应的`/api-doc/**`请求地址的拦截。