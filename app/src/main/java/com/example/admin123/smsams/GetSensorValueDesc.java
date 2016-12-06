package com.example.admin123.smsams;

public class GetSensorValueDesc {

    private Integer soilData;
    private String readableData;

    public String ReturnSoilValueDescription(int soil_data) {

        this.soilData = soil_data;

        if (soilData < 300) {
            readableData = "In Water";
        }
        if (soilData > 300 && soilData < 700) {
            readableData = "Humid Soil";
        }
        if (soilData > 700 && soilData < 950) {
            readableData = "Dry Soil";
        }

        return readableData;
    }
}
