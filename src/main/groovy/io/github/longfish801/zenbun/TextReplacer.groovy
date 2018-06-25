/*
 * TextReplacer.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import io.github.longfish801.zenbun.io.BufferedReaderUntilLs;
import io.github.longfish801.zenbun.searchinfo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファイル内テキストを置換します。
 * 
 * @version 1.0.00 2014/09/12
 * @author io.github.longfish801
 */
class TextReplacer extends TextSearcher {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(TextReplacer.class);
	/** テキスト置換条件 */
	private ReplaceCondType replaceCond = null;
	
	/**
	 * コンストラクタ。
	 *
	 * @param cond TextCondType
	 */
	public TextReplacer(TextCondType textCond, ReplaceCondType replaceCond){
		super(textCond);
		this.replaceCond = replaceCond;
	}
	
	/**
	 * ファイル内テキストを置換します。
	 *
	 * @param fileType 検索対象ファイル
	 * @return 検索結果件数
	 */
	public long replace(FileType fileType){
		// 検索を実行し、検索結果件数が０件であれば、なにもせずに終了します
		long hitNum = search(fileType);
		if (hitNum == 0){
			return hitNum;
		}
		
		// ファイルを一行ずつ走査します
		File file = new File(fileType.getPath());
		BufferedReaderUntilLs reader = null;
		StringBuilder buffer = new StringBuilder("");
		try {
			reader = new BufferedReaderUntilLs(new InputStreamReader(new FileInputStream(file), fileType.getEncoding()));
			int resultListIdx = 0;
			long lineNum = 0;
			String line;
			while((line = reader.readLine()) != null){
				++ lineNum;
				if (resultListIdx < resultList.size()){
					while (true){
						ResultType result = resultList.get(resultListIdx);
						if (result.getLineNum() != lineNum) break;
						if (isRex){
							line = replaceEachLineByRegex(line, lineNum);
						} else {
							line = replaceEachLine(line, lineNum);
						}
						result.setReplaced(line);
						resultListIdx ++;
						if (resultListIdx >= resultList.size()) break;
					}
				}
				buffer.append(line);
				buffer.append(reader.readLineSeparator());
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
		
		// 置換後の文字列をファイルに書き込みます
		bulkWrite(file, buffer.toString(), fileType.getEncoding());
		
		// 検索結果件数を返します
		return hitNum;
	}
	
	/**
	 * 行毎に固定文字列を置換します。
	 *
	 * @param line 一行の文字列
	 * @param lineNum 行番号
	 * @return 置換後の文字列
	 */
	public String replaceEachLine(String line, long lineNum){
		List<String> keywords = cond.getKeywords().getKeyword();
		for (String keyword : keywords){
			line = line.replaceAll(Pattern.quote(keyword), Matcher.quoteReplacement(replaceCond.getReplacement()));
		}
		return line;
	}
	
	/**
	 * 行毎に正規表現で置換します。
	 *
	 * @param line 一行の文字列
	 * @param lineNum 行番号
	 * @return 置換後の文字列
	 */
	public String replaceEachLineByRegex(String line, long lineNum){
		List<String> keywords = cond.getKeywords().getKeyword();
		for (String keyword : keywords){
			line = line.replaceAll(keyword, replaceCond.getReplacement());
		}
		return line;
	}
	
	/**
	 * テキストファイルに文字列を一括書込します。
	 *
	 * @param file 対象ファイル
	 * @param text 出力文字列
	 * @param encoding 文字コード
	 */
	private static void bulkWrite(File file, String text, String encoding){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			writer.write(text);
		} catch (IOException exc){
			LOG.warn("置換したファイル内容の出力時に例外が発生しました:file=" + file.getPath(), exc);
		} finally {
			try {
				if (writer != null){
					writer.close();
				}
			} catch (IOException exc){
				LOG.warn("置換したファイルのクローズ時に例外が発生しました:file=" + file.getPath());
			}
		}
	}
}
