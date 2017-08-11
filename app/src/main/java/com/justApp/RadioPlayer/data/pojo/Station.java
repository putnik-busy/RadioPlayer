package com.justApp.RadioPlayer.data.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "country",
        "image",
        "slug",
        "website",
        "twitter",
        "facebook",
        "total_listeners",
        "categories",
        "streams",
        "created_at",
        "updated_at"
})
public final class Station {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("country")
    private String country;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("website")
    private String website;
    @JsonProperty("twitter")
    private String twitter;
    @JsonProperty("facebook")
    private String facebook;
    @JsonProperty("total_listeners")
    private Integer totalListeners;
    @JsonProperty("categories")
    private List<Category> categories = new ArrayList<>();
    @JsonProperty("streams")
    private List<Stream> streams = new ArrayList<>();
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String mLastUpdate;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Image image) {
        this.image = image;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonProperty("website")
    public String getWebsite() {
        return website;
    }

    @JsonProperty("website")
    public void setWebsite(String website) {
        this.website = website;
    }

    @JsonProperty("twitter")
    public String getTwitter() {
        return twitter;
    }

    @JsonProperty("twitter")
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    @JsonProperty("facebook")
    public String getFacebook() {
        return facebook;
    }

    @JsonProperty("facebook")
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @JsonProperty("total_listeners")
    public Integer getTotalListeners() {
        return totalListeners;
    }

    @JsonProperty("total_listeners")
    public void setTotalListeners(Integer totalListeners) {
        this.totalListeners = totalListeners;
    }

    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @JsonProperty("streams")
    public List<Stream> getStreams() {
        return streams;
    }

    @JsonProperty("streams")
    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        mLastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equal(id, station.id) &&
                Objects.equal(name, station.name) &&
                Objects.equal(country, station.country) &&
                Objects.equal(image, station.image) &&
                Objects.equal(slug, station.slug) &&
                Objects.equal(website, station.website) &&
                Objects.equal(twitter, station.twitter) &&
                Objects.equal(facebook, station.facebook) &&
                Objects.equal(totalListeners, station.totalListeners) &&
                Objects.equal(categories, station.categories) &&
                Objects.equal(streams, station.streams) &&
                Objects.equal(createdAt, station.createdAt) &&
                Objects.equal(updatedAt, station.updatedAt) &&
                Objects.equal(mLastUpdate, station.mLastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, country, image, slug, website, twitter, facebook,
                totalListeners, categories, streams, createdAt, updatedAt, mLastUpdate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("country", country)
                .add("image", image)
                .add("slug", slug)
                .add("website", website)
                .add("twitter", twitter)
                .add("facebook", facebook)
                .add("totalListeners", totalListeners)
                .add("categories", categories)
                .add("streams", streams)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("mLastUpdate", mLastUpdate)
                .toString();
    }
}
