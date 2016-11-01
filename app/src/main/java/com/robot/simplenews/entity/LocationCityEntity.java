package com.robot.simplenews.entity;

import android.text.TextUtils;

/**
 * 定位实体
 */
public class LocationCityEntity {
    public static final String DEFAULT_CITY  = "深圳";
    public static final String DEFAULT_CITY_UNIT  = "市";
    // http://api.map.baidu.com/geocoder?output=json&referer=32D45CBEEC107315C553AD1131915D366EEF79B4&location=30.281618,120.116257
    public static final String FORMAT_OUTPUT = "json";
    public static final String REFERER = "32D45CBEEC107315C553AD1131915D366EEF79B4";

    private String status;
    private ResultEntity result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private LocationEntity location;
        private String formatted_address;
        private String business;
        private AddressComponentEntity addressComponent;
        private int cityCode;

        public LocationEntity getLocation() {
            return location;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public AddressComponentEntity getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentEntity addressComponent) {
            this.addressComponent = addressComponent;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public static class LocationEntity {
            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class AddressComponentEntity {
            private String city;
            private String direction;
            private String distance;
            private String district;
            private String province;
            private String street;
            private String street_number;

            public String getCity() {
                if (!TextUtils.isEmpty(city)) {
                    city = city.replace(DEFAULT_CITY_UNIT, "");
                }
                return city;
            }

            /**
             * 注意：GSON是不调用set方法的
             * @param city
             */
            public void setCity(String city) {
                if (!TextUtils.isEmpty(city)) {
                    city = city.replace(DEFAULT_CITY_UNIT, "");
                }
                this.city = city;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }
        }
    }
}
