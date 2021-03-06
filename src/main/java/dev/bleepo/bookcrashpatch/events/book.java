package dev.bleepo.bookcrashpatch.events;

import dev.bleepo.bookcrashpatch.Main;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class book implements Listener {
    private final Main plugin;

    public book(Main plugin) {
        this.plugin = plugin;
    }

    //Thx John
    @EventHandler
    public void onEdit(PlayerEditBookEvent event) {
        for (String page : event.getNewBookMeta().getPages()) {
            if (!StandardCharsets.ISO_8859_1.newEncoder().canEncode((page))) {
                event.setCancelled(true);
            }
        }
    }

    public boolean isShulker(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) itemStack.getItemMeta()).getBlockState() instanceof ShulkerBox;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                if (isShulker(item)) {
                    ItemMeta meta = item.getItemMeta();
                    BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                    ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                    for (ItemStack i : shulkerBox.getInventory().getContents()) {
                        if (i != null) {
                            if (i.getType() == Material.WRITTEN_BOOK) {
                                BookMeta book = (BookMeta) i.getItemMeta();
                                if (isBanBook(book)) {
                                    player.getWorld().dropItem(player.getLocation(), i);
                                    shulkerBox.getInventory().remove(i);
                                }
                            }
                        }
                    }
                    blockStateMeta.setBlockState(shulkerBox);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    private boolean isBanBook(BookMeta book) {
        for (String bookPages : book.getPages()) {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
            Matcher matcher = pattern.matcher(bookPages);
            return matcher.find();
        }
        return false;
    }
}
