/*
 * TextSearcher.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import io.github.longfish801.zenbun.searchinfo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファイル内テキストを検索します。
 * 
 * @version 1.0.00 2014/09/12
 * @author io.github.longfish801
 */
class TextSearcher {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(TextSearcher.class);
	/** テキスト検索条件 */
	protected TextCondType cond = null;
	/** 検索結果リスト */
	protected List<ResultType> resultList = null;
	/** キーワード毎ヒット有無マップ */
	private Map<String, Boolean> hitMap = null;
	/** Patternマップ */
	private Map<String, Pattern> patternMap = null;
	/** 正規表現か否か */
	protected boolean isRex = true;
	
	/**
	 * コンストラクタ。
	 *
	 * @param cond TextCondType
	 */
	public TextSearcher(TextCondType cond){
		this.cond = cond;
		
		// キーワード毎ヒット有無マップを初期化します
		hitMap = new HashMap<String, Boolean>();
		List<String> keywords = cond.getKeywords().getKeyword();
		for (String keyword : keywords){
			hitMap.put(keyword, new Boolean(false));
		}
		
		// Patternマップを初期化します
		isRex = (cond.getIsRex() == null)? true : cond.getIsRex().isValue();
		if (isRex){
			boolean isSensitive = (cond.getIsRex() == null)? true : cond.getIsRex().isSensitiveCase();
			patternMap = new HashMap<String, Pattern>();
			for (String keyword : keywords){
				if (isSensitive){
					patternMap.put(keyword, Pattern.compile(keyword));
				} else {
					patternMap.put(keyword, Pattern.compile(keyword, Pattern.UNICODE_CASE));
				}
			}
		}
	}
	
	/**
	 * ファイル内テキストを検索します。
	 *
	 * @param fileType 検索対象ファイル
	 * @return 検索結果件数
	 */
	public long search(FileType fileType){
		// 検索結果リストを初期化します
		resultList = fileType.getResult();
		
		// ファイルを一行ずつ走査します
		File file = new File(fileType.getPath());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileType.getEncoding()));
			String line;
			long lineNum = 0;
			while((line = reader.readLine()) != null){
				boolean cont = true;
				if (isRex){
					cont = searchEachLineByRegex(line, ++ lineNum);
				} else {
					cont = searchEachLine(line, ++ lineNum);
				}
				if (!cont){
					break;
				}
			}
		} catch (IOException exc){
			LOG.warn("ファイル読込中に入出力例外が発生しました：path=" + fileType.getPath(), exc);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException exc){
				LOG.warn("ストリームを閉じるときに入出力例外が発生しました：path=" + fileType.getPath(), exc);
			}
		}
		
		// 検索結果をフィルタリングします
		filterResult();
		
		// 検索結果件数を返します
		return resultList.size();
	}
	
	/**
	 * 検索結果をフィルタリングします。
	 *
	 */
	private void filterResult(){
		// AND/OR条件でフィルタリングします
		boolean isAnd = cond.getKeywords().isIsAnd();
		boolean jdg = (isAnd)? true : false;
		for (String keyword : hitMap.keySet()) {
			if (hitMap.get(keyword).booleanValue() == !isAnd){
				jdg = !isAnd;
				break;
			}
		}
		if (LOG.isDebugEnabled()) LOG.debug("jdg=" + jdg);
		if (!jdg){
			resultList.clear();
		}
	}
	
	/**
	 * 行毎に固定文字列を検索します。
	 *
	 * @param line 一行の文字列
	 * @param lineNum 行番号
	 * @return 検索を続けるべきか
	 */
	public boolean searchEachLine(String line, long lineNum){
		List<String> keywords = cond.getKeywords().getKeyword();
		boolean cont = true;
		for (String keyword : keywords){
			int fromIdx = 0;
			while ((fromIdx + keyword.length()) < line.length()){
				int index = line.indexOf(keyword, fromIdx);
				if (index < 0) break;
				fromIdx = index + keyword.length();
				// 最低一件でもキーワードがマッチしたことを記録します
				hitMap.put(keyword, new Boolean(true));
				// 検索結果のインスタンスを生成します
				ResultType result = new ResultType();
				result.setParagraph(line);
				result.setLineNum(lineNum);
				result.setColNum(index + 1);
				List<GroupType> groups = result.getGroup();
				GroupType group = new GroupType();
				group.setIndex(0);
				group.setValue(keyword);
				groups.add(group);
				resultList.add(result);
				// 検索結果件数が打ち切り件数以上になったなら、検索を終了します
				if (cond.getDisposeNum() != null && resultList.size() > cond.getDisposeNum()){
					if (LOG.isDebugEnabled()) LOG.trace("DISPOSE resultList.size()=" + resultList.size());
					cont = false;
					break;
				}
			}
		}
		return cont;
	}
	
	/**
	 * 行毎に正規表現で検索します。
	 *
	 * @param line 一行の文字列
	 * @param lineNum 行番号
	 * @return 検索を続けるべきか
	 */
	public boolean searchEachLineByRegex(String line, long lineNum){
		List<String> keywords = cond.getKeywords().getKeyword();
		boolean cont = true;
		for (String keyword : keywords){
			Pattern pattern = patternMap.get(keyword);
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()){
				int start = matcher.start();
				int end = matcher.end();
				// 最低一件でもキーワードがマッチしたことを記録します
				hitMap.put(keyword, new Boolean(true));
				// 検索結果のインスタンスを生成します
				ResultType result = new ResultType();
				result.setParagraph(line);
				result.setLineNum(lineNum);
				result.setColNum(start + 1);
				List<GroupType> groups = result.getGroup();
				for (int idx = 0; idx <= matcher.groupCount(); idx ++) {
					GroupType group = new GroupType();
					group.setIndex(idx);
					group.setValue(matcher.group(idx));
					groups.add(group);
				}
				resultList.add(result);
				// 検索結果件数が打ち切り件数以上になったなら、検索を終了します
				if (cond.getDisposeNum() != null && resultList.size() > cond.getDisposeNum()){
					if (LOG.isDebugEnabled()) LOG.trace("DISPOSE resultList.size()=" + resultList.size());
					cont = false;
					break;
				}
			}
		}
		return cont;
	}
}
