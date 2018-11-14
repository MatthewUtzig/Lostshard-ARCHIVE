package com.lostshard.lostshard.player;

import com.lostshard.lostshard.Objects.Player.Bank;
import com.lostshard.lostshard.Objects.Player.Runebook;
import com.lostshard.lostshard.Objects.Player.SpellBook;
import com.lostshard.lostshard.Objects.Player.Titles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class LSPlayer {

    private final UUID uuid;
    private Titles titles;
    private Runebook runebook;
    private SpellBook spellBook;
    private Bank bank;

    public UUID getUuid() {
        return uuid;
    }

    public Titles getTitles() {
        return titles;
    }

    public void setTitles(Titles titles) {
        this.titles = titles;
    }

    public Runebook getRunebook() {
        return runebook;
    }

    public void setRunebook(Runebook runebook) {
        this.runebook = runebook;
    }

    public SpellBook getSpellBook() {
        return spellBook;
    }

    public void setSpellBook(SpellBook spellBook) {
        this.spellBook = spellBook;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void spawnNPC() {
        Bukkit.getPlayer(this.getUuid()).get
    }
}
