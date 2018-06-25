/*
 * DirSearcher.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.longfish801.zenbun.searchinfo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tools.ant.DirectoryScanner;

/**
 * ディレクトリを検索します。
 * 
 * @version 1.0.00 2014/09/12
 * @author io.github.longfish801
 */
class DirSearcher {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(DirSearcher.class);
	/** ディレクトリ検索条件 */
	private DirCondType cond = null;
	
	/**
	 * コンストラクタ。
	 *
	 * @param cond DirCondType
	 */
	public DirSearcher(DirCondType cond){
		this.cond = cond;
	}
	
	/**
	 * ディレクトリを検索し、結果を返します。
	 *
	 * @return 検索結果を満たしたディレクトリの一覧
	 */
	public ResultsType search(){
		// 対象/除外ディレクトリのパターンを参照します
		String[] includes = [ "**" ] as String[];
		if (cond.getIncludes() != null && cond.getIncludes().getInclude().size() > 0){
			includes = cond.getIncludes().getInclude().toArray(new String[0]);
		}
		
		String[] excludes = null;
		if (cond.getExcludes() != null && cond.getExcludes().getExclude().size() > 0){
			excludes = cond.getExcludes().getExclude().toArray(new String[0]);
		}
		
		// ベースディレクトリ配下からディレクトリを探します
		final DirectoryScanner scanner = new DirectoryScanner();
		File baseDir = new File(cond.getBaseDir());
		scanner.setBasedir(baseDir);
		scanner.setIncludes(includes);
		if (excludes != null){
			scanner.setExcludes(excludes);
		}
		scanner.setCaseSensitive(true);
		LOG.trace("BGN SEARCH DIRS");
		scanner.scan();
		LOG.trace("END SEARCH DIRS");
		String[] includedDirs = scanner.getIncludedDirectories();
		if (LOG.isDebugEnabled()) LOG.debug("dirs=" + Arrays.toString(includedDirs));
		
		// 下位ファイル数でフィルタリングします
		if (cond.getSubFileNum() != null){
			includedDirs = filterSubFileNum(includedDirs, cond.getSubFileNum(), baseDir);
		}
		
		// 下位フォルダ数でフィルタリングします
		if (cond.getSubDirNum() != null){
			includedDirs = filterSubDirNum(includedDirs, cond.getSubDirNum(), baseDir);
		}
		
		// 検索結果のインスタンスを生成します
		ResultsType results = new ResultsType();
		List<DirType> dirs = results.getDir();
		results.setDirNum(includedDirs.length);
		for (String path : includedDirs) {
			File dir = new File(baseDir, path);
			DirType dirType = new DirType();
			dirType.setPath(dir.getAbsolutePath());
			dirs.add(dirType);
		}
		
		return results;
	}
	
	/**
	 * 下位ファイル数でフィルタリングします。
	 *
	 * @param orgDirs フィルタリング前のディレクトリ一覧
	 * @param maxMin max属性とmin属性
	 * @param baseDir ルートディレクトリ
	 * @return フィルタリング後のディレクトリ一覧
	 */
	private String[] filterSubFileNum(String[] orgDirs, MaxminType maxMin, File baseDir){
		List<String> newDirs = new ArrayList<String>();
		for (String dir : orgDirs) {
			long num = getSubFileNum(new File(baseDir, dir));
			if (LOG.isDebugEnabled()) LOG.debug("subFileNum=" + num + ", dir=" + dir);
			if (Searcher.judgeMaxMin(num, maxMin)){
				newDirs.add(dir);
			}
		}
		return newDirs.toArray(new String[0]);
	}
	
	/**
	 * 下位ディレクトリ数でフィルタリングします。
	 *
	 * @param orgDirs フィルタリング前のディレクトリ一覧
	 * @param maxMin max属性とmin属性
	 * @param baseDir ルートディレクトリ
	 * @return フィルタリング後のディレクトリ一覧
	 */
	private String[] filterSubDirNum(String[] orgDirs, MaxminType maxMin, File baseDir){
		List<String> newDirs = new ArrayList<String>();
		for (String dir : orgDirs) {
			long num = getSubDirNum(new File(baseDir, dir));
			if (LOG.isDebugEnabled()) LOG.debug("subDirNum=" + num + ", dir=" + dir);
			if (Searcher.judgeMaxMin(num, maxMin)){
				newDirs.add(dir);
			}
		}
		return newDirs.toArray(new String[0]);
	}
	
	/**
	 * 下位ファイル数を返します。
	 *
	 * @param dir 対象ディレクトリ
	 * @return 対象ディレクトリの下位ファイル数
	 */
	private long getSubFileNum(File dir){
		File[] elems = dir.listFiles();
		if (elems == null){
			LOG.warn("下位要素を参照できません：dir=" + dir.getPath());
			return 0;
		}
		long num = 0;
		for (File elem : elems){
			if (elem.isFile()){
				num ++;
			}
		}
		return num;
	}
	
	/**
	 * 下位ディレクトリ数を返します。
	 *
	 * @param dir 対象ディレクトリ
	 * @return 対象ディレクトリの下位ディレクトリ数
	 */
	private long getSubDirNum(File dir){
		File[] elems = dir.listFiles();
		if (elems == null){
			LOG.warn("下位要素を参照できません：dir=" + dir.getPath());
			return 0;
		}
		long num = 0;
		for (File elem : elems){
			if (elem.isDirectory()){
				num ++;
			}
		}
		return num;
	}
}
