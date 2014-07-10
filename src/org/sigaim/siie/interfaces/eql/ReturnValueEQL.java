/**
 * ReturnValueEQL.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sigaim.siie.interfaces.eql;

public class ReturnValueEQL  extends org.sigaim.siie.interfaces.IdentifiedReturnValue  implements java.io.Serializable {
    private java.lang.String serialized;

    public ReturnValueEQL() {
    }

    public ReturnValueEQL(
           java.lang.String reasonCode,
           java.lang.String requestId,
           java.lang.String serialized) {
        super(
            reasonCode,
            requestId);
        this.serialized = serialized;
    }


    /**
     * Gets the serialized value for this ReturnValueEQL.
     * 
     * @return serialized
     */
    public java.lang.String getSerialized() {
        return serialized;
    }


    /**
     * Sets the serialized value for this ReturnValueEQL.
     * 
     * @param serialized
     */
    public void setSerialized(java.lang.String serialized) {
        this.serialized = serialized;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReturnValueEQL)) return false;
        ReturnValueEQL other = (ReturnValueEQL) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.serialized==null && other.getSerialized()==null) || 
             (this.serialized!=null &&
              this.serialized.equals(other.getSerialized())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getSerialized() != null) {
            _hashCode += getSerialized().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReturnValueEQL.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://eql.interfaces.siie.sigaim.org", "ReturnValueEQL"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serialized");
        elemField.setXmlName(new javax.xml.namespace.QName("http://eql.interfaces.siie.sigaim.org", "serialized"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
