package com.tys.ms.model;

import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="APP_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 6859321777828935480L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name="JOB_ID", unique=true, nullable=false)
    private String jobId;

    @Transient
    private String oldPassword;

    @NotEmpty
    @Column(name="PASSWORD", nullable=false)
    private String password;

    @Transient
    private String retypePassword;

    @NotEmpty
    @Column(name="NAME", nullable=false)
    private String name;

    @NotEmpty
    @Column(name="PHONE", nullable=false)
    private String phone;

    @NotEmpty
    @Column(name="LEADER_ID",  nullable=false)
    private String leaderId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "APP_USER_USER_PROFILE",
            joinColumns = { @JoinColumn(name = "USER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
    private UserProfile userProfile;

    @Column(name="HAS_LOCKED", nullable=false)
    private boolean hasLocked = false;

    @Column(name="HAS_PASSED", nullable=false)
    private boolean hasPassed = true;

    public User(String jobId, String oldPassword, String password, String retypePassword, String name, String phone, String leaderId, UserProfile userProfile, boolean hasLocked, boolean hasPassed) {
        this.jobId = jobId;
        this.oldPassword = oldPassword;
        this.password = password;
        this.retypePassword = retypePassword;
        this.name = name;
        this.phone = phone;
        this.leaderId = leaderId;
        this.userProfile = userProfile;
        this.hasLocked = hasLocked;
        this.hasPassed = hasPassed;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isHasLocked() {
        return hasLocked;
    }

    public void setHasLocked(boolean hasLocked) {
        this.hasLocked = hasLocked;
    }

    public boolean isHasPassed() {
        return hasPassed;
    }

    public void setHasPassed(boolean hasPassed) {
        this.hasPassed = hasPassed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jobId == null) {
            if (other.jobId != null)
                return false;
        } else if (!jobId.equals(other.jobId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id
                + ", jobId=" + jobId
                + ", name=" + name
                + ", phone=" + phone
                + ", leaderId=" + leaderId
                + "]";
    }
}
