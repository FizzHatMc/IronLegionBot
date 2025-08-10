package org.ironlegion.Listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("apply")) {
            createModel(event);
        }
    }

    private void createModel(@NotNull ButtonInteractionEvent event) {
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

        TextInput IGN = TextInput.create("ign", "In Game Name", TextInputStyle.SHORT)
                .setPlaceholder("RealKazz")
                .build();

        TextInput PROFILE = TextInput.create("profile", "Profile", TextInputStyle.SHORT)
                .setPlaceholder("Raspberry")
                .build();



        Modal modal = Modal.create("applyModal", "Apply")
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