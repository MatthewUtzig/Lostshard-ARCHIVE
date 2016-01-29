package com.lostshard.Lostshard.Objects.Player;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lostshard.Lostshard.Main.Lostshard;

@Entity
public class OfflineMessage {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@Type(type="uuid-char")
	private UUID player;
	private String message;
	private boolean seen;
	
	public OfflineMessage() {
		
	}
	
	public OfflineMessage(UUID player, String message) {
		this.player = player;
		this.message = message;
		this.insert();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public UUID getPlayer() {
		return player;
	}
	
	public void setPlayer(UUID player) {
		this.player = player;
	}
	
	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public void save() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.update(this);
			t.commit();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	public void insert() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.save(this);
			t.commit();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	public void delete() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.delete(this);
			s.clear();
			t.commit();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
}
