/*
 * ConfigManagerTest.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun

import spock.lang.Specification

class ConfigManagerTest extends Specification {
    
    def "ConfigManagerが初期化される"() {
        when:
        ConfigManager manager = new ConfigManager()
        
        then:
        manager.getCommand() != null
    }
    
    def "コマンドテンプレートが補完される"() {
        given:
        ConfigManager manager = new ConfigManager()
        
        when:
        String cmd = manager.interpolateCommand('/path/to/file.txt', 42)
        
        then:
        cmd.contains('/path/to/file.txt') || cmd.contains('${filePath}')
        cmd.contains('42') || cmd.contains('${lineNo}')
    }
}
