package com.tenqube.visualbase.infrastructure.adapter.resource.remote;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SyncCategoryResponse implements Serializable {

    private ArrayList<Category> categories;
    private int version;

    public int getVersion() {
        return version;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public static class Category implements Serializable {
        @SerializedName("id")
        private int id;

        @SerializedName("categoryCode")
        private int categoryCode;

        @SerializedName("largeCategory")
        private String largeCategory;

        @SerializedName("mediumCategory")
        private String mediumCategory;

        @SerializedName("smallCategory")
        private String smallCategory;

        @SerializedName("isDeleted")
        private int isDeleted;

        public int getId() {
            return id;
        }

        public int getCategoryCode() {
            return categoryCode;
        }

        public String getLargeCategory() {
            return largeCategory;
        }

        public String getMediumCategory() {
            return mediumCategory;
        }

        public String getSmallCategory() {
            return smallCategory;
        }

        public boolean isDeleted() {
            return isDeleted == 1;
        }
    }


}
