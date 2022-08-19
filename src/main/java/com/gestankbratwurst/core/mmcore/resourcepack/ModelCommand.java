package com.gestankbratwurst.core.mmcore.resourcepack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.gestankbratwurst.core.mmcore.util.Msg;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 15.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("models")
@CommandPermission("mmcore.model.command")
public class ModelCommand extends BaseCommand {

  @Subcommand("as item")
  @CommandCompletion("@Model")
  public void onGiveItem(final Player player, @Values("@Model") final TextureModel model) {
    player.getInventory().addItem(model.getItem());
    Msg.sendInfo(player, "Du hast das model {} als item erhalten.", model.toString());
  }

  @Subcommand("as text")
  @CommandCompletion("@Model")
  public void onAsText(final Player player, @Values("@Model") final TextureModel model) {
    player.getInventory().addItem(model.getItem());
    Msg.sendInfo(player, "Model im Chat: §f" + model.getChar());
  }

}
