# 1.0.1(2021.10.26)

## fix: 点赞时产生空指针异常。

点赞时调用的是Restful接口，这种接口的modelAndView为null，进而导致messageInterceptor产生空指针异常。

# 1.0(2021.6.23)
## fix
- 修改路径前的斜线
    - view路径前不能带斜线，斜线表示根目录。
    - 分页时setPath需要在路径前加斜线，表示紧跟着域名的绝对路径，而不是相对路径。
- 修改app的镜像，原镜像缺少字体，不能生成验证码。
## feat
- 添加docker compose文件，可以一键部署项目。
