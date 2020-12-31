# Aes

Use the **GCM** model to perform the encryption.

See:

- [MSC61-J. Do not use insecure or weak cryptographic algorithms](https://wiki.sei.cmu.edu/confluence/display/java/MSC61-J.+Do+not+use+insecure+or+weak+cryptographic+algorithms)

## Use
```java
String pwd = UUID.randomUUID().toString();
String encrypt = Aes.encrypt(pwd, data);
String decrypt = Aes.decrypt(pwd, encrypt);
//or
SecretKey secretKey = Aes.generateKey();
String encrypt = Aes.encrypt(secretKey, data);
String decrypt = Aes.decrypt(secretKey, encrypt);
```