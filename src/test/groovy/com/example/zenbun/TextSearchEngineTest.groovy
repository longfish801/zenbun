package com.example.zenbun

import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList

class TextSearchEngineTest extends Specification {
    
    private TextSearchEngine searchEngine
    private File testDir
    
    def setup() {
        searchEngine = new TextSearchEngine()
        testDir = Files.createTempDirectory('zenbun-test').toFile()
    }
    
    def cleanup() {
        testDir.deleteDir()
    }
    
    def "検索キーワードを含むテキストファイルが見つかる"() {
        given:
        File testFile = new File(testDir, 'test.txt')
        testFile.text = """Hello World
This is a test
Search me"""
        
        when:
        ArrayList<SearchResult> results = searchEngine.search(testDir.absolutePath, 'test', false, false)
        
        then:
        results.size() == 1
        results[0].line.contains('test')
        results[0].lineNo == 2
    }
    
    def "複数ファイルでの検索"() {
        given:
        File file1 = new File(testDir, 'file1.txt')
        file1.text = """First file
contains keyword"""
        
        File file2 = new File(testDir, 'file2.txt')
        file2.text = """Second file
also has keyword"""
        
        when:
        ArrayList<SearchResult> results = searchEngine.search(testDir.absolutePath, 'keyword', false, false)
        
        then:
        results.size() == 2
    }
    
    def "正規表現検索が動作する"() {
        given:
        File testFile = new File(testDir, 'test.txt')
        testFile.text = """test123
test456
abc789"""
        
        when:
        ArrayList<SearchResult> results = searchEngine.search(testDir.absolutePath, 'test\\d+', true, false)
        
        then:
        results.size() == 2
    }
    
    def "大文字小文字無視オプンが動作する"() {
        given:
        File testFile = new File(testDir, 'test.txt')
        testFile.text = """HELLO
hello
HeLLo"""
        
        when:
        ArrayList<SearchResult> results = searchEngine.search(testDir.absolutePath, 'hello', false, true)
        
        then:
        results.size() == 3
    }
}
