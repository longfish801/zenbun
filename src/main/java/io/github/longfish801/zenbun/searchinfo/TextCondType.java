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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>textCondType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="textCondType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="keywords" type="{http://longfish801.github.io/zenbun/searchinfo}keywordsType"/&gt;
 *         &lt;element name="isRex" type="{http://longfish801.github.io/zenbun/searchinfo}isRexType" minOccurs="0"/&gt;
 *         &lt;element name="disposeNum" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "textCondType", propOrder = {
    "keywords",
    "isRex",
    "disposeNum"
})
public class TextCondType {

    @XmlElement(required = true)
    protected KeywordsType keywords;
    protected IsRexType isRex;
    protected Long disposeNum;

    /**
     * keywordsプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link KeywordsType }
     *     
     */
    public KeywordsType getKeywords() {
        return keywords;
    }

    /**
     * keywordsプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link KeywordsType }
     *     
     */
    public void setKeywords(KeywordsType value) {
        this.keywords = value;
    }

    /**
     * isRexプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link IsRexType }
     *     
     */
    public IsRexType getIsRex() {
        return isRex;
    }

    /**
     * isRexプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link IsRexType }
     *     
     */
    public void setIsRex(IsRexType value) {
        this.isRex = value;
    }

    /**
     * disposeNumプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDisposeNum() {
        return disposeNum;
    }

    /**
     * disposeNumプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDisposeNum(Long value) {
        this.disposeNum = value;
    }

}
