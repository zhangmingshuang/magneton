# Signature

To verify a context in simply, the framework provide a `Signature` implement.

## Use

use the `Signature.builder()` to create a new `Signature`:

```java
Signature signature=
    Signature.builder()
    .salt("test")//the context salt. to make signature more safety.
    .build();
```

The more configuration:

```java
Signature signature=
    Signature.builder()
    .salt("test")//the context salt. to make signature more safety.
    .needBodyKeys(new String[]{"a","b"})
    .signatureContentBuilder((body,salt)->"body")
    .build();
```

In this code, the `.salt` use to add the context salt, which make the signature more safety.

`.needBodyKeys` make a condition with the body's keys contain. if the keys had not included, it will
throw a `SignatureBodyException`.

`.signatureContentBuilder` to change the default signature content
builder(`DefaultSignatureContentBuilder`) which to build the body.

After the `Signature` is created. use this to generate the sign:

```java
    HashMap<String, String> body=Maps.newHashMap();
    body.put("a","av");
    body.put("b","bv");
    body.put("c","");

    String sign=signature.sign(body);
```