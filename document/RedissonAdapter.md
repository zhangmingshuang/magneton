# Redisson适配器

框架提供了`RedissonAdapter`用来简化`Redisson`的使用。

## 创建RedissonClient

```java
RedissonAdapter.createClient(RedissonClientType.SINGLE);
```

- CLUSTER 集群模式
- MASTER_SLAVE 主从模式
- REPLICATED 云托管模式
- SENTINEL 哨兵模式
- SINGLE 单节点模式

或者，也可以直接使用对应的方法创建对应模式的`RedissonClient`

```java
// 集群模式
RedissonAdapter.createClusterServersClient();
// 主从模式
RedissonAdapter.createMasterSlaveServersClient();
// 云托管模式
RedissonAdapter.createReplicatedServersClient();
// 哨兵模式
RedissonAdapter.createSentinelServersClient();
// 单节点模式
RedissonAdapter.createSingleServerClient();
```

## 读取RedissonClient配置

```java
// 集群模式配置
Config config = RedissonAdapter.getDefaultClusterServersConfig();
// 主从模式配置
Config config = RedissonAdapter.getDefaultMasterSlaveServersConfig();
// 云托管模式配置
Config config = RedissonAdapter.getDefaultReplicatedServersConfig();
// 哨兵模式配置
Config config = RedissonAdapter.getDefaultSentinelServersConfig();
// 单节点模式配置
Config config = RedissonAdapter.getDefaultSingleServerConfig();
```

获取配置之后，可以自定义修改相应的配置信息，然后再创建`RedissonClient`

```java
RedissonAdapter.createRedissonClient(config);
```

## yaml文件配置RedissonClient配置

默认情况下，`RedissonAdapter`会尝试读取`classpath`下的对应模式的`yaml`配置文件：

- CLUSTER 集群模式 `adaptive-redisson-clusterServersConfig.yaml`
- MASTER_SLAVE 主从模式 `adaptive-redisson-masterSlaveServersConfig.yaml`
- REPLICATED 云托管模式  `adaptive-redisson-replicatedServersConfig.yaml`
- SENTINEL 哨兵模式 `adaptive-redisson-sentinelServersConfig.yaml`
- SINGLE 单节点模式 `adaptive-redisson-singleServerConfig.yaml`

如果需要区别环境，可以对`yaml`配置文件添加相应的`profile`前缀，比如`dev`。

`RedissonAdapter`会读取`spring.profiles.active`或者`redisson.adapter.prefix`做为配置的前缀。
