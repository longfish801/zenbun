//
// このファイルは、JavaTM Architecture for XML Binding(JAXB) Reference Implementation、v2.2.11によって生成されました 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>を参照してください 
// ソース・スキーマの再コンパイル時にこのファイルの変更は失われます。 
// 生成日: 2018.06.20 時間 09:28:33 PM JST 
//


package io.github.longfish801.zenbun.searchinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>maxminType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="maxminType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}long" default="-1" /&gt;
 *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}long" default="-1" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maxminType")
public class MaxminType {

    @XmlAttribute(name = "max")
    protected Long max;
    @XmlAttribute(name = "min")
    protected Long min;

    /**
     * maxプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getMax() {
        if (max == null) {
            return -1L;
        } else {
            return max;
        }
    }

    /**
     * maxプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMax(Long value) {
        this.max = value;
    }

    /**
     * minプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getMin() {
        if (min == null) {
            return -1L;
        } else {
            return min;
        }
    }

    /**
     * minプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMin(Long value) {
        this.min = value;
    }

}
