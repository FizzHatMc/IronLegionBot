package org.ironlegion.Listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.ironlegion.IronLegionBot;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ButtonListener extends ListenerAdapter {
    IronLegionBot bot;
    //private long bridgeID = 931468999106113577L;
    private long bridgeID = 1404473584105033828L;

    public ButtonListener(IronLegionBot bot) {
        this.bot = bot;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Role adm = bot.getJDA().getRoleById(852410152657354772L);
        if (event.getComponentId().equals("applybutton")) {
            createModel(event);
        }else if (event.getComponentId().equals("accept") && (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().getRoles().contains(adm) )) {
            acceptUser(event);
        }else if (event.getComponentId().equals("reject") && (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().getRoles().contains(adm) )) {
            denyUser(event);
        }
    }

    private void denyUser(ButtonInteractionEvent event) {
        String IGN = event.getChannel().getName().split("-")[0];
        System.out.println("IGN: " + IGN + " DENIED");

        TextChannel bridge = bot.getJDA().getTextChannelById(bridgeID);
        TextChannel application = event.getChannel().asTextChannel();

        application.sendMessage("You have been Denied!").queue();

        event.reply("User has been Denied").setEphemeral(true).queue();
    }

    private void acceptUser(ButtonInteractionEvent event) {
        String IGN = event.getChannel().getName().split("-")[0];
        System.out.println("IGN: " + IGN + " ACCEPTED");

        TextChannel bridge = bot.getJDA().getTextChannelById(bridgeID);
        TextChannel application = event.getChannel().asTextChannel();

        application.sendMessage("You have been accepted! You will be invited right away. If you arent online you will be have 5 Minutes to accept once you join the Game.").queue();


        assert bridge != null;
        bridge.sendMessage("!invite " + IGN).queue();
        event.reply("User has been Accepted").setEphemeral(true).queue();

    }

    private void createModel(@NotNull ButtonInteractionEvent event) {
        TextInput IGN = TextInput.create("ign", "In Game Name", TextInputStyle.SHORT)
                .setPlaceholder("RealKazz")
                .build();

        TextInput PROFILE = TextInput.create("profile", "Profile", TextInputStyle.SHORT)
                .setPlaceholder("Raspberry")
                .build();



        Modal modal = Modal.create("apply", "Apply")
                .addComponents(ActionRow.of(IGN), ActionRow.of(PROFILE))
                .build();

        event.replyModal(modal).queue();
    }
}

/*

ActionRow.of(PROFILESELECTOR)

StringSelectMenu PROFILESELECTOR = StringSelectMenu.create("pS")
                .setPlaceholder("Choose your Profile.")
                .addOptions(
                        SelectOption.of("Apple", "Apple"),
                        SelectOption.of("Banana", "Banana"),
                        SelectOption.of("Blueberry", "Blueberry"),
                        SelectOption.of("Cucumber", "Cucumber"),
                        SelectOption.of("Coconut", "Coconut"),
                        SelectOption.of("Grapes", "Grapes"),
                        SelectOption.of("Kiwi", "Kiwi"),
                        SelectOption.of("Lemon", "Lemon"),
                        SelectOption.of("Lime", "Lime"),
                        SelectOption.of("Mango", "Mango"),
                        SelectOption.of("Orange", "Orange"),
                        SelectOption.of("Papaya", "Papaya"),
                        SelectOption.of("Pineapple", "Pineapple"),
                        SelectOption.of("Peach", "Peach"),
                        SelectOption.of("Pear", "Pear"),
                        SelectOption.of("Pomegranate", "Pomegranate"),
                        SelectOption.of("Raspberry", "Raspberry"),
                        SelectOption.of("Strawberry", "Strawberry"),
                        SelectOption.of("Tomato", "Tomato"),
                        SelectOption.of("Watermelon", "Watermelon"),
                        SelectOption.of("Zucchini", "Zucchini")
                )
                .build();
 */