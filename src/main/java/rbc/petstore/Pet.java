package rbc.petstore;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
        
@Entity
public class Pet {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_entity_seq_gen")
    @SequenceGenerator(name="my_entity_seq_gen", sequenceName="pet_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    /*
    @Id
    @NotNull
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
*/
    
    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;
  /*
    @Size(max = 500)
    @Column(name = "photoUrls", nullable = true)
    private String photoUrls;
    */
    @Size(max = 25)
    @Column(name = "status", nullable = true)
    private String status;
    
    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name="category", nullable = true)
    private Category category;
    
    @ManyToMany
    @JoinTable(name="pet_tag", joinColumns={@JoinColumn(name="pet_id")}, inverseJoinColumns={@JoinColumn(name="tag_id")})
    private Collection<Tag> tags;
    
    @ElementCollection
    @CollectionTable(name ="urls")
    private List<String> photoUrls = new ArrayList<String>();
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    Pet() {
    }

    public Pet(final Long id, final String name, final List<String> photoUrls, final String status) {
        this.id = id;
        this.name = name;
        this.photoUrls = photoUrls;
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("status", status)
                .add("category", (category==null?"":category.getId() + " " + category.getName()))
                .toString();
    }
}
