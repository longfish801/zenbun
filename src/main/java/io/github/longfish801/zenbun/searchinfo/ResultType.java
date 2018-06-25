//
// このファイルは、JavaTM Architecture for XML Binding(JAXB) Reference Implementation、v2.2.11によって生成されました 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>を参照してください 
// ソース・スキーマの再コンパイル時にこのファイルの変更は失われます。 
// 生成日: 2018.06.20 時間 09:28:33 PM JST 
//


package io.github.longfish801.zenbun.searchinfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>resultType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="resultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="paragraph" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *         &lt;element name="group" type="{http://longfish801.github.io/zenbun/searchinfo}groupType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="replaced" type="{http://www.w3.org/2001/XMLSchema}normalizedString"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="lineNum" use="required" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="colNum" use="required" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultType", propOrder = {
    "paragraph",
    "group",
    "replaced"
})
public class ResultType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String paragraph;
    @XmlElement(required = true)
    protected List<GroupType> group;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String replaced;
    @XmlAttribute(name = "lineNum", required = true)
    protected long lineNum;
    @XmlAttribute(name = "colNum", required = true)
    protected long colNum;

    /**
     * paragraphプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParagraph() {
        return paragraph;
    }

    /**
     * paragraphプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParagraph(String value) {
        this.paragraph = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the group property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupType }
     * 
     * 
     */
    public List<GroupType> getGroup() {
        if (group == null) {
            group = new ArrayList<GroupType>();
        }
        return this.group;
    }

    /**
     * replacedプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplaced() {
        return replaced;
    }

    /**
     * replacedプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplaced(String value) {
        this.replaced = value;
    }

    /**
     * lineNumプロパティの値を取得します。
     * 
     */
    public long getLineNum() {
        return lineNum;
    }

    /**
     * lineNumプロパティの値を設定します。
     * 
     */
    public void setLineNum(long value) {
        this.lineNum = value;
    }

    /**
     * colNumプロパティの値を取得します。
     * 
     */
    public long getColNum() {
        return colNum;
    }

    /**
     * colNumプロパティの値を設定します。
     * 
     */
    public void setColNum(long value) {
        this.colNum = value;
    }

}
