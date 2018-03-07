

package org.geotoolkit.pending.demo.datamodel.customdatastore;

import java.util.Arrays;
import java.util.Collection;
import org.geotoolkit.data.AbstractFileFeatureStoreFactory;
import org.apache.sis.parameter.ParameterBuilder;
import org.apache.sis.storage.DataStoreException;
import org.geotoolkit.storage.ResourceType;
import org.geotoolkit.storage.StoreMetadataExt;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;

@StoreMetadataExt(resourceTypes = ResourceType.VECTOR)
public class FishDatastoreFactory extends AbstractFileFeatureStoreFactory{

    /** factory identification **/
    public static final String NAME = "fish";

    public static final ParameterDescriptor<String> IDENTIFIER = new ParameterBuilder()
            .addName(AbstractFileFeatureStoreFactory.IDENTIFIER.getName().getCode())
            .setRemarks(AbstractFileFeatureStoreFactory.IDENTIFIER.getRemarks())
            .setRequired(true)
            .create(String.class, NAME);

    public static final ParameterDescriptorGroup PARAMETERS_DESCRIPTOR =
            new ParameterBuilder().addName(NAME).createGroup(IDENTIFIER, PATH);

    @Override
    public String getDescription() {
        return "Scientific fish files (*.fsh)";
    }

    @Override
    public ParameterDescriptorGroup getOpenParameters() {
        return PARAMETERS_DESCRIPTOR;
    }

    @Override
    public FishFeatureStore open(ParameterValueGroup params) throws DataStoreException {
        ensureCanProcess(params);
        return new FishFeatureStore(params);
    }

    @Override
    public Collection<String> getSuffix() {
        return Arrays.asList("fsh");
    }

}
