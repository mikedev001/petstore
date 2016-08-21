package rbc.petstore;

import java.io.Serializable;

public class PetImageLight implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;
    
    public PetImageLight(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
