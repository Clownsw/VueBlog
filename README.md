# VueBlog
一个极简风格的个人博客系统。

## 特点
- 高性能
- 低占用
- 易使用
- 高稳定

## 技术架构
旧版: Rust + Actix Web + Sqlx + MySQL + Redis + Vue.js

新版: Java + Spring + Armeria + MybatisPlus + MySQL + Redis + Vue.js

## 功能
- 文章管理
	- 普通文章
	- 加密文章
- 分类管理
- 标签管理
- 歌单管理(音乐系统)
- 评论系统(Vuline + LeanCloud)
- 关于我
- 备份管理(将数据库备份到又拍云OSS)
- 系统管理

## 音乐系统

- [VueBlogNeteaseCloudMusicApi](https://github.com/Clownsw/VueBlogNeteaseCloudMusicApi)

## 部署

### 数据库

```bash
docker run -it -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=You PassWord -v/root/mysql:/var/lib/mysql mysql:8
```



### Nginx

```bash
docker run -it -d --name nginx -p 80:80 -v /root/nginx/html:/usr/share/nginx/html -v /root/nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf nginx
```



### VueBlog-Before

#### 构建

```bash
docker build -t smilex1/vueblog-before:0.11-dev -f Dockerfile .
```

#### 运行

```bash
docker rm --force vueblog-before && docker run -it -d --name vueblog-before --cap-add=SYS_PTRACE -p 8888:8888 -v ${PWD}:/app/conf/ -e JAVA_MEM_OPTIONS="-XX:ParallelGCThreads=6 -XX:ConcGCThreads=6 -XX:G1HeapRegionSize=4M -Xmx512M" -e VUEBLOG_CONFIG_PATH="/app/conf/vueblog-config.json" smilex1/vueblog-before:0.11-dev
```



### VueBlog-After

#### 构建

```bash
docker build -t smilex1/vueblog-after:0.11-dev -f Dockerfile .
```

#### 运行

```bash
docker rm --force vueblog-after && docker run -it -d --name vueblog-after --cap-add=SYS_PTRACE -p 9999:9999 -v ${PWD}:/app/conf/ -e JAVA_MEM_OPTIONS="-XX:ParallelGCThreads=6 -XX:ConcGCThreads=6 -XX:G1HeapRegionSize=4M -Xmx512M" -e VUEBLOG_CONFIG_PATH="/app/conf/vueblog-config.json" smilex1/vueblog-after:0.11-dev
```



## example
[Smilex' Blog](https://www.smilex.vip)
