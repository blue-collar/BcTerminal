具备以下功能：
1、数据库存储[可以不自己直接封装SQLite，采用DBFlow 或者 GreenDao]
2、文件存储
3、可以将sp的存储操作都统一放这里

注意做到：
1.支持存储I/O加解密
2.支持存储I/O序列化及反序列化
3.支持多文件存储方式，支持并发