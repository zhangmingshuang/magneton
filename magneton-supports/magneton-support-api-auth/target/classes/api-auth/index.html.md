# api-auth
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
1.0|2020-12-31 10:30|update|author|desc



## 短信注册登录模式.
### 发送短信
**URL:** /${magneton.api.prefix:api}/auth/sms/send

**Type:** POST

**Author:** zhangmsh 15/03/2022

**Content-Type:** application/json; charset=utf-8

**Description:** 发送短信有对应的风控模块，如果返回{@link SmsError#MOBILE_RISK}的状态码， 调用方需要进行风险控制流程的接入。

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
mobile|string|手机号|false|-
risk_type|string|风控类型，可选值：code|false|-
risk_value|string|风控值|false|-

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|string|No comments found.|-
data|object|No comments found. (ActualType: SmsSendReq)|-
└─mobile|string|手机号|-
└─risk_type|string|风控类型，可选值：code|-
└─risk_value|string|风控值|-
message|string|No comments found.|-
timestamp|int64|No comments found.|-
additions|map|No comments found. (ActualType: SmsSendReq)|-
└─any object|object|any object.|-

**Response-example:**
```
{
  "code": "30764",
  "data": {
    "mobile": "970.559.4865",
    "risk_type": "ul4x1p",
    "risk_value": "o14grs"
  },
  "message": "success",
  "timestamp": 1647411829633,
  "additions": {
    "mapKey": {}
  }
}
```

## 错误码列表
Error code |Description
---|---
1000001|PARAM_MOBILE_BLANK 手机号不能为空
1000002|PARAM_MOBILE_ERROR 手机号格式不正确
1000900|MOBILE_RISK 手机号存在风险

