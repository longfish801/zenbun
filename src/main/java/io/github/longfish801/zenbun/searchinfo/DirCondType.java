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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>dirCondType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="dirCondType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="baseDir" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="includes" type="{http://longfish801.github.io/zenbun/searchinfo}includesType" minOccurs="0"/&gt;
 *         &lt;element name="excludes" type="{http://longfish801.github.io/zenbun/searchinfo}excludesType" minOccurs="0"/&gt;
 *         &lt;element name="subFileNum" type="{http://longfish801.github.io/zenbun/searchinfo}maxminType" minOccurs="0"/&gt;
 *         &lt;element name="subDirNum" type="{http://longfish801.github.io/zenbun/searchinfo}maxminType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dirCondType", propOrder = {
    "baseDir",
    "includes",
    "excludes",
    "subFileNum",
    "subDirNum"
})
public class DirCondType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String baseDir;
    protected IncludesType includes;
    protected ExcludesType excludes;
    protected MaxminType subFileNum;
    protected MaxminType subDirNum;

    /**
     * baseDirプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * baseDirプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseDir(String value) {
        this.baseDir = value;
    }

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
     * subFileNumプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link MaxminType }
     *     
     */
    public MaxminType getSubFileNum() {
        return subFileNum;
    }

    /**
     * subFileNumプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link MaxminType }
     *     
     */
    public void setSubFileNum(MaxminType value) {
        this.subFileNum = value;
    }

    /**
     * subDirNumプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link MaxminType }
     *     
     */
    public MaxminType getSubDirNum() {
        return subDirNum;
    }

    /**
     * subDirNumプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link MaxminType }
     *     
     */
    public void setSubDirNum(MaxminType value) {
        this.subDirNum = value;
    }

}
