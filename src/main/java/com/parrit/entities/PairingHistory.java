package com.parrit.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PairingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(targetEntity = Person.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_one_id")
    private Person personOne;

    @ManyToOne(targetEntity = Person.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_two_id")
    private Person personTwo;

    private Timestamp timestamp;

    private String spaceName;

    public PairingHistory() {
    }

    public PairingHistory(Project project, Person personOne, Person personTwo, Timestamp timestamp, String spaceName) {
        this.project = project;
        this.personOne = personOne;
        this.personTwo = personTwo;
        this.timestamp = timestamp;
        this.spaceName = spaceName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Person getPersonOne() {
        return personOne;
    }

    public void setPersonOne(Person personOne) {
        this.personOne = personOne;
    }

    public Person getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(Person personTwo) {
        this.personTwo = personTwo;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairingHistory)) return false;

        PairingHistory that = (PairingHistory) o;

        if (getId() != that.getId()) return false;
        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null)
            return false;
        if (getPersonOne() != null ? !getPersonOne().equals(that.getPersonOne()) : that.getPersonOne() != null)
            return false;
        if (getPersonTwo() != null ? !getPersonTwo().equals(that.getPersonTwo()) : that.getPersonTwo() != null)
            return false;
        if (getTimestamp() != null ? !getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() != null)
            return false;
        return getSpaceName() != null ? getSpaceName().equals(that.getSpaceName()) : that.getSpaceName() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        result = 31 * result + (getPersonOne() != null ? getPersonOne().hashCode() : 0);
        result = 31 * result + (getPersonTwo() != null ? getPersonTwo().hashCode() : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        result = 31 * result + (getSpaceName() != null ? getSpaceName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PairingHistory{" +
            "id=" + id +
            ", project=" + project +
            ", personOne=" + personOne +
            ", personTwo=" + personTwo +
            ", timestamp=" + timestamp +
            ", spaceName='" + spaceName + '\'' +
            '}';
    }
}
