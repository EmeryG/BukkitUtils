package org.spacehq.bukkitutils.music.text;

import org.bukkit.Note;
import org.spacehq.bukkitutils.music.Instrument;
import org.spacehq.bukkitutils.music.NoteData;
import org.spacehq.bukkitutils.music.Track;

import java.util.ArrayList;
import java.util.List;

public class TextTrack implements Track {
	private Instrument instrument;
	private List<NoteData> notes;
	private int currentIndex = 0;

	public TextTrack(String line) {
		if(!line.contains(": ") || line.startsWith(": ")) {
			throw new IllegalArgumentException("Missing instrument.");
		}

		String parts[] = line.split(": ");
		try {
			this.instrument = Instrument.valueOf(parts[0].toUpperCase());
			if(this.instrument == null) {
				throw new IllegalArgumentException("Invalid instrument.");
			}
		} catch(Exception e) {
			throw new IllegalArgumentException("Invalid instrument.", e);
		}

		this.notes = new ArrayList<NoteData>();
		for(String data : parts[1].split(" ")) {
			this.notes.add(parseNote(data));
		}
	}

	@Override
	public Instrument getInstrument() {
		return this.instrument;
	}

	@Override
	public NoteData nextNote() {
		if(this.currentIndex >= this.notes.size()) {
			return null;
		}

		return this.notes.get(this.currentIndex++);
	}

	private static NoteData parseNote(String data) {
		if(data.equals("|")) {
			return Track.PAUSE_NOTE;
		}

		int octave = 0;
		int noteStart = 0;
		if(data.startsWith("0") || data.startsWith("1") || data.startsWith("2")) {
			octave = Integer.parseInt(String.valueOf(data.charAt(0)));
			noteStart = 1;
		}

		Note.Tone tone = Note.Tone.valueOf(String.valueOf(data.charAt(noteStart)).toUpperCase());
		boolean sharped = false;
		if(data.length() > 1 + noteStart && data.charAt(noteStart + 1) == '#') {
			sharped = true;
		}

		return new NoteData(new Note(octave, tone, sharped), 3);
	}
}
