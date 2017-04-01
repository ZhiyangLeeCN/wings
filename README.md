[wings - 支持加密的Socks服务](http://github.com/baidu/bfs)
======

<img src="https://travis-ci.org/ZhiyangLeeCN/wings.svg?branch=master">

wings是一个使用Java + netty实现的轻量级Socks5版本，支持使用SSL加密传输。

## 特性
1. 支持启用SSL
            *使用SSL加密传输，防止中间人拦截篡改原始数据
2. 简单易用
            *只需少量配置即可启动一个属于自己的Socks5服务
3. 轻量级实现
            *可以在该源代码基础上实现更加复杂的定制需求

## 结构说明
该源代码使用Java8编写并基于maven构建，所以你需要安装maven和Java8，如何安装maven以及Java可以参考网上教程，构建后会在target目录中产生一个名为
wings-1.0-SNAPSHOT-shaded.jar的文件，该jar可以当本地客户端也可以当服务端部署(使用配置文件来设置运行模式)，一般会在服务端部署该jar文件以服务端模式运行，
然后在本地以客户端模式运行，用户在填写socks代理地址的时候使用本地客户端所监听的地址即可。


## 快速体验

#### 构建源代码(需要安装Maven)
    mvn package

#### 创建服务端配置文件(server.yaml)
    ```
    #监听端口
    port: 1080

    #处理socks连接的线程数量(推荐 cpu数量 x 4)
    serverSelectorThreads: 3

    ```
#### 在服务端启动socks服务
    nohup java -jar wings-1.0-SNAPSHOT-shaded.jar -p -c server.yaml >> /dev/null 2>&1 &


#### 创建本地客户端配置文件(client.yaml)
    ```
     #监听端口
     port: 1080

     #是否将本机的请求转发到服务器端的socks服务
     dispatchUseSocksV5: true

     #运行在服务器端的socks服务的IP地址
     dispatchSocksV5Address: "localhost"

     #运行在服务器端的socks服务的端口
     dispatchSocksV5Port: 1080


    ```

### 在本地机器上启动客户端的socks转发服务
    //windows(cmd)
    java -jar wings-1.0-SNAPSHOT-shaded.jar -p -c client.yaml

    //Mac\Linux\UNIX
    nohup java -jar wings-1.0-SNAPSHOT-shaded.jar -p -c client.yaml >> /dev/null 2>&1 &


### 在火狐或者谷歌中使用
    设置浏览器的socks代理时，socks地址为127.0.0.1, 端口为你在client中填写的监听端口号



## 启用SSL加密传输

wings的SSL采用的是单向验证，需要使用openssl生成自签名证书，并且server和client的配置文件都需要做出小部分对应的修改。

#### 生成自签名证书

//socks.key(输出的私钥文件) socks.crt(输出的证书文件) 2048位长度目前认为不可被暴力破解
openssl req -newkey rsa:2048 -x509 -days 365 -keyout socks.key -out socks.crt

//输入以上命令会提示你设置私钥文件的密码，提示如下
Enter PEM pass phrase:
//当你输入后会提示你再输入一次
Verifying - Enter PEM pass phrase:
//之后会让你输入国家以及个人信息等资料，因为是自签名，随便填写回车下去生成即可
//成功后会在你当前执行命令的目录下生成2个文件，socks.key和socks.crt

### 将私钥文件由rsa转换为PCKS8格式
openssl pkcs8 -topk8 -inform PEM -in socks.key -outform PEM -out socks_pkcs8.key

//输入以上命令会先让你输入私钥文件的访问密码(你执行前面命令所设置的)
Enter pass phrase for socks.key:
//然后会让你设置转换后新输出的秘钥文件的密码
Enter Encryption Password:
//完成后，会在你执行命令的目录下生成了一个socks_pkcs8.key文件，由socks.key转换而来

#### 修改服务端配置文件新增以下选项(server.yaml)

    ```
    #SOCKS服务端启用SSL
    sslForServer: true

    #你前面执行命令生成的证书文件(socks.crt)的路径
    sslServerKeyCertChainFilePath: "/xxx/socks.crt"

    #你转换成PKS8格式的私钥文件路径(socks_pkcs8.key)
    sslServerPrivateKeyFilePath: "/xxx/socks_pkcs8.key"

    #你为转换成PKS8格式的私钥文件所设置的密码
    sslServerPrivateKeyPassword: "xxxxx"

    ```

#### 修改客户端配置文件新增以下选项(client.yaml)

    ```
    #客户端启用SSL
    sslForDispatch: false

    ```

然后分别在服务器上和本机上重新启动wings即可

## 联系我
zhiyanglee@foxmail.com

