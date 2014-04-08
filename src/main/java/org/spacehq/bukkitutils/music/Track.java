package org.spacehq.bukkitutils.music;

import org.bukkit.Note;

public interface Track {
	public static final NoteData PAUSE_NOTE = new NoteData(new Note(0), 0);

	public Instrument getInstrument();

	public NoteData nextNote();
}
