# 更新说明

使用`versions:set`插件进行更新

## step

```cmd
set VERSION=2023.1

mvn versions:set -N -DnewVersion=%VERSION% -f .\pom.xml

mvn versions:set-property -N -DnewVersion=%VERSION% -DallowDowngrade=true -Dproperty=magneton.version -f .\magneton-dependencies\pom.xml

mvn versions:commit

mvn install -DskipTests
```

### 回退版本

`mvn versions:revert`
注意设置generateBackupPoms为true（默认值），
才会有pom.xml.versionsBackup备份文件，
否则没有备份文件无法回退版本号。

### 确认修改过的版本号

`versions:commit`
查看修改后的pom文件，如果没有问题则进行确认，
该命令会删除修改版本号时生成的pom备份文件。