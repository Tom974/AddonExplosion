package me.mynqme.explosion;

import me.mynqme.plasmaprisoncore.StringUtil;
import me.mynqme.plasmaprisoncore.addons.cmds.CommandInfo;
import me.mynqme.plasmaprisoncore.addons.cmds.XCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

@CommandInfo(
        name="explosive",
        desc="Toggle explosive"
)
public class CmdExplosive extends XCommand {
    private final Explosion instance = ((Explosion) Explosion.getInstance());
    @Override
    public void onCommand(CommandSender sender, Player p, String[] args) {
        boolean curr = instance.toggleExplosive(p.getUniqueId());
        p.sendMessage(StringUtil.color(instance.config.getString("messages.explosive-toggle-" + curr)));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }
}
