package org.zhiyang.wings.socks;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author lizhiyang
 */
public class SocksServerConfigTest {

    @Test
    public void testGetSet()
    {
        SocksServerConfig config = new SocksServerConfig();
        config.setPort(1086);
        config.setConnectionIdleTimeSeconds(60);
        config.setConnectTimeOutMillis(1000);
        config.setServerSelectorThreads(3);
        config.setDispatchUseSocksV5(true);
        config.setDispatchSocksV5Address("localhost");
        config.setDispatchSocksV5Port(1080);
        config.setSslForServer(true);
        config.setSslForDispatch(true);
        config.setSslServerKeyCertChainFilePath("/key/keyFile");
        config.setSslServerPrivateKeyFilePath("/key/privateKeyFile");
        config.setSslServerPrivateKeyPassword("password");

        assertEquals(1086, config.getPort());
        assertEquals(60, config.getConnectionIdleTimeSeconds());
        assertEquals(1000, config.getConnectTimeOutMillis());
        assertEquals(3, config.getServerSelectorThreads());
        assertEquals(true, config.isDispatchUseSocksV5());
        assertEquals("localhost", config.getDispatchSocksV5Address());
        assertEquals(1080, config.getDispatchSocksV5Port());
        assertEquals(true, config.isSslForServer());
        assertEquals(true, config.isSslForDispatch());
        assertEquals("/key/keyFile", config.getSslServerKeyCertChainFilePath());
        assertEquals("/key/privateKeyFile", config.getSslServerPrivateKeyFilePath());
        assertEquals("password", config.getSslServerPrivateKeyPassword());

    }

}