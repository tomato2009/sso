> - ## 三、环境要求
>
> - JDK1.7+
>
> - Maven3.3
>
> - Eclipse/IDEA
>
>   ## 四、准备工作
>
>   因为我们主要讲的是跨域的单点登录,所以我们需要把不同项目部署到不同域名下.不可能为了完成这个代码,让同学们去阿里云买三台主机,映射三个IP.所以我们的实验就在本机来实现.我们需要修改host文件,让三个域名映射到本机.
>
>   ​
>
>   host文件存放的位置:
>
>   ```
>   C:\Windows\System32\drivers\etc
>   ```
>
>   ​
>
>   打开host文件之后,在最后追加如下配置:
>
>   ```
>   127.0.0.1 www.sso.com
>   127.0.0.1 www.crm.com
>   127.0.0.1 www.wms.com
>   ```
>
>   这段配置的意思是,我们在浏览器中输入:
>
>   ​
>
>   ```
>   http://www.sso.com
>   ```
>
>   ```
>   http://www.crm.com
>   ```
>
>   ```
>   http://www.wms.com
>   ```
>
>   ​
>
>   其实访问的都是本机:
>
>   ```
>   127.0.0.1
>   ```

PS:有些同学打开这个文件之后,保存的时候可能被拒绝.原因可能是权限不够.解决方法:把host文件拷贝到桌面(有权限的地方即可),修改好之后再把:`C:\Windows\System32\drivers\etc`的host文件覆盖.



客户端没有使用Spring框架.使用Servlet3.0

```
@WebServlet(name = "mainServlet", urlPatterns = "/main")
```


项目完全拷贝与：

https://blog.csdn.net/wolfcode_cn/article/details/80773274

https://github.com/javalanxiongwei



测试的过程中发现有个漏洞：就是crm客户端登录后得到一个url

 http://www.crm.com:8088/main?token=b4416a94-600c-4c4c-a7b7-7db8b37bdc56 

别人可以伪造这个token直接在另一台机器上访问该url就可以直接登录到A客户端而不需要用户名密码了。



我实际应用到项目中的修改优化：

sso服务器端在每个客户端登录的时候生成一个临时的token，verify完成后迅速删除(最合理的方式是给每个临时token一个有效时长，时间极短，根据实际测试效果来给个经验值)，而不是用全局的token，很危险。