package com.lostshard.Lostshard.Objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerListSet implements Set<UUID>  {
	
	private Set<UUID> players = new HashSet<UUID>();
	
	@Override
	public int size() {
		return players.size();
	}

	@Override
	public boolean isEmpty() {
		return players.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return players.contains(o);
	}
	
	public boolean contains(Player player) {
		return contains(player.getUniqueId());
	}

	public boolean contains(OfflinePlayer offlinePlayer) {
		return contains(offlinePlayer.getUniqueId());
	}
	
	public boolean contains(UUID uuid) {
		return players.contains(uuid);
	}
	
	@Override
	public Iterator<UUID> iterator() {
		return players.iterator();
	}

	@Override
	public Object[] toArray() {
		return players.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return players.toArray(a);
	}

	@Override
	public boolean add(UUID uuid) {
		return players.add(uuid);
	}
	
	public boolean add(Player player) {
		return add(player.getUniqueId());
	}

	public boolean add(OfflinePlayer offlinePlayer) {
		return add(offlinePlayer.getUniqueId());
	}
	
	@Override
	public boolean remove(Object o) {
		return players.remove(o);
	}
	
	public boolean remove(Player player) {
		return remove(player.getUniqueId());
	}

	public boolean remove(OfflinePlayer offlinePlayer) {
		return remove(offlinePlayer.getUniqueId());
	}
	
	public boolean remove(UUID uuid) {
		return players.remove(uuid);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return players.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends UUID> c) {
		return players.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return players.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return players.removeAll(c);
	}

	@Override
	public void clear() {
		players.clear();
	}

	public Set<String> usernames() {
		Set<String> result = new HashSet<>();
		for(UUID uuid : this)
			result.add(Bukkit.getOfflinePlayer(uuid).getName());
		return result;
	}
}
