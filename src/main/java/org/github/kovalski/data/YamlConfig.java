package org.github.kovalski.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.github.kovalski.TwoPlayerHorseRiding;

import java.io.*;

public class YamlConfig extends YamlConfiguration {

    private final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final String fileName;

    public YamlConfig(String fileName){
        File file = new File(instance.getDataFolder(), fileName);
        this.fileName = fileName;
        saveDefaultConfig(file);
        this.load(file);
    }

    @Override
    public void load(File file) {
        try {
            this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File can't be null");
        Files.createParentDirs(file);
        String data = this.saveToString();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        }
    }

    private void saveDefaultConfig(File file) {
        if (!file.exists()) {
            instance.saveResource(fileName, false);
        }
    }
}
