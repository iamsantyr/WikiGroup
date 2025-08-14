package com.wikigroup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.text.Normalizer;

@Entity
@Table(name = "contacto")
public class Contacto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Size(max = 100)
  @Column(length = 100, nullable = false)
  private String nombres;

  @NotBlank @Size(max = 100)
  @Column(length = 100, nullable = false)
  private String apellidos;

  @NotBlank @Size(max = 100)
  @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", message = "Correo inválido")
  @Column(length = 100, nullable = false)
  private String correo;

  @NotNull @Min(0) @Max(16)
  @Column(nullable = false)
  private Integer semestre;

  @NotBlank
  @Lob @Column(nullable = false)
  private String descripcion;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  /* Normalización: MAYÚSCULAS, sin tildes ni ñ */
  private static String toUpperNoAccents(String s) {
    if (s == null) return null;
    String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    n = n.replaceAll("[ñÑ]", "N");
    return n.toUpperCase();
  }

  @PrePersist @PreUpdate
  public void normalize() {
    if (nombres != null) nombres = toUpperNoAccents(nombres.trim());
    if (apellidos != null) apellidos = toUpperNoAccents(apellidos.trim());
    if (descripcion != null) descripcion = toUpperNoAccents(descripcion.trim());
    if (correo != null) correo = toUpperNoAccents(correo.trim()).replaceAll("\\s+","");
    if (createdAt == null) createdAt = Instant.now();
  }

  // Getters & setters
  public Long getId() { return id; }
  public String getNombres() { return nombres; }
  public void setNombres(String nombres) { this.nombres = nombres; }
  public String getApellidos() { return apellidos; }
  public void setApellidos(String apellidos) { this.apellidos = apellidos; }
  public String getCorreo() { return correo; }
  public void setCorreo(String correo) { this.correo = correo; }
  public Integer getSemestre() { return semestre; }
  public void setSemestre(Integer semestre) { this.semestre = semestre; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
