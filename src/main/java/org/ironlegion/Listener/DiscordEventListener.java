package org.ironlegion.Listener;

import com.google.gson.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.ironlegion.Util.SkillLevelCalculator;
import org.ironlegion.api.HypixelApiUtil;
import org.jetbrains.annotations.NotNull;
import org.ironlegion.IronLegionBot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
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
        bot.getJDA().getShards().forEach(jda -> jda.addEventListener(new ButtonListener(bot)));
    }

    private void registerCommands(@NotNull ShardManager jda) {
        Guild g = jda.getGuildById(guildID); // Replace this with the ID of your own server.

        if (g != null) {
            CommandListUpdateAction commands = g.updateCommands();
            //commands.addCommands(Commands.slash("log","Logs Loan into a File (Archiving)").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("createapplymessage","Uses the current open Channel as a Apply Channel and sends the Message for the Users").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("createchanneltest","test").setDefaultPermissions(DefaultMemberPermissions.DISABLED)).queue();
            commands.addCommands(Commands.slash("apitest","Test API features").setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                    .addOption(OptionType.STRING,"name","IGN")
                    .addOption(OptionType.STRING,"profile","profile"))
                    .queue();
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

    Map<String,String> userData = new HashMap<>();
    
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
            case "apitest":
                userData.put("SKILL_AVERAGE", String.valueOf(calcAvg(event)));
                break;

        }
//  || skillName.equals("SKILL_CARPENTRY")
    }
    
    private double calcAvg(SlashCommandInteractionEvent event){
        JsonObject test = HypixelApiUtil.getPlayerStats(event.getOption("name").getAsString());
        try {
            File file = new File("apiTest.txt");
            file.createNewFile();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            JsonArray profiles = test.get("profiles").getAsJsonArray();
            System.out.println(profiles.size());
            JsonObject profileObj = null;

            for (JsonElement profileElement : profiles) {
                profileObj = profileElement.getAsJsonObject();
                String cuteName = profileObj.get("cute_name").getAsString();
                System.out.println("Cute Name: " + cuteName);
                System.out.println("-----");
                if(cuteName.equals(event.getOption("profile").getAsString())){
                    System.out.println("Found Profile : " + profileObj.get("profile_id"));
                    break;
                }
            }
            if(profileObj==null){return 0;}

            JsonObject members = profileObj.get("members").getAsJsonObject();
            JsonObject rk = members.get("85de5df53960428bbca47dce9766ff8f").getAsJsonObject();
            JsonObject pd = rk.get("player_data").getAsJsonObject();
            JsonObject exp = pd.get("experience").getAsJsonObject();
            Map<String, String> skillsMap = new HashMap<>();
            Map<String, Double> normalSkill = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : exp.entrySet()) {
                String skillName = entry.getKey();
                BigDecimal skillValue = new BigDecimal(entry.getValue().getAsString());
                if(skillName.equals("SKILL_RUNECRAFTING")){continue;}
                skillsMap.put(skillName, skillValue.toPlainString());
                normalSkill.put(skillName, SkillLevelCalculator.xpToLevel(skillValue));

            }

            skillsMap.forEach((s,i)->{System.out.println(s + " | " + i);});
            return ((normalSkill.values().stream().mapToDouble(Double::doubleValue).sum()) / normalSkill.size()) ;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private void sendMessage(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessage("Click the button below to apply for the Iron Legion [LEGION] guild! Currently, our requirements are an active profile and an agreement to follow the Hypixel and Discord rules. Thanks for checking us out!")
                .setActionRow(
                        Button.primary("applybutton","Apply!")
                ).queue();
    }


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        ShardManager jda = bot.getJDA();
        Guild g = jda.getGuildById(guildID);
        assert g != null;

        if(event.getModalId().equals("apply")) {
            String ign = Objects.requireNonNull(event.getValue("ign")).getAsString();

            TextChannel newChannel = g.createTextChannel(ign+"-application",bot.getJDA().getCategoryById(applyCategory))
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

            MessageCreateAction action = newChannel.sendMessage(ign+" wants to join the Guild. Click Accept to invite them and give the Respective Roles! (Staff)")
                    .addActionRow(
                            Button.primary("accept", "Accept"),
                            Button.danger("reject", "Reject")
                    );
            action.queue();
            newChannel.sendMessage(event.getMember().getAsMention() + " this is your application for Iron Legion guild.").queue();

            event.reply("Application created " + newChannel.getAsMention()).setEphemeral(true).queue();

///  /level name: Mizcos profile: Apple
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