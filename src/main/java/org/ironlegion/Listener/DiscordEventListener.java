package org.ironlegion.Listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.ironlegion.IronLegionBot;

import javax.security.auth.login.LoginException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordEventListener extends ListenerAdapter {
    public IronLegionBot bot;
    public TextChannel textChannel;
    public boolean done = false;


    //IronLegion Discord:
    public long categoryGuildZoneId = 876859933835034694L;  // CATEGORY ID from Loan channel
    public long guildBridgeId = 931468999106113577L;        // Channel ID Guild-Bridge
    public long guildID = 847516508183461912L;              // ID FROM SERVER

    /*
     */


    public DiscordEventListener(IronLegionBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        registerCommands(bot.getJDA());

    }

    private void registerCommands(@NotNull ShardManager jda) {
        Guild g = jda.getGuildById(guildID); // Replace this with the ID of your own server.

        if (g != null) {
            CommandListUpdateAction commands = g.updateCommands();
            commands.addCommands(Commands.slash("maketimedmessage", "Creates a Timed Message (Repeats after said time).").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("stoptimedmessage","Stops repeating message").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            //commands.addCommands(Commands.slash("log","Logs Loan into a File (Archiving)").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
        }


    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("maketimedmessage")) {
            createModel(event);
        }

        if (event.getName().equals("stoptimedmessage")) {
            stopScheduledTask();
            event.reply("The scheduled task has been stopped.").queue();
        }
    }


    public void createModel(@NotNull SlashCommandInteractionEvent event) {
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
    }


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        ShardManager jda = bot.getJDA();
        Guild g = jda.getGuildById(guildID);
        assert g != null;


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
    }

    private static ScheduledFuture<?> scheduledFuture;

    public void repeatedMessage(String msg, int time, TimeUnit unit)  throws LoginException {
        ShardManager jda = bot.getJDA();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> sendMessage(jda, msg), 0, time, unit);
    }

    public void sendMessage(ShardManager jda, String msg) {
        TextChannel channel = jda.getTextChannelById(guildBridgeId);
        if (channel != null) {
            channel.sendMessage(msg).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public static void stopScheduledTask() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true); // Cancel the task
            System.out.println("Scheduled task has been stopped.");
        }
    }
}
