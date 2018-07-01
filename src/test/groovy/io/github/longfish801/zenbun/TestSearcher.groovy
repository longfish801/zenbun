/*
 * TestSearcher.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import io.github.longfish801.shared.lang.PackageDirectory;
import io.github.longfish801.zenbun.searchinfo.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Searcherクラスのテスト。
 * 
 * @version 1.0.00 2014/10/09
 * @author io.github.longfish801
 */
public class TestSearcher extends TestCase {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(TestSearcher.class);
	/** ファイル入出力のテスト用フォルダ */
	private static final File testDir = PackageDirectory.deepDir(new File('src/test/resources'), TestSearcher.class);
	
	/**
	 * コンストラクタ。
	 * @param name 名前
	 */
	public TestSearcher(String name) {
		super(name);
	}
	
	/**
	 * 固定文字列による検索ができること。
	 */
	public void testExec001() {
		try {
			File baseDir = new File(testDir, "001");
			String searchWord = "あいうえお";
			
			// 期待する検索結果のリストを作成します
			List<SearchResult> expectedList = new ArrayList<SearchResult>();
			expectedList.add(new SearchResult(new File(baseDir, "file01.txt"), 2, 3));
			
			expectedList.add(new SearchResult(new File(baseDir, "dir01/file0101.txt"), 1, 1));
			expectedList.add(new SearchResult(new File(baseDir, "dir01/file0101.txt"), 3, 4));
			
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0201.txt"), 2, 3));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0201.txt"), 2, 9));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0201.txt"), 3, 3));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0201.txt"), 3, 8));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0201.txt"), 5, 1));
			
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0202.txt"), 2, 3));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0202.txt"), 2, 9));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0202.txt"), 2, 15));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0202.txt"), 3, 3));
			expectedList.add(new SearchResult(new File(baseDir, "dir02/file0202.txt"), 5, 1));
			
			LOG.debug("期待する検索結果：" + expectedList.toString());
			
			// 検索条件を設定します
			Searcher searcher = new Searcher();
			searcher.getSearchInfo().getDirCond().setBaseDir(baseDir.getAbsolutePath());
			LOG.debug("baseDir=" + baseDir.getAbsolutePath());
			KeywordsType keywords = new KeywordsType();
			keywords.getKeyword().add(searchWord);
			searcher.getSearchInfo().getTextCond().setKeywords(keywords);
			
			// 検索を実行し、検索結果のリストを作成します
			SearchInfo searchInfo = searcher.searchText();
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			List<DirType> dirs = searchInfo.getResults().getDir();
			for (DirType dir : dirs){
				LOG.debug("dir.path=" + dir.getPath());
				List<FileType> files =  dir.getFile();
				for (FileType file : files){
					LOG.debug("file.path=" + file.getPath());
					List<ResultType> results =  file.getResult();
					for (ResultType result : results){
						resultList.add(new SearchResult(new File(file.getPath()), result.getLineNum(), result.getColNum()));
					}
				}
			}
			LOG.debug("検索結果：" + resultList.toString());
			
			assertEquals(expectedList, resultList);
			
		} catch (Exception exc){
			LOG.error("想定外の例外が発生しました。", exc);
			fail("想定外の例外が発生しました。");
		}
	}
	
	/**
	 * 検索結果を格納するデータモデルです。
	 */
	private class SearchResult {
		/** ファイルパス */
		public String path;
		/** 行番号 */
		public long lineNum;
		/** 列番号 */
		public long colNum;
		
		/**
		 * コンストラクタ。
		 * @param file ファイル
		 * @param lineNum 行番号
		 * @param colNum 列番号
		 */
		public SearchResult(File file, long lineNum, long colNum) {
			this.path = file.getAbsolutePath();
			this.lineNum = lineNum;
			this.colNum = colNum;
		}
		
		/**
		 * 検索結果として同じ値か判定します。
		 * @param obj 比較対象
		 * @return 判定結果
		 */
		@Override
		public boolean equals(Object obj){
//			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			SearchResult other = (SearchResult) obj;
			if (!path.equals(other.path)) return false;
			if (lineNum != other.lineNum) return false;
			if (colNum != other.colNum) return false;
			return true;
		}
		
		/**
		 * 文字列に変換します。
		 * @return 文字列
		 */
		@Override
		public String toString(){
			return path + " (" + lineNum + ":" + colNum + ")";
		}
	}
}
