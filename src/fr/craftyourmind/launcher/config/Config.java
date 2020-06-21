package fr.craftyourmind.launcher.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Config {
    private File file;

    public Map<String, Object> config = new HashMap<>();

    private Yaml yaml = new Yaml();

    public Config(String path) {
        this.file = new File(path);
    }

    private void setDefaults() {
        this.config.clear();
        this.config.put("usernameSave", Boolean.TRUE);
        this.config.put("passwordSave", Boolean.TRUE);
        this.config.put("ram", 1024.0D);
    }

    public void generate() throws IOException {
        setDefaults();
        save();
    }

    public void load() throws IOException {
        this.config = (Map<String, Object>)this.yaml.load(new FileInputStream(this.file));
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(this.file);
        this.yaml.dump(this.config, writer);
    }

    public Object getObject(String key) {
        return this.config.get(key);
    }

    public String getString(String key) {
        return (String)getObject(key);
    }

    public int getInt(String key) {
        return (Integer) getObject(key);
    }

    public double getDouble(String key) {
        return (Double) getObject(key);
    }

    public boolean getBoolean(String key) {
        return (Boolean) getObject(key);
    }
}