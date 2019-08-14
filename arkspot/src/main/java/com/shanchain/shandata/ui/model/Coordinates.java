package com.shanchain.shandata.ui.model;

import java.util.List;

public class Coordinates {
    /**
     * roomId : 12826211
     * roomName : 20.04318544,110.31167518
     * focusLatitude : 20.04318544
     * focusLongitude : 110.31167518
     * coordinates : [{"latitude":"20.04048747","longitude":"110.30880325"},{"latitude":"20.04588340","longitude":"110.30880325"},{"latitude":"20.04588340","longitude":"110.31454700"},{"latitude":"20.04048747","longitude":"110.31454700"}]
     */

    private String roomId;
    private String roomName;
    private String focusLatitude;
    private String focusLongitude;
    private int status;
    private String diggingId;
    private List<CoordinatesBean> coordinates;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFocusLatitude() {
        return focusLatitude;
    }

    public void setFocusLatitude(String focusLatitude) {
        this.focusLatitude = focusLatitude;
    }

    public String getFocusLongitude() {
        return focusLongitude;
    }

    public void setFocusLongitude(String focusLongitude) {
        this.focusLongitude = focusLongitude;
    }

    public List<CoordinatesBean> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinatesBean> coordinates) {
        this.coordinates = coordinates;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDiggingId() {
        return diggingId;
    }

    public void setDiggingId(String diggingId) {
        this.diggingId = diggingId;
    }

    public static class CoordinatesBean {
        /**
         * latitude : 20.04048747
         * longitude : 110.30880325
         */

        private String latitude;
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
