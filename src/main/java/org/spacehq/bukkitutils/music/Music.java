package org.spacehq.bukkitutils.music;

import java.util.List;

public interface Music {
	public long getMicrosDelay();

	public List<Track> buildTracks();
}
