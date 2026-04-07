package com.example.zenbun

import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.ArrayList

class TextSearchEngine {

    ArrayList<SearchResult> search(String rootFolder, String keyword, boolean useRegex, boolean ignoreCase) {
        ArrayList<SearchResult> results = new ArrayList<>()
        
        Pattern pattern = createPattern(keyword, useRegex, ignoreCase)
        
        Paths.get(rootFolder).toFile().eachFileRecurse { file ->
            if (file.isFile() && isTextFile(file)) {
                try {
                    searchInFile(file, pattern, results, ignoreCase)
                } catch (Exception e) {
                    // ファイル読み込みエラーはスキップ
                }
            }
        }
        
        return results.sort { a, b ->
            if (a.filePath != b.filePath) {
                return a.filePath.compareTo(b.filePath)
            }
            return a.lineNo.compareTo(b.lineNo)
        }
    }

    private Pattern createPattern(String keyword, boolean useRegex, boolean ignoreCase) {
        if (useRegex) {
            int flags = ignoreCase ? Pattern.CASE_INSENSITIVE : 0
            return Pattern.compile(keyword, flags)
        } else {
            if (ignoreCase) {
                return Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE)
            } else {
                return Pattern.compile(Pattern.quote(keyword))
            }
        }
    }

    private void searchInFile(File file, Pattern pattern, List<SearchResult> results, boolean ignoreCase) {
        int lineNo = 0
        file.eachLine { line ->
            lineNo++
            
            def matcher = pattern.matcher(line)
            while (matcher.find()) {
                int columnNo = matcher.start() + 1
                results.add(new SearchResult(
                    filePath: file.absolutePath,
                    lineNo: lineNo,
                    columnNo: columnNo,
                    line: line
                ))
                // 同一行内の複数マッチはスキップ（最初のマッチのみ）
                break
            }
        }
    }

    private boolean isTextFile(File file) {
        String name = file.name.toLowerCase()
        
        // テキストファイルの拡張子
        List<String> textExtensions = [
            'txt', 'java', 'groovy', 'gradle', 'xml', 'properties', 'yml', 'yaml',
            'json', 'html', 'css', 'js', 'md', 'sh', 'py', 'rb', 'go', 'rs'
        ]
        
        return textExtensions.any { name.endsWith('.' + it) }
    }
}
