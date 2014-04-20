package com.craftedbytes.domain;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;

public class User implements DataSerializable {

    private int id;
    private String displayName;
    private int reputation;
    private Date creationDate;
    private Date lastAccessDate;
    private String websiteURL;
    private String location;
    private int age;
    private String aboutMe;
    private int views;
    private int upVotes;
    private int downVotes;

    public User() {
    } // Zero Args Constructor for Serialization

    public User(int id, String displayName, int reputation, Date creationDate, Date lastAccessDate, String websiteURL, String location, int age, String aboutMe, int views, int upVotes, int downVotes) {
        this.id = id;
        this.displayName = displayName;
        this.reputation = reputation;
        this.creationDate = creationDate;
        this.lastAccessDate = lastAccessDate;
        this.websiteURL = websiteURL;
        this.location = location;
        this.age = age;
        this.aboutMe = aboutMe;
        this.views = views;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (downVotes != user.downVotes) return false;
        if (id != user.id) return false;
        if (reputation != user.reputation) return false;
        if (upVotes != user.upVotes) return false;
        if (views != user.views) return false;
        if (aboutMe != null ? !aboutMe.equals(user.aboutMe) : user.aboutMe != null) return false;
        if (creationDate != null ? !creationDate.equals(user.creationDate) : user.creationDate != null) return false;
        if (displayName != null ? !displayName.equals(user.displayName) : user.displayName != null) return false;
        if (lastAccessDate != null ? !lastAccessDate.equals(user.lastAccessDate) : user.lastAccessDate != null)
            return false;
        if (location != null ? !location.equals(user.location) : user.location != null) return false;
        if (websiteURL != null ? !websiteURL.equals(user.websiteURL) : user.websiteURL != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + reputation;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastAccessDate != null ? lastAccessDate.hashCode() : 0);
        result = 31 * result + (websiteURL != null ? websiteURL.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (aboutMe != null ? aboutMe.hashCode() : 0);
        result = 31 * result + views;
        result = 31 * result + upVotes;
        result = 31 * result + downVotes;
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", reputation=" + reputation +
                ", creationDate=" + creationDate +
                ", lastAccessDate=" + lastAccessDate +
                ", websiteURL='" + websiteURL + '\'' +
                ", location='" + location + '\'' +
                ", age=" + age +
                ", aboutMe='" + aboutMe + '\'' +
                ", views=" + views +
                ", upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getReputation() {
        return reputation;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public int getViews() {
        return views;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(id);
        objectDataOutput.writeUTF(displayName);
        objectDataOutput.writeInt(reputation);
        objectDataOutput.writeLong(creationDate.getTime());
        objectDataOutput.writeLong(lastAccessDate.getTime());
        objectDataOutput.writeUTF(websiteURL);
        objectDataOutput.writeUTF(location);
        objectDataOutput.writeInt(age);
        objectDataOutput.writeUTF(aboutMe);
        objectDataOutput.writeInt(views);
        objectDataOutput.writeInt(upVotes);
        objectDataOutput.writeInt(downVotes);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        id = objectDataInput.readInt();
        displayName = objectDataInput.readUTF();
        reputation = objectDataInput.readInt();
        creationDate = new Date(objectDataInput.readLong());
        lastAccessDate = new Date(objectDataInput.readLong());
        websiteURL = objectDataInput.readUTF();
        location = objectDataInput.readUTF();
        age = objectDataInput.readInt();
        aboutMe = objectDataInput.readUTF();
        views = objectDataInput.readInt();
        upVotes = objectDataInput.readInt();
        downVotes = objectDataInput.readInt();
    }
}
