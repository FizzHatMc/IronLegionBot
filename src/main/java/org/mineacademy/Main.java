package org.mineacademy;

import org.apache.commons.cli.*;

public class Main {



    public static void main(String[] args) {
        Options options = new Options();

        options.addOption(new Option("h", "help", false, "Displays this help menu."));
        options.addOption(new Option("t", "token", true, "Provide the token during startup."));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            // Check if the help argument was provided.
            if (cmd.hasOption("help")) {
                formatter.printHelp("Help Menu", options);
                System.exit(0);
            }

            // Check if the token argument was provided and has a value. If it doesn't, return null.
            String token = cmd.hasOption("token") ? cmd.getOptionValue("token") : null;
            token = "MTI3ODAyMjkxODM3NDQyODY5Mw.G9Q7pH.m95E8gfk-atJ2B9FblYSTl26kymIj_DjD5qZIY";             // IronLegionBot Token

            if (token == null) {
                System.out.println("ERROR: No token provided, please provide a token using the -t or --token flag.");
                formatter.printHelp("", options);
                System.exit(0);
            }

            // If it passes through everything, it starts the bot and sends the token to our second class.
            MineAcademyBot.selfBot = new MineAcademyBot(token);     //  <-----------------------------------------------
            //BOT START LINE (44)
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("", options);
            System.exit(0);
        }

        




    }
}