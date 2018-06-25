/*
 * BufferedReaderUntilLs.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun.io;

import groovy.util.logging.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 行区切り文字列を参照できる BufferedReaderです。<br>
 * {@link BufferedReader#readLine()}は行区切り文字列を参照できないため作成しました。
 * 
 * @version 1.0.00 2015/08/13
 * @author io.github.longfish801
 */
@Slf4j('LOG')
class BufferedReaderUntilLs extends BufferedReader {
	/** ファイルから読み込んだ文字を格納するバッファ */
	private StringBuilder buffer = new StringBuilder("");
	/** ファイルから改行コードを格納するバッファ */
	private StringBuilder lsBuffer = new StringBuilder("");
	/** 行区切り文字列を表す正規表現 */
	private Pattern lsPattern = ~$/\r|\n|\r\n/$;
	/** 行区切り文字列 */
	private String lineSeparator = "";
	/** 改行コードを読みこんだか */
	private boolean lsFlg = false;
	
	/**
	 * コンストラクタ。
	 *
	 * @param reader Reader
	 */
	BufferedReaderUntilLs(Reader reader){
		super(reader);
	}
	
	/**
	 * ファイル内容をすべて、改行コードもそのままで一括読込します。
	 *
	 * @return ファイル内容
	 */
	String readAll(){
		StringBuilder buffer = new StringBuilder("");
		int num = 0;
		while ((num = this.read()) != -1){
			buffer.append((char) num);
		}
		return buffer.toString();
	}
	
	/**
	 * 一行を読みこんで返します。
	 *
	 * @return 一行分の文字列（行区切り文字列を含みません。ファイル終端に達した場合はnullを返します）
	 * @see {#readLineSeparator()}
	 */
	String readLine(){
		String result = null;
		int num = -1;
		while (true){
			num = this.read();
			if (num == -1){	// ファイル終端の場合
				// まだ返していない読込文字があった場合はそれを返します
				if (buffer != null){
					lineSeparator = "";
					result = buffer.toString();
					buffer = null;
				}
				// まだ返していない改行コードがあった場合はそれを追加します
				if (lsBuffer.toString().length() > 0){
					lineSeparator = lsBuffer.toString();
				}
				break;
			} else if (num as char == '\r' as char || num as char == '\n' as char){	// 改行コードの場合
				lsFlg = true;
				lineSeparator = lsBuffer.toString();
				lsBuffer.append((char) num);
				Matcher lsMatcher = lsPattern.matcher(lsBuffer.toString());
				if (!lsMatcher.matches()){	// 行区切り文字列が連続した場合
					result = buffer.toString();
					buffer = new StringBuilder("");
					lsBuffer = new StringBuilder("");
					lsBuffer.append((char) num);
					break;
				}
			} else {
				if (lsFlg){
					lsFlg = false;
					lineSeparator = lsBuffer.toString();
					lsBuffer = new StringBuilder("");
					// ひとつ前が改行コードだった場合は、それまでに読んだ文字を返します
					result = buffer.toString();
					// 読みこんだ文字（行の先頭文字）は、次に本メソッドが呼ばれたときに備えてバッファに格納しておきます
					buffer = new StringBuilder("");
					buffer.append((char) num);
					break;
				} else {
					// 読みこんだ文字をバッファに格納します
					buffer.append((char) num);
				}
			}
		}
		return result;
	}
	
	/**
	 * 行区切り文字列を返します。<br>
	 * 事前に{#readLine()}を実行している必要があります。
	 *
	 * @return 行区切り文字列（ファイル終端に達した場合や、事前に{#readLine()}を実行していなかった場合は、空文字を返します）
	 * @see {#readLine()}
	 */
	String readLineSeparator(){
		return lineSeparator;
	}
}
