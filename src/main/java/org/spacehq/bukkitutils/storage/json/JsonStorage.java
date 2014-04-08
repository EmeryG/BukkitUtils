package org.spacehq.bukkitutils.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spacehq.bukkitutils.storage.MapBasedStorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonStorage extends MapBasedStorage {
	
	private Gson gson;
	
	public JsonStorage() {
		this(true);
	}

	public JsonStorage(boolean prettyPrinting) {
		super();
		this.createGson(prettyPrinting);
	}
	
	public JsonStorage(String path) {
		this(path, true);
	}

	public JsonStorage(String path, boolean prettyPrinting) {
		super(path);
		this.createGson(prettyPrinting);
	}
	
	public JsonStorage(File file) {
		this(file, true);
	}

	public JsonStorage(File file, boolean prettyPrinting) {
		super(file);
		this.createGson(prettyPrinting);
	}

	private void createGson(boolean prettyPrinting) {
		GsonBuilder builder = new GsonBuilder();
		if(prettyPrinting) {
			builder.setPrettyPrinting();
		}

		this.gson = builder.create();
	}
	
	@Override
	public void load(InputStream in) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder build = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null) {
				if(build.length() != 0) {
					build.append("\n");
				}
				
				build.append(line);
			}
			
			this.root = this.gson.fromJson(build.toString(), TypeToken.get(Map.class).getType());
			if(this.root == null) {
				this.root = new HashMap<String, Object>();
			}
		} catch(IOException e) {
			System.err.println("Failed to load from input stream!");
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	@Override
	public void save(OutputStream out) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, "UTF-8");
			writer.write(this.gson.toJson(this.root));
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
