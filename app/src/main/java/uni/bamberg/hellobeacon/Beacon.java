package uni.bamberg.hellobeacon;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Beacon implements BeaconIF, Parcelable {

    private String uuid;
    private int major;
    private int minor;

    Beacon(String uuid, int major, int minor) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    private Beacon(Parcel source) {
        uuid = source.readString();
        major = source.readInt();
        minor = source.readInt();
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Integer getMajor() {
        return major;
    }

    @Override
    public void setMajor(int major) {
        this.major = major;
    }

    @Override
    public Integer getMinor() {
        return minor;
    }

    @Override
    public void setMinor(int minor) {
        this.minor = minor;
    }

    @Override
    public String toString() {
        return uuid + " " + major + " " + minor;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        if (objectToCompare instanceof Beacon) {
            Beacon beaconToCompare = (Beacon) objectToCompare;
            return this.uuid.equals(beaconToCompare.uuid) && this.major == beaconToCompare.major && this.minor == beaconToCompare.minor;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return major + minor;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeInt(major);
        dest.writeInt(minor);
    }
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {

        @Override
        public Beacon createFromParcel(Parcel source) {
            return new Beacon(source);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };
}
