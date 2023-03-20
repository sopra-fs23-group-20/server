package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.util.Date;

public class UserGetDTO {

    private Long userId;
    private String username;

    private Date creation_date;
    private UserStatus status;

    private Date birthday;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creationDate) {
        this.creation_date = creationDate;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
