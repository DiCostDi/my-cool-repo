package com.aposisi.lab1;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String HELP = "\n-h --help\tto print info\n -p --port\tto set custom port\n";

    public static void main(String... args) {
        int port = 1337;
        List<String> commands = Arrays.stream(args)
                .filter(x -> x.startsWith("-") | x.startsWith("--"))
                .collect(Collectors.toList());
        if (!commands.isEmpty()){
            Optional<String> helpCommandOptional = commands.stream()
                    .filter(x -> x.startsWith("-h") || x.startsWith("--help"))
                    .findFirst();
            if (helpCommandOptional.isPresent()){
                logger.log(Level.INFO, HELP);
                return;
            }
        }
    
        try {
            ServerSocket serverConnect = new ServerSocket(port);
            logger.log(Level.INFO, "Server started. Listening for connections on port : " + port);

            while (true) {
                HttpServer myServer = new HttpServer(serverConnect.accept());

                logger.log(Level.INFO,"Connection opened. (" + new Date() + ")");

                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            logger.log(Level.ERROR,"Server Connection error : " + e.getMessage());
        }
    }


}
