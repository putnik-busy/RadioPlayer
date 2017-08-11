package com.justApp.RadioPlayer.data.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "slug",
        "ancestry"
})
public final class Category {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("ancestry")
    private Object ancestry;
    private int mIdStation;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonProperty("ancestry")
    public Object getAncestry() {
        return ancestry;
    }

    @JsonProperty("ancestry")
    public void setAncestry(Object ancestry) {
        this.ancestry = ancestry;
    }

    public int getIdStation() {
        return mIdStation;
    }

    public void setIdStation(int idStation) {
        mIdStation = idStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return mIdStation == category.mIdStation &&
                Objects.equal(id, category.id) &&
                Objects.equal(title, category.title) &&
                Objects.equal(description, category.description) &&
                Objects.equal(slug, category.slug) &&
                Objects.equal(ancestry, category.ancestry);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description, slug, ancestry, mIdStation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("description", description)
                .add("slug", slug)
                .add("ancestry", ancestry)
                .add("mIdStation", mIdStation)
                .toString();
    }
}
