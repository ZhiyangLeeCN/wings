package org.zhiyang.wings;

import com.google.common.base.Charsets;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author lizhiyang.
 */
public class $ {

    public static String baseFileName(@NotNull String filename) {
        return new File(filename).getName();
    }


    public static String hashFileName(@NotNull String filename) {
        return Integer.toString(filename.hashCode());
    }

    @Nullable
    public static String readFile(@NotNull String path) {
        // Don't use line-oriented file read -- need to retain CRLF if present
        // so the style-run and link offsets are correct.
        byte[] content = getBytesFromFile(path);
        if (content == null) {
            return null;
        } else {
            return new String(content, Charset.forName("UTF-8"));
        }
    }

    @Nullable
    public static byte[] getBytesFromFile(@NotNull String filename) {
        try {
            return FileUtils.readFileToByteArray(new File(filename));
        } catch (Exception e) {
            return null;
        }
    }

    public static void closeOnFlush(@NotNull Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void msg(String m) {
        System.out.println(m);
    }

    public static void die(String msg) {
        die(msg, null);
    }


    public static void die(String msg, Exception e) {
        System.err.println(msg);

        if (e != null) {
            System.err.println("Exception: " + e + "\n");
        }

        Thread.dumpStack();
        System.exit(2);
    }

    @Nullable
    public static <T> T loadYamlConfig2Object(@NotNull String path, @NotNull Class<T> clazz)
    {
        String yamlContent = readFile(path);
        if (yamlContent == null) {
            return null;
        } else {
            Constructor constructor = new Constructor(clazz);
            Yaml yaml = new Yaml(constructor);

            return (T)yaml.load(yamlContent);
        }
    }

    @Nullable
    public static CommandLine parseCommandLine(@NotNull Options options, @NotNull String[] args)
    {
        CommandLineParser parser = new DefaultParser();

        try {

            return parser.parse(options, args);

        } catch (ParseException e) {

            return null;

        }
    }

}
