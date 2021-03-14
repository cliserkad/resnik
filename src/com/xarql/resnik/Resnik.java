package com.xarql.resnik;

import com.xarql.smp.GenericParser;
import com.xarql.smp.ParseData;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Resnik implements Runnable {

    public static final String CONFIG = "config";
    public static final String BOT_INFO = "info";
    public static final String TEST_PREFIX = "test-";
    public static final String TOKEN = "token";

    public static void main(String[] args) throws IOException {
        new Resnik().run();
    }

    public Resnik() {

    }

    @Override
    public void run() {
        try {
            ParseData config = loadSmp(CONFIG);
            final String prefix;
            if(config.getBoolean("testing")) {
                prefix = TEST_PREFIX;
                System.out.println("TESTING");
            } else
                prefix = "";
            ParseData priv = loadSmp(TOKEN);
            final String privateKey = priv.getString(prefix + TOKEN);
            GatewayDiscordClient client = DiscordClientBuilder.create(privateKey)
                    .build()
                    .login()
                    .block();



            client.onDisconnect().block();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ParseData loadSmp(String path) throws IOException, URISyntaxException {
        return GenericParser.parse(Files.readString(Path.of(getClass().getResource("/" + path + ".smp").toURI())));
    }

}
