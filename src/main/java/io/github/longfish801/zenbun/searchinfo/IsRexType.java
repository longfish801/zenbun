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
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>isRexType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="isRexType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;boolean"&gt;
 *       &lt;attribute name="sensitiveCase" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isRexType", propOrder = {
    "value"
})
public class IsRexType {

    @XmlValue
    protected boolean value;
    @XmlAttribute(name = "sensitiveCase")
    protected Boolean sensitiveCase;

    /**
     * valueプロパティの値を取得します。
     * 
     */
    public boolean isValue() {
        return value;
    }

    /**
     * valueプロパティの値を設定します。
     * 
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * sensitiveCaseプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSensitiveCase() {
        if (sensitiveCase == null) {
            return true;
        } else {
            return sensitiveCase;
        }
    }

    /**
     * sensitiveCaseプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSensitiveCase(Boolean value) {
        this.sensitiveCase = value;
    }

}
