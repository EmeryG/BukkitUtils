package org.spacehq.bukkitutils.music.midi;

import org.bukkit.Note;
import org.spacehq.bukkitutils.music.Instrument;
import org.spacehq.bukkitutils.music.NoteData;
import org.spacehq.bukkitutils.music.Track;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

public class MidiTrack implements Track {
	private static final int[] instruments = {
			0, 0, 0, 0, 0, 0, 0, 5,
			6, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 5,
			5, 5, 5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6, 6, 6,
			5, 5, 5, 5, 5, 5, 5, 2,
			5, 5, 5, 5, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 1, 3, 1, 1, 1, 5,
			1, 1, 1, 1, 1, 2, 4, 3
	};

	private static final int[] percussion = {
			3, 3, 4, 4, 3, 2, 3, 2,
			2, 2, 2, 2, 2, 2, 2, 2,
			3, 2, 3, 3, 3, 0, 3, 3,
			3, 3, 3, 2, 2, 3, 3, 3,
			2, 2, 0, 0, 2, 2, 0, 0,
			3, 3, 3, 3, 3, 3, 5, 5,
			3, 3
	};

	private Instrument instrument;
	private List<NoteData> notes;
	private int currentIndex = 0;

	public MidiTrack(javax.sound.midi.Track track) {
		this.instrument = Instrument.BASS_DRUM;
		this.notes = new ArrayList<NoteData>();
		long lastNote = 0;
		long lastTick = 0;
		for(int index = 0; index < track.size(); index++) {
			MidiEvent event = track.get(index);
			MidiMessage message = event.getMessage();
			if((message.getStatus() & 0xF0) == ShortMessage.PROGRAM_CHANGE) {
				ShortMessage msg = (ShortMessage) message;
				int channel = msg.getChannel();
				int instrument = msg.getData1();
				if(channel == 9) {
					this.instrument = toInstrument(toPercussionByte(instrument));
				} else {
					this.instrument = toInstrument(toInstrumentByte(instrument));
				}
			} else if((message.getStatus() & 0xF0) == ShortMessage.NOTE_ON) {
				ShortMessage msg = (ShortMessage) message;
				long curr = event.getTick();
				if(curr == lastTick && curr != 0) {
					curr = lastNote + 1;
				}

				for(int times = 0; times < curr - lastNote - 1; times++) {
					this.notes.add(Track.PAUSE_NOTE);
				}

				int n = msg.getData1();
				byte note = toNoteByte(n);
				this.notes.add(new NoteData(new Note(note), 10 * (msg.getData2() / 127f)));
				lastTick = event.getTick();
				lastNote = curr;
			}
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

	private static Instrument toInstrument(byte instrument) {
		switch(instrument) {
			case 1:
				return Instrument.BASS_GUITAR;
			case 2:
				return Instrument.SNARE_DRUM;
			case 3:
				return Instrument.STICKS;
			case 4:
				return Instrument.BASS_DRUM;
			case 5:
				return Instrument.GUITAR;
			case 6:
				return Instrument.BASS;
			default:
				return Instrument.PIANO;
		}
	}

	private static byte toInstrumentByte(Integer patch) {
		if(patch == null) {
			return 0;
		}

		if(patch < 0 || patch >= instruments.length) {
			return 0;
		}

		return (byte) instruments[patch];
	}

	private static byte toPercussionByte(Integer patch) {
		if(patch == null) {
			return 0;
		}

		int i = patch - 33;
		if(i < 0 || i >= percussion.length) {
			return 1;
		}

		return (byte) percussion[i];
	}

	private static byte toNoteByte(int n) {
		if(n < 54) {
			return (byte) ((n - 6) % (18 - 6));
		} else if(n > 78) {
			return (byte) ((n - 6) % (18 - 6) + 12);
		} else {
			return (byte) (n - 54);
		}
	}
}
