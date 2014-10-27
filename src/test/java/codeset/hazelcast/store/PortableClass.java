package codeset.hazelcast.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

/**
 * Sample domain object with a handful of different property types.
 * 
 * @author ingemar.svensson
 *
 */
public class PortableClass implements Portable {

    private Date dateProperty;
    private Integer intProperty;
    private Long longProperty;
    private Double doubleProperty;
    private String stringProperty;
    private Boolean booleanProperty;
    private Byte byteProperty;
    private Character charProperty;
    private Float floatProperty;
    private Short shortProperty;
    private NestedPortableClass nestedProperty;
    private List<NestedPortableClass> listProperty = new ArrayList<>();
    private Set<NestedPortableClass> setProperty = new HashSet<>();

    @Override
    public void readPortable(PortableReader reader) throws IOException {
    }

    @Override
    public void writePortable(PortableWriter writer) throws IOException {
    }

    @Override
    public int getClassId() {
        return 1;
    }

    @Override
    public int getFactoryId() {
        return 1;
    }

    public Date getDateProperty() {
        return dateProperty;
    }

    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }

    public Integer getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(Integer intProperty) {
        this.intProperty = intProperty;
    }

    public Long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
    }

    public Double getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(Double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public NestedPortableClass getNestedProperty() {
        return nestedProperty;
    }

    public void setNestedProperty(NestedPortableClass nestedProperty) {
        this.nestedProperty = nestedProperty;
    }

    public List<NestedPortableClass> getListProperty() {
        return listProperty;
    }

    public void setListProperty(List<NestedPortableClass> listProperty) {
        this.listProperty = listProperty;
    }

    public Byte getByteProperty() {
        return byteProperty;
    }

    public void setByteProperty(Byte byteProperty) {
        this.byteProperty = byteProperty;
    }

    public Character getCharProperty() {
        return charProperty;
    }

    public void setCharProperty(Character charProperty) {
        this.charProperty = charProperty;
    }

    public Float getFloatProperty() {
        return floatProperty;
    }

    public void setFloatProperty(Float floatProperty) {
        this.floatProperty = floatProperty;
    }

    public Short getShortProperty() {
        return shortProperty;
    }

    public void setShortProperty(Short shortProperty) {
        this.shortProperty = shortProperty;
    }

    public Set<NestedPortableClass> getSetProperty() {
        return setProperty;
    }

    public void setSetProperty(Set<NestedPortableClass> setProperty) {
        this.setProperty = setProperty;
    }

}