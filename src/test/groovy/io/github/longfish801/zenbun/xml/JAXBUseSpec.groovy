/*
 * JAXBUseSpec.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun.xml;

import groovy.util.logging.Slf4j;
import io.github.longfish801.shared.lang.ExchangeResource;
import io.github.longfish801.shared.lang.PackageDirectory;
import io.github.longfish801.shared.xml.root.*;
import org.w3c.dom.Document;
import spock.lang.Specification;

/**
 * JAXBUseクラスのテスト。
 * 
 * @version 1.0.00 2015/08/18
 * @author io.github.longfish801
 */
@Slf4j('LOG')
class JAXBUseSpec extends Specification {
	/** ファイル入出力のテスト用フォルダ */
	private static final File testDir = PackageDirectory.deepDir(new File('src/test/resources'), JAXBUseSpec.class);
	
	def 'Documentを取得できること'(){
		given:
		InputStream xmlStream = new FileInputStream(new File(testDir, '001.xml'));
		InputStream xsdStream = ExchangeResource.stream(JAXBUseSpec.class, '.xsd');
		
		when:
		Root root = (new JAXBUse(Root.class, xsdStream).unmarshal(xmlStream)) as Root;
		
		then:
		root.getChild() == 'テスト';
	}
	
	def 'タグに不整合がある XMLを検証エラーとすること'(){
		given:
		String expectedErrMsg = $/【エラー】12行目 24文字目 内容:予期しない要素(URI:""、ローカル:"property")です。/$;
		InputStream xmlStream = new FileInputStream(new File(testDir, '002.xml'));
		InputStream xsdStream = ExchangeResource.stream(JAXBUseSpec.class, '.xsd');
		JAXBUse parser = new JAXBUse(Root.class, xsdStream);
		
		when:
		Root root = parser.unmarshal(xmlStream) as Root;
		String resultErrMsg = parser.getErrorMessage();
		
		then:
		root == null;
		parser.getErrorNum() == 1;
		resultErrMsg.startsWith(expectedErrMsg) == true;
	}
	
	def 'XML Schemaへの違反がある XMLを検証エラーとすること'(){
		given:
		String expectedErrMsg = $/【致命的エラー】11行目 13文字目 内容:cvc-complex-type.4: 要素'property'に属性'key'が含まれている必要があります。/$;
		InputStream xmlStream = new FileInputStream(new File(testDir, '003.xml'));
		InputStream xsdStream = ExchangeResource.stream(JAXBUseSpec.class, '.xsd');
		JAXBUse parser = new JAXBUse(Root.class, xsdStream);
		
		when:
		Root root = parser.unmarshal(xmlStream) as Root;
		String resultErrMsg = parser.getErrorMessage();
		
		then:
		root == null;
		parser.getErrorNum() == 1;
		resultErrMsg == expectedErrMsg;
	}
	
	def 'XML Schemaへの違反が二箇所ある XMLを検証エラーとすること'(){
		given:
		// JAXBは、違反が二箇所にあってもエラーはひとつのみの模様
		String expectedErrMsg = $/【致命的エラー】11行目 13文字目 内容:cvc-complex-type.4: 要素'property'に属性'key'が含まれている必要があります。/$;
		InputStream xmlStream = new FileInputStream(new File(testDir, '004.xml'));
		InputStream xsdStream = ExchangeResource.stream(JAXBUseSpec.class, '.xsd');
		JAXBUse parser = new JAXBUse(Root.class, xsdStream);
		
		when:
		Root root = parser.unmarshal(xmlStream) as Root;
		String resultErrMsg = parser.getErrorMessage();
		
		then:
		root == null;
		parser.getErrorNum() == 1;
		resultErrMsg == expectedErrMsg;
	}
}
