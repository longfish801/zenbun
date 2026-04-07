package com.example.zenbun

import org.yaml.snakeyaml.Yaml
import java.nio.file.Paths
import java.nio.file.Files

class ConfigManager {
    
    private Map<String, Object> config = [:]
    private String configPath
    
    ConfigManager() {
        loadConfig()
    }
    
    private void loadConfig() {
        configPath = Paths.get(System.getProperty('user.home'), '.zenbun', 'config.yml').toString()
        
        File configFile = new File(configPath)
        if (!configFile.exists()) {
            createDefaultConfig(configFile)
        }
        
        try {
            Yaml yaml = new Yaml()
            config = yaml.load(new FileInputStream(configFile))
            if (config == null) {
                config = [:]
            }
        } catch (Exception e) {
            config = [:]
        }
    }
    
    private void createDefaultConfig(File configFile) {
        configFile.parentFile.mkdirs()
        configFile.text = """# Zenbun Configuration
command: "open -t '\${filePath}'"
"""
    }
    
    String getCommand() {
        return (String) config.get('command', '')
    }
    
    String interpolateCommand(String filePath, int lineNo) {
        String cmd = getCommand()
        cmd = cmd.replace('${filePath}', filePath)
        cmd = cmd.replace('${lineNo}', String.valueOf(lineNo))
        return cmd
    }
}
