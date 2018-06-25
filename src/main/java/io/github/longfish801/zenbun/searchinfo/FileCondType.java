//
// このファイルは、JavaTM Architecture for XML Binding(JAXB) Reference Implementation、v2.2.11によって生成されました 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>を参照してください 
// ソース・スキーマの再コンパイル時にこのファイルの変更は失われます。 
// 生成日: 2018.06.20 時間 09:28:33 PM JST 
//


package io.github.longfish801.zenbun.searchinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>fileCondType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="fileCondType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="includes" type="{http://longfish801.github.io/zenbun/searchinfo}includesType" minOccurs="0"/&gt;
 *         &lt;element name="excludes" type="{http://longfish801.github.io/zenbun/searchinfo}excludesType" minOccurs="0"/&gt;
 *         &lt;element name="encodings" type="{http://longfish801.github.io/zenbun/searchinfo}encodingsType" minOccurs="0"/&gt;
 *         &lt;element name="size" type="{http://longfish801.github.io/zenbun/searchinfo}maxminType" minOccurs="0"/&gt;
 *         &lt;element name="lastModified" type="{http://longfish801.github.io/zenbun/searchinfo}fromtoType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileCondType", propOrder = {
    "includes",
    "excludes",
    "encodings",
    "size",
    "lastModified"
})
public class FileCondType {

    protected IncludesType includes;
    protected ExcludesType excludes;
    protected EncodingsType encodings;
    protected MaxminType size;
    protected FromtoType lastModified;

    /**
     * includesプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link IncludesType }
     *     
     */
    public IncludesType getIncludes() {
        return includes;
    }

    /**
     * includesプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link IncludesType }
     *     
     */
    public void setIncludes(IncludesType value) {
        this.includes = value;
    }

    /**
     * excludesプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link ExcludesType }
     *     
     */
    public ExcludesType getExcludes() {
        return excludes;
    }

    /**
     * excludesプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link ExcludesType }
     *     
     */
    public void setExcludes(ExcludesType value) {
        this.excludes = value;
    }

    /**
     * encodingsプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link EncodingsType }
     *     
     */
    public EncodingsType getEncodings() {
        return encodings;
    }

    /**
     * encodingsプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link EncodingsType }
     *     
     */
    public void setEncodings(EncodingsType value) {
        this.encodings = value;
    }

    /**
     * sizeプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link MaxminType }
     *     
     */
    public MaxminType getSize() {
        return size;
    }

    /**
     * sizeプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link MaxminType }
     *     
     */
    public void setSize(MaxminType value) {
        this.size = value;
    }

    /**
     * lastModifiedプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link FromtoType }
     *     
     */
    public FromtoType getLastModified() {
        return lastModified;
    }

    /**
     * lastModifiedプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link FromtoType }
     *     
     */
    public void setLastModified(FromtoType value) {
        this.lastModified = value;
    }

}
