package org.zhiyang.wings.socks;

import io.netty.handler.ssl.SslContext;

/**
 * @author lizhiyang
 */
public class SocksServerConfig {

    /**
     *  Socks监听的端口
     */
    private int port = 1080;

    /**
     * Socks维持的连接允许最大闲置时间
     */
    private int connectionIdleTimeSeconds = 60;

    /**
     * Socks连接转发目标服务超时时间
     */
    private int connectTimeOutMillis = 1000;

    /**
     * Socks负责selector的线程数量
     */
    private int serverSelectorThreads = 3;

    /**
     * 转发请求的时候是否使用SocksV5代理
     */
    private boolean dispatchUseSocksV5 = false;

    /**
     * 转发请求使用的SocksV5代理的地址
     */
    private String dispatchSocksV5Address = "localhost";

    /**
     * 转发请求使用的SocksV5代理的端口
     */
    private int dispatchSocksV5Port = 1080;

    /**
     * Socks服务端监听是否启用SSL
     */
    private boolean sslForServer = false;

    /**
     * Socks服务端的X.509证书文件路径(PEM格式)
     */
    private String sslServerKeyCertChainFilePath = "";

    /**
     * Socks服务端的私钥(PKCS#8)文件路径(PEM格式)
     */
    private String sslServerPrivateKeyFilePath = "";

    /**
     * Socks服务端的私钥文件访问密码
     */
    private String sslServerPrivateKeyPassword = "";

    /**
     * 根据证书和秘钥构建的SSL上下文对象
     */
    private SslContext sslContextForServer = null;

    /**
     * 为转发所构建的SSL上下文(forClient)
     */
    private SslContext sslContextForDispatch = null;

    /**
     * 是否为转发启用SSL
     */
    private boolean sslForDispatch = false;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectionIdleTimeSeconds() {
        return connectionIdleTimeSeconds;
    }

    public void setConnectionIdleTimeSeconds(int connectionIdleTimeSeconds) {
        this.connectionIdleTimeSeconds = connectionIdleTimeSeconds;
    }

    public int getConnectTimeOutMillis() {
        return connectTimeOutMillis;
    }

    public void setConnectTimeOutMillis(int connectTimeOutMillis) {
        this.connectTimeOutMillis = connectTimeOutMillis;
    }

    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public boolean isDispatchUseSocksV5() {
        return dispatchUseSocksV5;
    }

    public void setDispatchUseSocksV5(boolean dispatchUseSocksV5) {
        this.dispatchUseSocksV5 = dispatchUseSocksV5;
    }

    public String getDispatchSocksV5Address() {
        return dispatchSocksV5Address;
    }

    public void setDispatchSocksV5Address(String dispatchSocksV5Address) {
        this.dispatchSocksV5Address = dispatchSocksV5Address;
    }

    public int getDispatchSocksV5Port() {
        return dispatchSocksV5Port;
    }

    public void setDispatchSocksV5Port(int dispatchSocksV5Port) {
        this.dispatchSocksV5Port = dispatchSocksV5Port;
    }

    public boolean isSslForServer() {
        return sslForServer;
    }

    public void setSslForServer(boolean sslForServer) {
        this.sslForServer = sslForServer;
    }

    public String getSslServerKeyCertChainFilePath() {
        return sslServerKeyCertChainFilePath;
    }

    public void setSslServerKeyCertChainFilePath(String sslServerKeyCertChainFilePath) {
        this.sslServerKeyCertChainFilePath = sslServerKeyCertChainFilePath;
    }

    public String getSslServerPrivateKeyFilePath() {
        return sslServerPrivateKeyFilePath;
    }

    public void setSslServerPrivateKeyFilePath(String sslServerPrivateKeyFilePath) {
        this.sslServerPrivateKeyFilePath = sslServerPrivateKeyFilePath;
    }

    public String getSslServerPrivateKeyPassword() {
        return sslServerPrivateKeyPassword;
    }

    public void setSslServerPrivateKeyPassword(String sslServerPrivateKeyPassword) {
        this.sslServerPrivateKeyPassword = sslServerPrivateKeyPassword;
    }

    public boolean isSslForDispatch() {
        return sslForDispatch;
    }

    public void setSslForDispatch(boolean sslForDispatch) {
        this.sslForDispatch = sslForDispatch;
    }

    public SslContext getSslContextForServer() {
        return sslContextForServer;
    }

    public void setSslContextForServer(SslContext sslContextForServer) {
        this.sslContextForServer = sslContextForServer;
    }

    public SslContext getSslContextForDispatch() {
        return sslContextForDispatch;
    }

    public void setSslContextForDispatch(SslContext sslContextForDispatch) {
        this.sslContextForDispatch = sslContextForDispatch;
    }

    @Override
    public String toString() {
        return "[port=" + port + ", connectionIdleTimeSeconds=" + connectionIdleTimeSeconds +
                ", connectTimeOutMillis=" + connectTimeOutMillis + ", serverSelectorThreads=" +
                serverSelectorThreads + ", dispatchUseSocksV5=" + dispatchUseSocksV5 +
                ", dispatchSocksV5Address=" + dispatchSocksV5Address + ", dispatchSocksV5Port=" +
                dispatchSocksV5Port + "]";
    }
}
