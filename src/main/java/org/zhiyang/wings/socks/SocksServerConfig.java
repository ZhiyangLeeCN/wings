package org.zhiyang.wings.socks;

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
    private boolean dispatchUseSocksv5 = false;

    /**
     * 转发请求使用的SocksV5代理的地址
     */
    private String dispatchSocksv5Address = "localhost";

    /**
     * 转发请求使用的SocksV5代理的端口
     */
    private int dispatchSocksv5Port = 1080;

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

    public boolean isDispatchUseSocksv5() {
        return dispatchUseSocksv5;
    }

    public void setDispatchUseSocksv5(boolean dispatchUseSocksv5) {
        this.dispatchUseSocksv5 = dispatchUseSocksv5;
    }

    public String getDispatchSocksv5Address() {
        return dispatchSocksv5Address;
    }

    public void setDispatchSocksv5Address(String dispatchSocksv5Address) {
        this.dispatchSocksv5Address = dispatchSocksv5Address;
    }

    public int getDispatchSocksv5Port() {
        return dispatchSocksv5Port;
    }

    public void setDispatchSocksv5Port(int dispatchSocksv5Port) {
        this.dispatchSocksv5Port = dispatchSocksv5Port;
    }

    @Override
    public String toString() {
        return "[port=" + port + ", connectionIdleTimeSeconds=" + connectionIdleTimeSeconds +
                ", connectTimeOutMillis=" + connectTimeOutMillis + ", serverSelectorThreads=" +
                serverSelectorThreads + ", dispatchUseSocksv5=" + dispatchUseSocksv5 +
                ", dispatchSocksv5Address=" + dispatchSocksv5Address + ", dispatchSocksv5Port=" +
                dispatchSocksv5Port + "]";
    }
}
