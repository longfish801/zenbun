/*
 * Searcher.java
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import io.github.longfish801.zenbun.searchinfo.*;
import io.github.longfish801.zenbun.xml.JAXBUse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファイルシステムを対象としたテキスト検索を実施します。
 * 
 * @version 1.0.00 2014/09/12
 * @author io.github.longfish801
 */
public class Searcher {
	/** ログ出力インスタンス */
	private static final Logger LOG = LoggerFactory.getLogger(Searcher.class);
	/** 検索情報 */
	private SearchInfo searchInfo = null;
	
	/**
	 * コンストラクタ。
	 * @throws IOException XMLファイルあるいはXML Schema定義ファイルを参照できません。
	 * @throws JAXBException XMLのバインディングに失敗しました。
	 */
	public Searcher() throws IOException, JAXBException {
		searchInfo = (SearchInfo) JAXBUse.getObject(SearchInfo.class);
	}
	
	/**
	 * 検索情報を返します。
	 *
	 * @return SearchInfo
	 */
	public SearchInfo getSearchInfo(){
		return this.searchInfo;
	}
	
	/**
	 * ディレクトリを検索し、結果を返します。
	 *
	 * @return 検索条件を満たしたディレクトリの一覧を格納した検索情報
	 */
	public SearchInfo searchDir(){
		DirSearcher searcher = new DirSearcher(searchInfo.getDirCond());
		ResultsType results = searcher.search();
		searchInfo.setResults(results);
		return searchInfo;
	}
	
	/**
	 * ファイルを検索し、結果を返します。
	 *
	 * @return 検索条件を満たしたファイルの一覧を格納した検索情報
	 */
	public SearchInfo searchFile(){
		if (searchInfo.getFileCond() == null){
			LOG.warn("ファイル検索条件が指定されていません。");
			return null;
		}
		searchDir();
		FileSearcher searcher = new FileSearcher(searchInfo.getFileCond());
		long fileNum = 0;
		for (DirType dir : searchInfo.getResults().getDir()){
			fileNum += searcher.search(dir);
		}
		LOG.debug("fileNum=" + fileNum);
		searchInfo.getResults().setFileNum(fileNum);
		return searchInfo;
	}
	
	/**
	 * ファイル内テキストを検索し、結果を返します。
	 *
	 * @return 検索条件を満たしたファイル内テキストの一覧を格納した検索情報
	 */
	public SearchInfo searchText(){
		if (searchInfo.getTextCond() == null){
			LOG.warn("ファイル内テキスト検索条件が指定されていません。");
			return null;
		}
		searchFile();
		TextSearcher searcher = new TextSearcher(searchInfo.getTextCond());
		long hitNum = 0;
		for (DirType dir : searchInfo.getResults().getDir()){
			for (FileType file : dir.getFile()){
				hitNum += searcher.search(file);
			}
		}
		LOG.debug("hitNum=" + hitNum);
		searchInfo.getResults().setHitNum(hitNum);
		return searchInfo;
	}
	
	/**
	 * ファイル内テキストを置換し、結果を返します。
	 *
	 * @return 検索条件を満たし置換を実施したファイル内テキストの一覧を格納した検索情報
	 */
	public SearchInfo replaceText(){
		if (searchInfo.getTextCond() == null){
			LOG.warn("ファイル内テキスト検索条件が指定されていません。");
			return null;
		}
		if (searchInfo.getReplaceCond() == null){
			LOG.warn("ファイル内テキスト置換条件が指定されていません。");
			return null;
		}
		searchFile();
		TextReplacer replacer = new TextReplacer(searchInfo.getTextCond(), searchInfo.getReplaceCond());
		long hitNum = 0;
		for (DirType dir : searchInfo.getResults().getDir()){
			for (FileType file : dir.getFile()){
				hitNum += replacer.replace(file);
			}
		}
		searchInfo.getResults().setHitNum(hitNum);
		return searchInfo;
	}
	
	/**
	 * 最大値/最小値の条件を満たすか判定します。
	 *
	 * @param num 判定対象の数値
	 * @param maxMin max属性とmin属性
	 * @return 判定結果
	 */
	public static boolean judgeMaxMin(long num, MaxminType maxMin){
		boolean jdg = false;
		long max = maxMin.getMax();
		long min = maxMin.getMin();
		
		// max属性とmin属性のどちらも指定されていない場合は falseを返します
		if (max == -1 && min == -1){
			return false;
		}
		
		// max属性とmin属性の値に基づき判定します
		if (max == -1){
			if (num >= min){
				jdg = true;
			}
		} else if (min == -1){
			if (num <= max){
				jdg = true;
			}
		} else {
			if (min <= max){
				if (num >= min && num <= max){
					jdg = true;
				}
			} else {
				if (num >= min || num <= max){
					jdg = true;
				}
			}
		}
		return jdg;
	}
	
	/**
	 * 開始日時/終端日時の条件を満たすか判定します。
	 *
	 * @param date 判定対象の日時
	 * @param fromTo from属性とto属性
	 * @return 判定結果
	 */
	public static boolean judgeMaxMin(Date date, FromtoType fromTo){
		boolean jdg = false;
		Calendar from = convertXMLGregorianCalendarToCalendar(fromTo.getFrom());
		Calendar to = convertXMLGregorianCalendarToCalendar(fromTo.getTo());
		
		// from属性とto属性のどちらも指定されていない場合は falseを返します
		if (from == null && to == null){
			return false;
		}
		
		// 判定対象をCalendarインスタンスに変換します
		Calendar clndr = Calendar.getInstance();
		clndr.setTime(date);
		
		// from属性とto属性の値に基づき判定します
		if (to == null){
			if (clndr.compareTo(from) >= 0){
				jdg = true;
			}
		} else if (from == null){
			if (clndr.compareTo(to) <= 0){
				jdg = true;
			}
		} else {
			if (to.compareTo(from) >= 0){
				if (clndr.compareTo(from) >= 0 && clndr.compareTo(to) <= 0){
					jdg = true;
				}
			} else {
				if (clndr.compareTo(from) >= 0 || clndr.compareTo(to) <= 0){
					jdg = true;
				}
			}
		}
		return jdg;
	}
	
	/**
	 * XMLGregorianCalendarをCalendarに変換して返す。
	 *
	 * @param xmlGregorianCalendar XMLGregorianCalendar
	 * @return Calendar（引数がnullの場合はnullを返す）
	 */
	public static Calendar convertXMLGregorianCalendarToCalendar(XMLGregorianCalendar xmlGregorianCalendar){
		if (xmlGregorianCalendar == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		int year = xmlGregorianCalendar.getYear();
		int month = xmlGregorianCalendar.getMonth();
		int day = xmlGregorianCalendar.getDay();
		int hour = xmlGregorianCalendar.getHour();
		int minute = xmlGregorianCalendar.getMinute();
		int second = xmlGregorianCalendar.getSecond();
		int millisecond = xmlGregorianCalendar.getMillisecond();
		calendar.set(year, month - 1, day, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return calendar;
	}
}
