# Redis Distributed Lock

`Redisson` distributed lock that relies on `Redis`.

## use

### create the `redisson` client

```java 
  Config config=new Config();
  config.useSingleServer().setAddress("redis://xxxx:xxxx").setPassword("xxxxxx");
  RedissonClient redissonClient=Redisson.create(config);
```

### using redis distructed lock

```java
  DistributedLock lock = new RedisDistributedLock(client,"lock_key");
    lock.lock();
  try {
  ...
  } finally {
    lock.unLock();
  }
```