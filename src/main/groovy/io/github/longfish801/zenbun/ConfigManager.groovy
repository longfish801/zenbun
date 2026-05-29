/*
 * ConfigManager.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun

import groovy.util.logging.Slf4j
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
        configFile.text = """### Zenbun Configuration
# Open Command
command: "open -t \${filePath}"
# VSCode
# command: "code -g \${filePath}:\${lineNo}"
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
