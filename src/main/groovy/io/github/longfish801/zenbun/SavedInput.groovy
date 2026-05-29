/*
 * SavedInput.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun

import groovy.util.logging.Slf4j
import groovy.yaml.YamlSlurper
import groovy.yaml.YamlBuilder
import java.nio.file.Paths
import java.nio.file.Files

/**
 * 検索キーワードの履歴と関連状態を管理します。
 */
@Slf4j('LOG')
class SavedInput {
    
    private List<String> keywords = []
    private List<String> folders = []
    private String lastRootFolder = ''
    private boolean lastRegexEnabled = false
    private boolean lastIgnoreCaseEnabled = false
    private String inputFile
    
    SavedInput() {
        inputFile = Paths.get(System.getProperty('user.home'), '.zenbun', 'input.yml').toString()
        loadInput()
    }
    
    private void loadInput() {
        File file = new File(inputFile)
        if (file.exists()) {
            try {
                def yaml = new YamlSlurper()
                def data = yaml.parse(file)
                keywords = data.keywords ?: []
                folders = data.folders ?: []
                lastRootFolder = data.lastRootFolder ?: ''
                lastRegexEnabled = data.lastRegexEnabled ?: false
                lastIgnoreCaseEnabled = data.lastIgnoreCaseEnabled ?: false
            } catch (Exception e) {
                LOG.error("前回の入力の読み込みに失敗", e)
                folders = []
                keywords = []
                lastRootFolder = ''
                lastRegexEnabled = false
                lastIgnoreCaseEnabled = false
            }
        }
    }
    
    void addKeyword(String keyword) {
        if (!keyword.isEmpty() && !keywords.contains(keyword)) {
            keywords = [keyword] + keywords
            if (keywords.size() > 100) {
                keywords = keywords.take(100)
            }
            saveInput()
        }
    }
    
    void clearKeywords() {
        keywords.clear()
        saveInput()
    }
    
    List<String> getAllKeywords() {
        return keywords.clone()
    }
    
    void addFolder(String folder) {
        if (!folder.isEmpty() && !folders.contains(folder)) {
            folders = [folder] + folders
            if (folders.size() > 100) {
                folders = folders.take(100)
            }
            saveInput()
        }
    }
    
    void clearFolders() {
        folders.clear()
        saveInput()
    }
    
    List<String> getAllFolders() {
        return folders.clone()
    }
    
    String getLastRootFolder() {
        return lastRootFolder
    }
    
    void setLastRootFolder(String folder) {
        lastRootFolder = folder
        saveInput()
    }
    
    boolean isLastRegexEnabled() {
        return lastRegexEnabled
    }
    
    void setLastRegexEnabled(boolean enabled) {
        lastRegexEnabled = enabled
        saveInput()
    }
    
    boolean isLastIgnoreCaseEnabled() {
        return lastIgnoreCaseEnabled
    }
    
    void setLastIgnoreCaseEnabled(boolean enabled) {
        lastIgnoreCaseEnabled = enabled
        saveInput()
    }
    
    private void saveInput() {
        try {
            File file = new File(inputFile)
            file.parentFile.mkdirs()
            def yaml = new YamlBuilder()
            yaml {
                keywords keywords
                folders folders
                lastRootFolder lastRootFolder
                lastRegexEnabled lastRegexEnabled
                lastIgnoreCaseEnabled lastIgnoreCaseEnabled
            }
            file.text = yaml.toString()
        } catch (Exception e) {
            LOG.error("入力内容の保存に失敗", e)
        }
    }
}
