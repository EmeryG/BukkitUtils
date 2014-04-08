package org.spacehq.bukkitutils.music;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicPlayer implements Runnable {
	private static Map<String, MusicPlayer> activePlayers = new HashMap<String, MusicPlayer>();
	private static Plugin plugin;

	public static void initialize(Plugin plug) {
		plugin = plug;
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent event) {
				MusicPlayer.stop(event.getPlayer());
			}
		}, plugin);
	}

	public static void play(Player player, Music music) {
		stop(player);
		MusicPlayer play = new MusicPlayer(player, music);
		activePlayers.put(player.getName(), play);
		new Thread(play).start();
	}

	public static boolean isPlaying(Player player) {
		return activePlayers.containsKey(player.getName());
	}

	public static void stop(Player player) {
		if(isPlaying(player)) {
			activePlayers.get(player.getName()).stop();
		}
	}

	public static void stopAll() {
		for(MusicPlayer player : activePlayers.values()) {
			player.stop();
		}

		activePlayers.clear();
	}

	public static void cleanup() {
		stopAll();
		plugin = null;
	}

	private Player player;
	private Music music;
	private boolean finished = false;

	private List<Track> tracks;

	private MusicPlayer(Player player, Music music) {
		this.player = player;
		this.music = music;
		this.tracks = music.buildTracks();
	}

	public Player getPlayer() {
		return this.player;
	}

	public Music getMusic() {
		return this.music;
	}

	public boolean isFinished() {
		return this.finished;
	}

	public void stop() {
		this.finished = true;
		activePlayers.remove(this.player.getName());
	}

	@Override
	public void run() {
		long last = System.nanoTime() / 1000;
		while(!this.isFinished()) {
			long time = System.nanoTime() / 1000;
			if(time - last >= this.music.getMicrosDelay()) {
				last = time;
				boolean done = true;
				for(Track track : this.tracks) {
					NoteData note = track.nextNote();
					if(note != null) {
						done = false;
						if(note != Track.PAUSE_NOTE) {
							Sound sound = null;
							switch(track.getInstrument()) {
								case GUITAR:
									sound = Sound.NOTE_PLING;
									break;
								case PIANO:
									sound = Sound.NOTE_PIANO;
									break;
								case BASS_DRUM:
									sound = Sound.NOTE_BASS_DRUM;
									break;
								case SNARE_DRUM:
									sound = Sound.NOTE_SNARE_DRUM;
									break;
								case STICKS:
									sound = Sound.NOTE_STICKS;
									break;
								case BASS:
									sound = Sound.NOTE_BASS;
									break;
								case BASS_GUITAR:
									sound = Sound.NOTE_BASS_GUITAR;
									break;
							}

							this.player.playSound(this.player.getLocation(), sound, note.getVolume(), (float) Math.pow(2.0, ((double) note.getNote().getId() - 12.0) / 12.0));
						}
					}
				}

				if(done) {
					this.stop();
				}
			}
		}
	}
}
