package org.geotoolkit.data.model.xal;

/**
 * <p>This interface maps AdministrativeAreaName type.</p>
 *
 * <p>Name of the administrative area. eg. MI in USA, NSW in Australia.</p>
 *
 * <br />&lt;xs:complexType mixed="true">
 * <br />&lt;xs:attribute name="Type"/>
 * <br />&lt;xs:attributeGroup ref="grPostal"/>
 * <br />&lt;xs:anyAttribute namespace="##other"/>
 * <br />&lt;/xs:complexType>
 *
 * @author Samuel Andrés
 */
public interface AdministrativeAreaName {

    /**
     *
     * @return
     */
    public String getType();

    /**
     *
     * @return
     */
    public GrPostal getGrPostal();
}
