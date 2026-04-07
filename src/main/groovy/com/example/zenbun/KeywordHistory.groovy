package com.example.zenbun

import java.nio.file.Paths
import java.nio.file.Files

class KeywordHistory {
    
    private List<String> keywords = []
    private String lastRootFolder = ''
    private boolean lastRegexEnabled = false
    private boolean lastIgnoreCaseEnabled = false
    private String historyFile
    
    KeywordHistory() {
        historyFile = Paths.get(System.getProperty('user.home'), '.zenbun', 'history.txt').toString()
        loadHistory()
    }
    
    private void loadHistory() {
        File file = new File(historyFile)
        if (file.exists()) {
            try {
                keywords = file.readLines().reverse() as List<String>
            } catch (Exception e) {
                keywords = []
            }
        }
    }
    
    void addKeyword(String keyword) {
        if (!keyword.isEmpty() && !keywords.contains(keyword)) {
            keywords.insert(0, keyword)
            if (keywords.size() > 100) {
                keywords = keywords.take(100)
            }
            saveHistory()
        }
    }
    
    List<String> getAllKeywords() {
        return keywords.clone()
    }
    
    String getLastRootFolder() {
        return lastRootFolder
    }
    
    void setLastRootFolder(String folder) {
        lastRootFolder = folder
    }
    
    boolean isLastRegexEnabled() {
        return lastRegexEnabled
    }
    
    void setLastRegexEnabled(boolean enabled) {
        lastRegexEnabled = enabled
    }
    
    boolean isLastIgnoreCaseEnabled() {
        return lastIgnoreCaseEnabled
    }
    
    void setLastIgnoreCaseEnabled(boolean enabled) {
        lastIgnoreCaseEnabled = enabled
    }
    
    private void saveHistory() {
        try {
            File file = new File(historyFile)
            file.parentFile.mkdirs()
            file.text = keywords.join('\n')
        } catch (Exception e) {
            // 履歴保存失敗は無視
        }
    }
}
