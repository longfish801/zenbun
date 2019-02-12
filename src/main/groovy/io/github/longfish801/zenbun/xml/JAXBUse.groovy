/*
 * JAXBUse.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun.xml;

import groovy.util.logging.Slf4j;
import java.text.MessageFormat;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import io.github.longfish801.shared.ExchangeResource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

/**
 * JAXBを利用してXMLファイルとJavaインスタンスの相互変換を実現します。
 * 
 * @version 1.0.00 2015/08/13
 * @author io.github.longfish801
 */
@Slf4j('LOG')
class JAXBUse {
	/** ConfigObject */
	static final ConfigObject cnst = ExchangeResource.config(JAXBUse.class);
	/** Unmarshaller */
	private Unmarshaller unmarshaller = null;
	/** ValidationEventHandler */
	protected ValidationEventHandler eventHandler = null;

	/**
	 * クラス名に対応する XMLを読みこんで、XML Schemaで検証し、バインディングしたインスタンスを返します。<br>
	 * XML Schema検証にも、クラス名に対応するXML Schema定義ファイルを参照します。<br>
	 * バインディングにも同じクラスを使用します。<br>
	 * このメソッドはメンバ変数の宣言時に利用することを想定しているため、例外を投げません。
	 *
	 * @param clazz XMLファイルと XML Schema定義ファイル参照とバインディングのためのクラス
	 * @return XMLをバインディングしたインスタンス（検証エラー時は null）
	 */
	static Object getObject(Class<?> clazz){
		LOG.debug("clazz={}", clazz.simpleName);
		Object obj = null;
		try {
			InputStream xmlStream = ExchangeResource.stream(clazz, '.xml');
			InputStream xsdStream = ExchangeResource.stream(clazz, '.xsd');
			JAXBUse parser = new JAXBUse(clazz, xsdStream);
			obj = parser.unmarshal(xmlStream);
			if (parser.getErrorNum() > 0) {
				LOG.error("XMLの構文に誤りがあります（エラー件数：{}）。\n{}" + parser.getErrorNum(), parser.getErrorMessage());
			}
		} catch (exc){
			LOG.error("XMLのバインディングに失敗しました。clazz={}", clazz.simpleName, exc);
		}
		return obj;
	}

	/**
	 * コンストラクタ。
	 * XML Schemaによる検証に必要となるインスタンス生成を実施します。<br>
	 * 指定された生成対象クラスと XML Schemaの入力ストリームを{@link #setupSchema(Class,InputStream)}に渡します。
	 * @param clazz 生成対象クラス
	 * @param xsdStream XML Schemaの入力ストリーム
	 */
	JAXBUse(Class clazz, InputStream xsdStream) {
		setupSchema(clazz, xsdStream);
	}
	
	/**
	 * XML Schemaによる検証に必要となるUnmarshallerインスタンスを生成します。<br>
	 *
	 * @param clazz 生成対象クラス
	 * @param xsdStream XML Schemaの入力ストリーム
	 */
	void setupSchema(Class clazz, InputStream xsdStream){
		LOG.debug("clazz={}", clazz.simpleName);
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(xsdStream));
		unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
		unmarshaller.setSchema(schema);
		eventHandler = (ValidationEventHandler) new EventHandler();
		unmarshaller.setEventHandler(eventHandler);
	}
	
	/**
	 * XMLファイル内容をバインディングしたインスタンスを返します。
	 *
	 * @param xmlStream XMLの入力ストリーム
	 * @return XMLファイル内容をバインディングしたインスタンス
	 */
	Object unmarshal(InputStream xmlStream){
		Object obj = null;
		try {
			eventHandler.errors.clear();
			obj = unmarshaller.unmarshal(xmlStream);
		} catch (JAXBException exc) {
			LOG.warn("XML Schemaによる検証でエラーがみつかり、バインディングに失敗しました。");
			obj = null;	// XML Schema検証エラーがある場合、nullを返します
		}
		return obj;
	}
	
	/**
	 * XMLファイル内容をバインディングしたインスタンスを返します。
	 *
	 * @param node XMLの入力ノード
	 * @return XMLファイル内容をバインディングしたインスタンス
	 */
	Object unmarshal(Node node){
		Object obj = null;
		try {
			eventHandler.errors.clear();
			obj = unmarshaller.unmarshal(node);
		} catch (JAXBException exc) {
			LOG.warn("XML Schemaによる検証でエラーがみつかり、バインディングに失敗しました。");
			obj = null;	// XML Schema検証エラーがある場合、nullを返します
		}
		return obj;
	}
	
	/**
	 * エラー件数を返します。
	 * @return エラー件数
	 * @see #unmarshal(Class,InputStream,InputStream)
	 */
	int getErrorNum(){
		return eventHandler.errors.size();
	}
	
	/**
	 * バインディングにより生じたエラーについてメッセージを返します。
	 *
	 * @return エラーメッセージ
	 * @see #unmarshal(Class,InputStream,InputStream)
	 */
	String getErrorMessage(){
		List messages = [];
		for (ValidationEvent event in eventHandler.errors){
			String typeStr = "";
			switch (event.getSeverity()) {
				case ValidationEvent.FATAL_ERROR:	// 致命的エラー（整形式制約違反）
					typeStr = cnst.ENUM_SCHEMAERRORTYPE_FATAL;
					break;
				case ValidationEvent.ERROR:	// エラー（妥当性制約違反）
					typeStr = cnst.ENUM_SCHEMAERRORTYPE_ERROR;
					break;
				case ValidationEvent.WARNING:	// 警告
					typeStr = cnst.ENUM_SCHEMAERRORTYPE_WARN;
					break;
				default:
					throw new InternalError("重要度コードが不正です。type=" + event.getSeverity());
			}
			
			// エラーメッセージを作成します
			messages << MessageFormat.format(
				cnst.FORMAT_SCHEMAERROR_MESSAGE,
				typeStr,
				event.getLocator().getLineNumber(),
				event.getLocator().getColumnNumber(),
				event.getMessage()
			);
		}
		return messages.join("\n");
	}
	
	/**
	 * XML Schema検証におけるエラー発生時のハンドラクラスです。
	 */
	protected class EventHandler implements ValidationEventHandler {
		/** XML Schema検証エラー一覧 */
		List errors = [];
		
		/** {@inheritDoc} */
		boolean handleEvent(ValidationEvent event) {
			LOG.debug("event={}", event);
			errors << event;
			return false;
		}
	}
}
