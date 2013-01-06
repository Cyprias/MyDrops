package com.cyprias.mydrops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YML extends YamlConfiguration {
	private static File file = null;
	public YML(InputStream fileStream) {
		//load yml from resources. 
		try {
			load(fileStream);
		} catch (IOException e) {e.printStackTrace();
		} catch (InvalidConfigurationException e) {e.printStackTrace();
		}
	}
	
	public YML(File pluginDur, String fileName) {
		//Load yml from directory.
		YML.file = new File(pluginDur, fileName);

		try {
			load(YML.file);
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		} catch (InvalidConfigurationException e) {e.printStackTrace();
		}
	}
	
	public YML(InputStream fileStream, File pluginDur, String fileName) {
		//Copy yml resource to directory then load it.

		YML.file = new File(pluginDur, fileName);
		if (!YML.file.exists())
			YML.file = toFile(fileStream, pluginDur, fileName);
		
		try {
			load(YML.file);
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		} catch (InvalidConfigurationException e) {e.printStackTrace();
		}
	}
	
	public YML(InputStream fileStream, File pluginDur, String fileName, Boolean noLoad) {
		//Just copy the stream to directory, no loading as YML. 
		YML.file = new File(pluginDur, fileName);
		if (!YML.file.exists())
			YML.file = toFile(fileStream, pluginDur, fileName);
	}
	
	//Write a stream to file on disk, return the file object.  
	private static File toFile(InputStream in, File pluginDur, String fileName) {
		File file = new File(pluginDur, fileName);
		file.getParentFile().mkdirs();
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {e.printStackTrace();
		}
		return file;
	}
	
	public void save(){
		try {
			save(file);
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
}
