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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>resultsType complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>
 * &lt;complexType name="resultsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dir" type="{http://longfish801.github.io/zenbun/searchinfo}dirType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dirNum" use="required" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="fileNum" use="required" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="hitNum" use="required" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultsType", propOrder = {
    "dir"
})
public class ResultsType {

    @XmlElement(required = true)
    protected List<DirType> dir;
    @XmlAttribute(name = "dirNum", required = true)
    protected long dirNum;
    @XmlAttribute(name = "fileNum", required = true)
    protected long fileNum;
    @XmlAttribute(name = "hitNum", required = true)
    protected long hitNum;

    /**
     * Gets the value of the dir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDir().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DirType }
     * 
     * 
     */
    public List<DirType> getDir() {
        if (dir == null) {
            dir = new ArrayList<DirType>();
        }
        return this.dir;
    }

    /**
     * dirNumプロパティの値を取得します。
     * 
     */
    public long getDirNum() {
        return dirNum;
    }

    /**
     * dirNumプロパティの値を設定します。
     * 
     */
    public void setDirNum(long value) {
        this.dirNum = value;
    }

    /**
     * fileNumプロパティの値を取得します。
     * 
     */
    public long getFileNum() {
        return fileNum;
    }

    /**
     * fileNumプロパティの値を設定します。
     * 
     */
    public void setFileNum(long value) {
        this.fileNum = value;
    }

    /**
     * hitNumプロパティの値を取得します。
     * 
     */
    public long getHitNum() {
        return hitNum;
    }

    /**
     * hitNumプロパティの値を設定します。
     * 
     */
    public void setHitNum(long value) {
        this.hitNum = value;
    }

}
