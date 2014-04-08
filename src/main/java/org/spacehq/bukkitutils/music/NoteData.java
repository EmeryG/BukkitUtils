package org.spacehq.bukkitutils.music;

import org.bukkit.Note;

public class NoteData {
	private Note note;
	private float volume;

	public NoteData(Note note, float volume) {
		this.note = note;
		this.volume = volume;
	}

	public Note getNote() {
		return this.note;
	}

	public float getVolume() {
		return this.volume;
	}
}
