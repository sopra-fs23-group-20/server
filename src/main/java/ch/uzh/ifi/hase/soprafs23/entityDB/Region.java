package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Region {

    private RegionEnum type;

    public RegionEnum getType() {
        return type;
    }

    public void setType(RegionEnum type) {
        this.type = type;
    }

}
