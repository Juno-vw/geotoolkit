package org.geotoolkit.data.model.xal;

import java.util.List;

/**
 * <p>This interface maps Premise element.</p>
 *
 * <p>Specification of a single premise, for example a house or a building.
 * The premise as a whole has a unique premise (house) number or a premise name.
 * There could be more than one premise in a street referenced in an address.
 * For example a building address near a major shopping centre or raiwlay station</p>
 *
 * <br />&lt;xs:element name="Premise">
 * <br />&lt;xs:complexType>
 * <br />&lt;xs:sequence>
 * <br />&lt;xs:element ref="AddressLine" minOccurs="0" maxOccurs="unbounded"/>
 * <br />&lt;xs:element name="PremiseName" minOccurs="0" maxOccurs="unbounded">...
 * <br />&lt;/xs:element>
 * <br />&lt;xs:choice minOccurs="0">
 * <br />&lt;xs:element name="PremiseLocation">...
 * <br />&lt;/xs:element>
 * <br />&lt;xs:choice>
 * <br />&lt;xs:element ref="PremiseNumber" maxOccurs="unbounded"/>
 * <br />&lt;xs:element name="PremiseNumberRange">...
 * <br />&lt;/xs:element>
 * <br />&lt;/xs:choice>
 * <br />&lt;/xs:choice>
 * <br />&lt;xs:element ref="PremiseNumberPrefix" minOccurs="0" maxOccurs="unbounded"/>
 * <br />&lt;xs:element ref="PremiseNumberSuffix" minOccurs="0" maxOccurs="unbounded"/>
 * <br />&lt;xs:element name="BuildingName" type="BuildingNameType" minOccurs="0" maxOccurs="unbounded">...
 * <br />&lt;/xs:element>
 * <br />&lt;xs:choice>
 * <br />&lt;xs:element name="SubPremise" type="SubPremiseType" minOccurs="0" maxOccurs="unbounded">...
 * <br />&lt;/xs:element>
 * <br />&lt;xs:element name="Firm" type="FirmType" minOccurs="0">...
 * <br />&lt;/xs:element>
 * <br />&lt;/xs:choice>
 * <br />&lt;xs:element name="MailStop" type="MailStopType" minOccurs="0">...
 * <br />&lt;/xs:element>
 * <br />&lt;xs:element ref="PostalCode" minOccurs="0"/>
 * <br />&lt;xs:element ref="Premise" minOccurs="0"/>
 * <br />&lt;xs:any namespace="##other" minOccurs="0" maxOccurs="unbounded"/>
 * <br />&lt;/xs:sequence>
 * <br />&lt;xs:attribute name="Type">...
 * <br />&lt;/xs:attribute>
 * <br />&lt;xs:attribute name="PremiseDependency">...
 * <br />&lt;/xs:attribute>
 * <br />&lt;xs:attribute name="PremiseDependencyType">
 * <br />&lt;/xs:attribute>
 * <br />&lt;xs:attribute name="PremiseThoroughfareConnector">...
 * <br />&lt;/xs:attribute>
 * <br />&lt;xs:anyAttribute namespace="##other"/>
 * <br />&lt;/xs:complexType>
 * <br />&lt;/xs:element>
 *
 * @author Samuel Andrés
 */
public interface Premise {

    /**
     *
     * @return
     */
    public List<AddressLine> getAddressLines();

    /**
     *
     * @return
     */
    public List<PremiseName> getPremiseNames();

    /*
     * === CHOIX: ===
     */

    /**
     *
     * @return
     */
    public PremiseLocation getPremiseLocation();

    /**
     *
     * @return
     */
    public List<PremiseNumber> getPremiseNumbers();

    /**
     *
     * @return
     */
    public PremiseNumberRange getPremiseNumberRange();

    /*
     * === END OF CHOICE ===
     */

    /**
     *
     * @return
     */
    public List<PremiseNumberPrefix> getPremiseNumberPrefix();

    /**
     *
     * @return
     */
    public List<PremiseNumberSuffix> getPremiseNumberSuffix();
    
    /**
     * <p>Specification of the name of a building.</p>
     * 
     * @return
     */
    public List<BuildingName> getBuildingNames();

    /*
     * === CHOICE : ===
     */

    /**
     * <p>Specification of a single sub-premise.
     * Examples of sub-premises are apartments and suites.
     * Each sub-premise should be uniquely identifiable.</p>
     *
     * @return
     */
    public List<SubPremise> getSubPremises();

    /**
     * <p>Specification of a firm, company, organization, etc.
     * It can be specified as part of an address that contains a street or a postbox.
     * It is therefore different from a large mail user address,
     * which contains no street.</p>
     *
     * @return
     */
    public Firm getFirm();

    /*
     * === END OF CHOICE ===
     */

    /**
     * <p>A MailStop is where the the mail is delivered to within a premise/subpremise/firm or a facility.</p>
     *
     * @return
     */
    public MailStop getMailStop();

    /**
     *
     * @return
     */
    public PostalCode getPostalCode();

    /**
     *
     * @return
     */
    public Premise getPremise();

    /*
     * === ATTRIBUTES ===
     */

    /**
     * <p>COMPLEXE in COMPLEX DES JARDINS, A building, station, etc.</p>
     *
     * @return
     */
    public String getType();

    /**
     * <p>STREET, PREMISE, SUBPREMISE, PARK, FARM, etc.</p>
     *
     * @return
     */
    public String getPremiseDependency();

    /**
     * <p>NEAR, ADJACENT TO, etc.</p>
     *
     * @return
     */
    public String getPremiseDependencyType();

    /**
     * <p>DES, DE, LA, LA, DU in RUE DU BOIS.
     * These terms connect a premise/thoroughfare type and premise/thoroughfare name.
     * Terms may appear with names AVE DU BOIS</p>
     *
     * @return
     */
    public String getPremiseThoroughfareConnector();
}
