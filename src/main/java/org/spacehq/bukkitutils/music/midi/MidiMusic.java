package org.spacehq.bukkitutils.music.midi;

import org.spacehq.bukkitutils.music.Music;
import org.spacehq.bukkitutils.music.Track;

import javax.sound.midi.Sequence;
import java.util.ArrayList;
import java.util.List;

public class MidiMusic implements Music {
	private Sequence sequence;
	private long microsDelay;

	public MidiMusic(Sequence sequence) {
		this.sequence = sequence;
		this.microsDelay = Math.max((long) (sequence.getMicrosecondLength() / (double) sequence.getTickLength()), 1);
	}

	@Override
	public long getMicrosDelay() {
		return this.microsDelay;
	}

	@Override
	public List<Track> buildTracks() {
		List<Track> tracks = new ArrayList<Track>();
		for(javax.sound.midi.Track track : this.sequence.getTracks()) {
			tracks.add(new MidiTrack(track));
		}

		return tracks;
	}
}
