package ch.uzh.ifi.hase.soprafs23.entityDB;

import javax.persistence.*;

@Entity
@Table(name = "OUTLINE")
public class Outline {

    @Id
    @GeneratedValue
    @Column(name = "outlineId")
    private Long outlineId;

    @Column(length = 10000000)
    private String outline;

    public Long getOutlineId() {
        return outlineId;
    }

    public void setOutlineId(Long outlineId) {
        this.outlineId = outlineId;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }
}
