package org.spacehq.bukkitutils.storage.yaml;

import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.spacehq.bukkitutils.storage.MapBasedStorage;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class YamlStorage extends MapBasedStorage {

	private Yaml yaml;
	
	public YamlStorage() {
		super();
		this.initYaml();
	}
	
	public YamlStorage(String path) {
		super(path);
		this.initYaml();
	}
	
	public YamlStorage(File file) {
		super(file);
		this.initYaml();
	}

	private void initYaml() {
		DumperOptions options = new DumperOptions();
		options.setIndent(4);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yaml = new Yaml(new YamlConstructor(), new YamlRepresenter(), options);
	}

	@Override
	public void load(InputStream in) {
		this.root = (Map<String, Object>) this.yaml.load(in);
		if(this.root == null) this.root = new HashMap<String, Object>();
	}
	
	@Override
	public void save(OutputStream out) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, "UTF-8");
			this.yaml.dump(this.root, writer);
		} catch (IOException e) {
			System.err.println("Failed to save config file to stream!");
			e.printStackTrace();
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
