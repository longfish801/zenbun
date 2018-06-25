/*
 * BufferedReaderUntilLsSpec.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun.io;

import groovy.util.logging.Slf4j;

import io.github.longfish801.shared.util.ClassDirectory;

import spock.lang.Specification;
import spock.lang.Unroll;

/**
 * BufferedReaderUntilLsクラスのテスト。
 * 
 * @version 1.0.00 2015/08/17
 * @author io.github.longfish801
 */
@Slf4j('LOG')
class BufferedReaderUntilLsSpec extends Specification {
	/** ファイル入出力のテスト用フォルダ */
	private static final File testDir = new ClassDirectory('src/test/resources').getDeepDir(BufferedReaderUntilLsSpec.class);
	
	@Unroll
	def 'ファイル内容を正しく読みこめること'(){
		expect:
		readLinesAll(new File(testDir, fname)) == readAll(new File(testDir, expect));
		
		where:
		fname			|| expect
		'test001.txt'	|| 'test001.txt'	// 空ファイルでもファイル内容を正しく読みこめること
		'test002.txt'	|| 'test002.txt'	// 空行を含む場合もファイル内容を正しく読みこめること
		'test003.txt'	|| 'test003.txt'	// 空行を含む場合（改行コードがLF）もファイル内容を正しく読みこめること
		'test004.txt'	|| 'test004.txt'	// 行末に改行コードが無い場合もファイル内容を正しく読みこめること
		'test005.txt'	|| 'test005.txt'	// 行末に改行コードが無い場合（改行コードがLF）もファイル内容を正しく読みこめること
		'test006.txt'	|| 'test006.txt'	// 改行コードのみの場合もファイル内容を正しく読みこめること
	}

	def 'ファイルを改行コードと共に一括読込できること'(){
		expect:
		readAll(new File(testDir, 'test007.txt')) == "aaa\nbbb\nccc\n";
	}
	
	/**
	 * BufferedReaderUntilLsクラスを用いてファイル内容を改行コードも含めて一括読込します。
	 */
	private String readAll(File file){
		BufferedReaderUntilLs reader = null;
		String result = null;
		try {
			reader = new BufferedReaderUntilLs(new InputStreamReader(new FileInputStream(file), System.getProperty('file.encoding')));
			result = reader.readAll();
		} catch (exc){
			LOG.error('ファイル読込中に例外が発生しました。path={}', file.path, exc);
			throw exc;
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (exc){
				LOG.error('ストリームを閉じるときに例外が発生しました。path={}', file.path, exc);
				throw exc;
			}
		}
		return result;
	}
	
	/**
	 * BufferedReaderUntilLsクラスを用いてファイル内容をすべて読みこみます。<br>
	 * 改行コードはBufferedReaderUntilLs#readLineSeparatorで読みます。
	 */
	private String readLinesAll(File file){
		BufferedReaderUntilLs reader = null;
		StringBuilder buffer = new StringBuilder('');
		try {
			reader = new BufferedReaderUntilLs(new InputStreamReader(new FileInputStream(file), System.getProperty('file.encoding')));
			String line;
			while((line = reader.readLine()) != null){
				buffer.append(line);
				buffer.append(reader.readLineSeparator());
			}
		} catch (exc){
			LOG.error('ファイル読込中に例外が発生しました。path={}', file.path, exc);
			throw exc;
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (exc){
				LOG.error('ストリームを閉じるときに例外が発生しました。path={}', file.path, exc);
				throw exc;
			}
		}
		return buffer.toString();
	}
}
