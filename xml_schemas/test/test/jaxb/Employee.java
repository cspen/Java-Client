//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.04 at 10:49:18 PM CST 
//


package test.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="employeeID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="last_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="first_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="department" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="full_time" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="hire_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="salary" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="etag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="last_modified" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "employeeID",
    "lastName",
    "firstName",
    "department",
    "fullTime",
    "hireDate",
    "salary",
    "etag",
    "lastModified"
})
@XmlRootElement(name = "Employee")
public class Employee {

    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger employeeID;
    @XmlElement(name = "last_name", required = true)
    protected String lastName;
    @XmlElement(name = "first_name", required = true)
    protected String firstName;
    @XmlElement(required = true)
    protected String department;
    @XmlElement(name = "full_time")
    protected boolean fullTime;
    @XmlElement(name = "hire_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar hireDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger salary;
    @XmlElement(required = true)
    protected String etag;
    @XmlElement(name = "last_modified", required = true)
    protected String lastModified;

    /**
     * Gets the value of the employeeID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the value of the employeeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEmployeeID(BigInteger value) {
        this.employeeID = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the fullTime property.
     * 
     */
    public boolean isFullTime() {
        return fullTime;
    }

    /**
     * Sets the value of the fullTime property.
     * 
     */
    public void setFullTime(boolean value) {
        this.fullTime = value;
    }

    /**
     * Gets the value of the hireDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHireDate() {
        return hireDate;
    }

    /**
     * Sets the value of the hireDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHireDate(XMLGregorianCalendar value) {
        this.hireDate = value;
    }

    /**
     * Gets the value of the salary property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSalary() {
        return salary;
    }

    /**
     * Sets the value of the salary property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSalary(BigInteger value) {
        this.salary = value;
    }

    /**
     * Gets the value of the etag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtag() {
        return etag;
    }

    /**
     * Sets the value of the etag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtag(String value) {
        this.etag = value;
    }

    /**
     * Gets the value of the lastModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * Sets the value of the lastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastModified(String value) {
        this.lastModified = value;
    }

}
