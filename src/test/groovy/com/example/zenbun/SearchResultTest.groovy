package com.example.zenbun

import spock.lang.Specification

class SearchResultTest extends Specification {
    
    def "SearchResultのプロパティが設定される"() {
        when:
        SearchResult result = new SearchResult(
            filePath: '/path/to/file.txt',
            lineNo: 10,
            columnNo: 5,
            line: 'This is a test line'
        )
        
        then:
        result.filePath == '/path/to/file.txt'
        result.lineNo == 10
        result.columnNo == 5
        result.line == 'This is a test line'
    }
}
