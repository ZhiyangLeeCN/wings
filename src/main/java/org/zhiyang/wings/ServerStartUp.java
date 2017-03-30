package org.zhiyang.wings;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.Socks5Server;
import org.zhiyang.wings.socks.SocksServer;
import org.zhiyang.wings.socks.SocksServerConfig;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * @author lizhiyang.
 */
public class ServerStartUp {

    private static Options buildSocksServerOptions()
    {
        Options options = new Options();

        //可选，指定socks(yaml)配置文件的位置
        options.addOption(new Option("c", true,"socks server config(yaml)"));

        //可选，打印配置内容
        options.addOption(new Option("p", false, "print socks server config"));

        return options;

    }

    public static void main(String[] args) {

        CommandLine commandLine = $.parseCommandLine(buildSocksServerOptions(), args);
        if (commandLine == null) {

            $.die("parse cli args error!");

        } else {

            SocksServerConfig socksServerConfig = new SocksServerConfig();
            if (commandLine.hasOption("c")) {

                String path = commandLine.getOptionValue("c");
                $.msg("loading:" + path);
                SocksServerConfig loadedSocksServerConfig = $.loadYamlConfig2Object(
                        commandLine.getOptionValue("c"),
                        SocksServerConfig.class);

                if (loadedSocksServerConfig == null) {
                    $.die("Load socks server file error ! check file is found");
                } else {
                    socksServerConfig = loadedSocksServerConfig;
                }

            }

            if (commandLine.hasOption("p")) {

                //打印SOCKS服务配置
                $.msg("--------------socks config--------------");
                $.msg(socksServerConfig.toString());
                $.msg("----------------------------------------");

                //打印日志配置
                $.msg("--------------log config----------------");
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                StatusPrinter.print(loggerContext);
                $.msg("----------------------------------------");

            }

            try {

                if (socksServerConfig.isSslForDispatch()) {

                    socksServerConfig.setSslContextForDispatch(
                            SslContextBuilder.forClient()
                                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                    .build());

                }

                if (socksServerConfig.isSslForServer()) {

                    File sslServerKeyCertChainFile = new File(
                            socksServerConfig.getSslServerKeyCertChainFilePath());
                    File sslServerPrivateKeyFile = new File(
                            socksServerConfig.getSslServerPrivateKeyFilePath());

                    if (!sslServerKeyCertChainFile.exists()) {
                        $.die("ssl server KeyCertChain file not exists!");
                    }

                    if (!sslServerPrivateKeyFile.exists()) {
                        $.die("ssl server private key file not exists!");
                    }

                    SslContextBuilder sslContextBuilder = null;
                    if (socksServerConfig.getSslServerPrivateKeyPassword().isEmpty()) {
                        sslContextBuilder = SslContextBuilder.
                                forServer(sslServerKeyCertChainFile, sslServerPrivateKeyFile);
                    } else {
                        sslContextBuilder = SslContextBuilder.forServer(
                                sslServerKeyCertChainFile,
                                sslServerPrivateKeyFile,
                                socksServerConfig.getSslServerPrivateKeyPassword());
                    }

                    socksServerConfig.setSslContextForServer(sslContextBuilder.build());

                }

            } catch (SSLException e) {

                $.die("build SslContext error", e);

            }

            final SocksServer socksServer = new Socks5Server(socksServerConfig);
            socksServer.start();

        }

    }

}
