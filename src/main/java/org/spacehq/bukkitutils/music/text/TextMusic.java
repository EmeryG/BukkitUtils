package org.spacehq.bukkitutils.music.text;

import org.spacehq.bukkitutils.music.Music;
import org.spacehq.bukkitutils.music.Track;

import java.util.ArrayList;
import java.util.List;

public class TextMusic implements Music {
	private String text;
	private long microsDelay;

	public TextMusic(String text) {
		if(!text.startsWith("MPN: ")) {
			throw new IllegalArgumentException("Missing microseconds per note.");
		}

		String tempoLine = text.split("\n")[0];
		this.text = text.replace(tempoLine + "\n", "");
		try {
			this.microsDelay = Long.parseLong(tempoLine.split(": ")[1]);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid microseconds per note.", e);
		}
	}

	@Override
	public long getMicrosDelay() {
		return this.microsDelay;
	}

	@Override
	public List<Track> buildTracks() {
		List<Track> tracks = new ArrayList<Track>();
		for(String line : this.text.split("\n")) {
			tracks.add(new TextTrack(line));
		}

		return tracks;
	}
}
