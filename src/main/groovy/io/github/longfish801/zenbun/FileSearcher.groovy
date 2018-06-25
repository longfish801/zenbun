/*
 * FileSearcher.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.github.longfish801.zenbun.searchinfo.*;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファイルを検索します。
 * 
 * @version 1.0.00 2014/09/12
 * @author io.github.longfish801
 */
class FileSearcher {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(FileSearcher.class);
	/** ファイル検索条件 */
	private FileCondType cond = null;
	/** デフォルトエンコーディング */
	public static final String DEFAULT_ENCODING = "JISAutoDetect";
	
	/**
	 * コンストラクタ。
	 *
	 * @param cond FileCondType
	 */
	public FileSearcher(FileCondType cond){
		this.cond = cond;
	}
	
	/**
	 * ファイルを検索します。
	 *
	 * @param dirType 検索対象ディレクトリ
	 * @return 検索結果を満たしたファイル数
	 */
	public long search(DirType dirType){
		// 対象/除外ファイルのパターンを参照します
		String[] includes = [ "*" ] as String[];
		if (cond.getIncludes() != null && cond.getIncludes().getInclude().size() > 0){
			includes = cond.getIncludes().getInclude().toArray(new String[0]);
		}
		
		String[] excludes = null;
		if (cond.getExcludes() != null && cond.getExcludes().getExclude().size() > 0){
			excludes = cond.getExcludes().getExclude().toArray(new String[0]);
		}
		
		// マスク/除外マスクでフィルタリングします
		File dir = new File(dirType.getPath());
		File[] elems = dir.listFiles();
		if (elems == null || elems.length == 0){
			if (elems == null){
				LOG.warn("ディレクトリ配下のファイル一覧を参照できませんでした：path=" + dirType.getPath());
			}
			return 0;
		}
		if (LOG.isDebugEnabled()) LOG.debug("elems=" + Arrays.toString(elems));
		List<File> fileList = new ArrayList<File>();
		for (File elem : elems) {
			if (elem.isFile()){
				boolean jdg = false;
				for (String mask : includes){
					// ひとつでもマスクに合致すればOK
					if (FilenameUtils.wildcardMatch(elem.getName(), mask)){
						jdg = true;
						break;
					}
				}
				if (!jdg){
					continue;
				}
				if (excludes != null){
					for (String mask : excludes){
						// ひとつでも除外マスクに合致すればNG
						if (FilenameUtils.wildcardMatch(elem.getName(), mask)){
							jdg = false;
							break;
						}
					}
				}
				if (jdg){
					fileList.add(elem);
				}
			}
		}
		if (LOG.isDebugEnabled()) LOG.debug("elems=" + Arrays.toString(elems));
		
		// ファイルサイズでフィルタリングします
		if (cond.getSize() != null){
			fileList = filterSize(fileList, cond.getSize());
		}
		
		// 最終更新日時でフィルタリングします
		if (cond.getLastModified() != null){
			fileList = filterLastModified(fileList, cond.getLastModified());
		}
		
		// 検索結果を生成します
		List<FileType> results = dirType.getFile();
		for (File file : fileList){
			FileType fileType = new FileType();
			fileType.setPath(file.getAbsolutePath());
			fileType.setEncoding(getEncoding(file));
			results.add(fileType);
		}
		return results.size();
	}
	
	/**
	 * ファイルサイズでフィルタリングします。
	 *
	 * @param orgDirs フィルタリング前のファイル一覧
	 * @param maxMin max属性とmin属性
	 * @return フィルタリング後のファイル一覧
	 */
	private List<File> filterSize(List<File> orgFiles, MaxminType maxMin){
		List<File> newFiles = new ArrayList<File>();
		for (File file : orgFiles){
			long size = file.length();
			if (LOG.isDebugEnabled()) LOG.debug("size=" + size + ", file=" + file.getPath());
			if (Searcher.judgeMaxMin(size, maxMin)){
				newFiles.add(file);
			}
		}
		return newFiles;
	}
	
	/**
	 * 最終更新日時でフィルタリングします。
	 *
	 * @param orgDirs フィルタリング前のファイル一覧
	 * @param fromTo from属性とto属性
	 * @return フィルタリング後のファイル一覧
	 */
	private List<File> filterLastModified(List<File> orgFiles, FromtoType fromTo){
		List<File> newFiles = new ArrayList<File>();
		for (File file : orgFiles){
			Date date = new Date(file.lastModified());
			if (LOG.isDebugEnabled()) LOG.debug("date=" + date + ", file=" + file.getPath());
			if (Searcher.judgeMaxMin(date, fromTo)){
				newFiles.add(file);
			}
		}
		return newFiles;
	}
	
	/**
	 * ファイル名に適合するエンコーディングを返します。
	 *
	 * @param file 対象ファイル
	 * @return ファイル名に適合するエンコーディング
	 */
	private String getEncoding(File file){
		List<EncodingType> encodings = cond.getEncodings().getEncoding();
		String encoding = DEFAULT_ENCODING;
		for (EncodingType encodingType : encodings) {
			if (FilenameUtils.wildcardMatch(file.getName(), encodingType.getMask())){
				encoding = encodingType.getValue();
				break;
			}
		}
		return encoding;
	}
}
