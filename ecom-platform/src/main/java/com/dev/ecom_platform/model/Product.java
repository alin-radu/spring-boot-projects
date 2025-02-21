package com.dev.ecom_platform.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Entity
@Data
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;
    private boolean available;
    private int quantity;

    private String imageName;
    private String imageType;

    @Lob
    private byte[] imageData;
}
