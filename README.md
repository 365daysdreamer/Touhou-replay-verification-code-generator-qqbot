<div align="center">

# 东方STG游戏回放验证码生成器

![](https://img.shields.io/github/languages/top/365daysdreamer/Touhou-replay-verification-code-generator-qqbot "语言")
[![](https://img.shields.io/github/actions/workflow/status/365daysdreamer/Touhou-replay-verification-code-generator-qqbot/build.yml?branch=main)](https://github.com/365daysdreamer/Touhou-replay-verification-code-generator-qqbot/actions/workflows/build.yml "代码分析")
[![](https://img.shields.io/github/contributors/365daysdreamer/Touhou-replay-verification-code-generator-qqbot)](https://github.com/365daysdreamer/Touhou-replay-verification-code-generator-qqbot/graphs/contributors "贡献者")
[![](https://img.shields.io/github/license/365daysdreamer/Touhou-replay-verification-code-generator-qqbot)](https://github.com/365daysdreamer/Touhou-replay-verification-code-generator-qqbot/blob/main/LICENSE "许可协议")
</div>

本项目使用了[东方Project沙包聚集地Q群（以下简称“红群”）机器人](https://github.com/CuteReimu/tfcc-bot-mirai-console)的框架和大部分代码，基于 [Mirai](https://github.com/mamoe/mirai) 编写

## 声明

* 本项目采用`AGPLv3`协议开源。同时**强烈建议**各位开发者遵循以下原则：
    * **任何间接接触本项目的软件也要求使用`AGPLv3`协议开源**
    * **不鼓励，不支持一切商业用途**
* **由于使用本项目提供的接口、文档等造成的不良影响和后果与本人无关**
* 由于本项目的特殊性，可能随时停止开发或删档
* 本项目为开源项目，不接受任何的催单和索取行为

## 编译

```shell
./gradlew buildPlugin
```

在`build/mirai`下可以找到编译好的jar包，即为Mirai插件

## 使用方法

1. 首先了解、安装并启动 [Mirai - Console Terminal](https://github.com/mamoe/mirai/blob/dev/docs/ConsoleTerminal.md) 。
   如有必要，你可能需要修改 `config/Console` 下的 Mirai 相关配置。
   **QQ登录、收发消息相关全部使用 Mirai 框架，若一步出现了问题，请去Mirai的repo或者社区寻找解决方案。**
2. 启动Mirai后，会发现生成了很多文件夹。将编译得到的插件jar包放入 `plugins` 文件夹后，重启Mirai。

## 指令列表及说明

- @机器人<br>
  获取可用指令列表

- 指令说明 <指令名>

- 增加管理员 <@某人|QQ号>

- 删除管理员 <@某人|QQ号>

- 查看管理员

- 随机操作 <标签><br>
  获得一串由[↑, ↓, ←, →]组成的随机方向，玩家需要在直播+实录的同时，在关底boss对话过程中通过方向键输入进游戏里<br>
  在配置文件里可以设置验证码的长度，默认为10位验证码<br>
  标签可以用来描述验证码的用途，也可以不填<br>
  标签不能包含空格，且长度默认限制为20字符<br>
  生成相同标签的随机操作时会覆盖原有的记录

- 删除记录 <标签><br>
  删除自己申请的随机操作记录，不能删除别人的<br>
  支持同时删除多个记录，用空格隔开<br>
  参数不能为空

- 清空记录 <@某人|QQ号><br>
  清空该用户申请的全部随机操作记录，参数为空则是清除自己的，只有管理员才能清除别人的操作记录<br>
  支持同时清除多位用户的记录，用空格隔开

- 修改标签 <旧标签> <新标签><br>
  修改随机操作记录的标签，只能修改自己的<br>
  旧标签必须指定，新标签可以不填，会自动生成<br>
  标签不能包含空格，且长度默认限制为20字符<br>
  修改为已存在的标签时会覆盖原有的记录

- 查询记录 <@某人|QQ号><br>
  查询该用户申请的随机操作记录，参数为空则是查询自己的<br>
  为了防止刷屏，可以设置查询时显示验证码个数的限制，默认显示最后10条验证码<br>
  支持同时查询多位用户的记录，用空格隔开

- 查询全部记录 <@某人|QQ号><br>
  查询该用户申请的全部随机操作记录，参数为空则是查询自己的，只有管理员才能进行此操作<br>
  支持同时查询多位用户的记录，用空格隔开

## 配置文件：

第一次运行会自动生成配置文件`config/org.stg.verification.bot/TRVGConfig.yml`，如下：

```yaml
# QQ相关配置
qq:
  super_admin_qq: 12345678 # 主管理员QQ号
  qq_group: # 主要功能的QQ群
    - 12345678
# 随机操作的次数配置
random_operation:
  number: 10 # 一个验证码包含的随机操作的次数
  query_limit: 10 # 查询时显示验证码个数的限制
  tag_limit: 20 # 标签的长度限制
# 指令冷却配置
cooldown:
  time: 30 # 指令冷却时间
  command_with_cd: # 需要冷却的指令
    - 查看管理员
    - 随机操作
    - 查询记录
    - 查询全部记录
# 指令冷却提示
# $quote: 引用消息
# $cmd: 指令名
# $cd: 冷却时间
cooldown_msg: '$quote $cmd 太快了，请 $cd 秒后再试'
```

修改配置文件后重新启动即可

## 开发相关

如果你想要本地调试，执行如下命令即可：

```shell
./gradlew runConsole
```

上述命令会自动下载Mirai Console并运行，即可本地调试。本地调试时会生成一个`debug-sandbox`文件夹，和Mirai Console的文件夹结构基本相同
