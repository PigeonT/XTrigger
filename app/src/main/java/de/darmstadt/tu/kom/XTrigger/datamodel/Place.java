package de.darmstadt.tu.kom.XTrigger.datamodel;

import java.util.ArrayList;
import java.util.List;

public final class Place {

    private final List<Address> addressList;

    private boolean allowEverywhere;

    private boolean triggerSomewhereElse;

    private boolean triggerInTheseStreets;

    public Place() {
        super();
        addressList = new ArrayList<>();
        triggerSomewhereElse = false;
        triggerInTheseStreets = true;
        allowEverywhere = false;
    }

    public void addtoAddressList(Address newAddress) {
        //maximal 3 addresses
        if (addressList.size() < 3) {
            addressList.add(newAddress);
        }
    }

    public boolean isTriggerSomewhereElse() {
        return triggerSomewhereElse;
    }

    public void setTriggerSomewhereElse(boolean triggerSomewhereElse) {
        this.triggerSomewhereElse = triggerSomewhereElse;
    }

    public boolean isTriggerInTheseStreets() {
        return triggerInTheseStreets;
    }

    public void setTriggerInTheseStreets(boolean triggerNotInTheseStreets) {
        this.triggerInTheseStreets = triggerNotInTheseStreets;
    }

    public List<Address> getAddressList() {
        return this.addressList;
    }

    public boolean isAllowedEverywhere() {
        return allowEverywhere;
    }

    public void setAllowedEverywhere(boolean allowEverywhere) {
        this.allowEverywhere = allowEverywhere;
    }

    public final static class Address {
        private double latitude;
        private double longitude;
        private String name;

        public Address() {
            super();
        }

        public Address(String name) {
            this(0, 0, name, false);
        }

        public Address(double latitude, double longitude, String name) {
            this(latitude, longitude, name, false);
        }

        public Address(double latitude, double longitude, String name, boolean allowEverywhere) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.name = name;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
