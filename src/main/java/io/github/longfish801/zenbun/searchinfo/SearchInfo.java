//
// このファイルは、JavaTM Architecture for XML Binding(JAXB) Reference Implementation、v2.2.11によって生成されました 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>を参照してください 
// ソース・スキーマの再コンパイル時にこのファイルの変更は失われます。 
// 生成日: 2018.06.20 時間 09:28:33 PM JST 
//


package io.github.longfish801.zenbun.searchinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dirCond" type="{http://longfish801.github.io/zenbun/searchinfo}dirCondType"/&gt;
 *         &lt;element name="fileCond" type="{http://longfish801.github.io/zenbun/searchinfo}fileCondType" minOccurs="0"/&gt;
 *         &lt;element name="textCond" type="{http://longfish801.github.io/zenbun/searchinfo}textCondType" minOccurs="0"/&gt;
 *         &lt;element name="replaceCond" type="{http://longfish801.github.io/zenbun/searchinfo}replaceCondType" minOccurs="0"/&gt;
 *         &lt;element name="results" type="{http://longfish801.github.io/zenbun/searchinfo}resultsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dirCond",
    "fileCond",
    "textCond",
    "replaceCond",
    "results"
})
@XmlRootElement(name = "searchInfo")
public class SearchInfo {

    @XmlElement(required = true)
    protected DirCondType dirCond;
    protected FileCondType fileCond;
    protected TextCondType textCond;
    protected ReplaceCondType replaceCond;
    protected ResultsType results;

    /**
     * dirCondプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link DirCondType }
     *     
     */
    public DirCondType getDirCond() {
        return dirCond;
    }

    /**
     * dirCondプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link DirCondType }
     *     
     */
    public void setDirCond(DirCondType value) {
        this.dirCond = value;
    }

    /**
     * fileCondプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link FileCondType }
     *     
     */
    public FileCondType getFileCond() {
        return fileCond;
    }

    /**
     * fileCondプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link FileCondType }
     *     
     */
    public void setFileCond(FileCondType value) {
        this.fileCond = value;
    }

    /**
     * textCondプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link TextCondType }
     *     
     */
    public TextCondType getTextCond() {
        return textCond;
    }

    /**
     * textCondプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link TextCondType }
     *     
     */
    public void setTextCond(TextCondType value) {
        this.textCond = value;
    }

    /**
     * replaceCondプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link ReplaceCondType }
     *     
     */
    public ReplaceCondType getReplaceCond() {
        return replaceCond;
    }

    /**
     * replaceCondプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link ReplaceCondType }
     *     
     */
    public void setReplaceCond(ReplaceCondType value) {
        this.replaceCond = value;
    }

    /**
     * resultsプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link ResultsType }
     *     
     */
    public ResultsType getResults() {
        return results;
    }

    /**
     * resultsプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsType }
     *     
     */
    public void setResults(ResultsType value) {
        this.results = value;
    }

}
