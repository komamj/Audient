# 功能列表
=========
## 定位相关
>   根据当前定位返回可以点歌的地点。（距离当前定位1以内）km（前期可考虑服务端直接返回一个列表包含所有的门店）
>   根据城市id返回当前城市所有的可以点歌的地点。
## 注册与登录
-----------
>   保存与验证用户名和密码，每个用户对应唯一id。
>   考虑使用微信或qq登录。
## 用户
>   用户的id，用户名，用户密码，头像地址，剩余金币，已点歌曲数目以及包含已点歌曲的列表。
>   获取用户已经点过的歌曲。
## 音乐数据来源
-------------
>   获取当前正在播放的队列。
>   获取推荐歌曲（榜单歌曲）。
>   获取对应歌曲id的歌词。
## 评论
------
>   提交对应歌曲id的评论至服务器。
>   获取对应歌曲id的评论。
### 搜索
-------
>   根据keyword（关键字可以是歌曲名，歌手名，专辑名字...）搜索音乐。
## 收藏
------
>   将用户收藏的歌曲提交至服务器。
>   获取对应用户已收藏歌曲。
## 试听
------
>   获取对应音乐id的试听地址。
>   获取对应音乐id的专辑图片地址。
>   获取对应音乐id的时长。
>   获取对应音乐id的歌曲名字和歌手名字。
## 点歌
------
>   提交当前歌曲id至服务器，服务器自动插入当前的播放队列并进行扣费(服务器需要验证是否可以加入当前播放队列，比如点的歌和当前正在播放的歌曲一样)。
>   当播放队列改变时通知客户端（手动切歌，播放完成自动切歌，新的歌曲加入播放队列）
### 统计
------
>   提交当前歌曲id至服务器，服务器统计当前歌曲id的点播次数加1。
### 支付
>   将用户充值金币post到服务器。
>   考虑是否需要服务器验证之类的（前期考虑用微信企业支付）。
### 播放
------
>   提交相应播放命令至服务器（切歌），（考虑开始播放是否由客户断控制，建议由服务器控制）。
## 服务器返回格式
>   统一返回json格式。