package org.ironlegion.Listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.ironlegion.IronLegionBot;

import java.util.EnumSet;
import java.util.Objects;

public class DiscordEventListener extends ListenerAdapter {
    public IronLegionBot bot;
    public TextChannel textChannel;
    public boolean done = false;


    //IronLegion Discord:
    public long applyCategory = 1404176814196391997L;  // CATEGORY ID from Loan channel
    public long applyChannelID = 1404176897256067122L;        // Channel ID Guild-Bridge
    public long guildID = 847516508183461912L;              // ID FROM SERVER
    private long admRole = 852410152657354772L;                // ID From a special USER

    public DiscordEventListener(IronLegionBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        registerCommands(bot.getJDA());
        bot.getJDA().getShards().forEach(jda -> jda.addEventListener(new ButtonListener()));
    }

    private void registerCommands(@NotNull ShardManager jda) {
        Guild g = jda.getGuildById(guildID); // Replace this with the ID of your own server.

        if (g != null) {
            CommandListUpdateAction commands = g.updateCommands();
            //commands.addCommands(Commands.slash("log","Logs Loan into a File (Archiving)").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("createapplymessage","Uses the current open Channel as a Apply Channel and sends the Message for the Users").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("createchanneltest","test").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();

        }
        /**
         * guild.updateCommands().addCommands(
         *         Commands.slash("echo", "Repeats messages back to you.")
         *             .addOption(OptionType.STRING, "message", "The message to repeat.")
         *             .addOption(OptionType.INTEGER, "times", "The number of times to repeat the message.")
         *             .addOption(OptionType.BOOLEAN, "ephemeral", "Whether or not the message should be sent as an ephemeral message."),
         *         Commands.slash("animal", "Finds a random animal")
         *              .addOptions(
         *                  new OptionData(OptionType.STRING, "type", "The type of animal to find")
         *                      .addChoice("Bird", "bird")
         *                      .addChoice("Big Cat", "bigcat")
         *                      .addChoice("Canine", "canine")
         *                      .addChoice("Fish", "fish")
         *              )
         * ).queue();
         */
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        //if (event.getName().equals("maketimedmessage")) {
        //    createModel(event);
        //}

        String eventName = event.getName();
        switch (eventName) {
            case "createapplymessage":
                sendMessage(event);
                break;
            case "createchanneltest":
                test(event);
                break;
        }

    }

    private void test(SlashCommandInteractionEvent event) {
        ShardManager jda = bot.getJDA();
        Guild g = jda.getGuildById(guildID);
        assert g != null;

        String ign = "RealKazz";

        TextChannel newChannel = g.createTextChannel(ign,bot.getJDA().getCategoryById(applyCategory))
                .addPermissionOverride(g.getPublicRole(),null, EnumSet.of(Permission.VIEW_CHANNEL))
                .addPermissionOverride(
                        g.getRoleById(admRole), EnumSet.of(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_SEND,
                                Permission.MESSAGE_HISTORY
                        ),
                        null)
                .addPermissionOverride(event.getMember(), EnumSet.of(
                                Permission.VIEW_CHANNEL,
                                Permission.MESSAGE_SEND,
                                Permission.MESSAGE_HISTORY
                        ),
                        null)
                .complete();

        newChannel.sendMessage("Test").queue();
        event.reply("Created Channel").queue();
    }

    private void sendMessage(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessage("Apply Test, React to open a Application")
                .setActionRow(
                        Button.primary("apply","Apply!")
                ).queue();
    }


    public void createModel(@NotNull SlashCommandInteractionEvent event) {
        /*
        TextInput msg = TextInput.create("msg", "Message", TextInputStyle.SHORT)
               .setPlaceholder("Example Message") //  setRequiredRange(10, 100)
               .build();

        TextInput time = TextInput.create("time", "Time between sends (formating important)", TextInputStyle.SHORT)
                .setPlaceholder("4 hours, 20 minutes, 2 days")
                .build();

        Modal modal = Modal.create("tmsg", "TimedMessage")
                .addComponents(ActionRow.of(msg), ActionRow.of(time))
                .build();

        event.replyModal(modal).queue();

         */
    }

/*
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();



    }

 */

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        ShardManager jda = bot.getJDA();
        Guild g = jda.getGuildById(guildID);
        assert g != null;

        if(event.getModalId().equals("apply")) {
            String ign = Objects.requireNonNull(event.getValue("ign")).getAsString();




            TextChannel newChannel = g.createTextChannel(ign,bot.getJDA().getCategoryById(applyCategory))
                    .addPermissionOverride(g.getPublicRole(),null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .addPermissionOverride(
                            g.getRoleById(admRole), EnumSet.of(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_SEND,
                                    Permission.MESSAGE_HISTORY
                            ),
                            null)
                    .addPermissionOverride(event.getMember(), EnumSet.of(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_SEND,
                                    Permission.MESSAGE_HISTORY
                            ),
                            null)
                    .complete();


        }

/*
        if (event.getModalId().equals("tmsg")) {

            String msg = Objects.requireNonNull(event.getValue("msg")).getAsString();
            String time = Objects.requireNonNull(event.getValue("time")).getAsString();
            Pattern pattern = Pattern.compile("(\\d+)\\s*(\\w+)");
            Matcher matcher = pattern.matcher(time);

            if (matcher.find()) {
                int number = Integer.parseInt(matcher.group(1)); // This will capture the number (e.g., "4")
                String unit = matcher.group(2);   // This will capture the unit (e.g., "days")

                // Output the captured groups
                //textChannel.sendMessage("Number: " + number + " | Unit: " + unit).queue();

                TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                switch(unit.toLowerCase()){
                    case "minutes": case "minute":
                        timeUnit = TimeUnit.MINUTES;
                        break;

                    case "hours": case "hour":
                        timeUnit = TimeUnit.HOURS;
                        break;

                    case "days": case "day":
                        timeUnit = TimeUnit.DAYS;
                        break;

                    case "seconds": case "second":
                        timeUnit = TimeUnit.SECONDS;
                        break;
                }

                try {
                    repeatedMessage(msg,number,timeUnit);
                    event.reply("Created Timed Message. Message: "+ msg +", Time:"+ time + ", Unit: " + timeUnit.name() + ", group2: " + unit).queue();
                } catch (LoginException e) {
                    throw new RuntimeException(e);
                }


            }


        }

 */
    }


}