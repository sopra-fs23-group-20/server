package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "REGIONSET")
public class RegionSet {

    @Id
    @GeneratedValue
    private Long regionSetId;

    @Embedded
    private Region currentRegion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SELECTEDREGIONS", joinColumns = @JoinColumn(name = "regionSetId"))
    @Column(name = "regionEnum")
    @Enumerated(EnumType.STRING)
    private Set<RegionEnum> selectedRegions;

    public RegionSet() {
        selectedRegions = new HashSet<>();
        currentRegion = new Region();
    }

    public Set<RegionEnum> getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(Set<RegionEnum> selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(Region currentRegion) {
        this.currentRegion = currentRegion;
    }

    public Long getRegionSetId() {
        return regionSetId;
    }

    public void setRegionSetId(Long regionSetId) {
        this.regionSetId = regionSetId;
    }
}

